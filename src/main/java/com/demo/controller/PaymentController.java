package com.demo.controller;

import com.demo.config.JwtUtil;
import com.demo.entity.CustomUserDetails;
import com.demo.entity.Settings;
import com.demo.entity.User;
import com.demo.model.ProductCustomer;
import com.demo.repository.SettingsRepository;
import com.demo.service.stripe.StripePriceService;
import com.demo.service.stripe.StripeProductService;
import com.stripe.model.Price;
import com.stripe.model.Product;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
public class PaymentController {

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private StripeProductService stripeProductService;

	@Autowired
	private StripePriceService stripePriceService;

	BigDecimal ONE_HUNDRED = new BigDecimal(100);

	@GetMapping("/payment")
	public String payment(@RequestHeader(value = "Authorization", required = false) String token, Model model) {

		if (token == null && token.isEmpty()) {
			return "redirect:/login";
		}
		String jwt = token.substring(7);
		if (!JwtUtil.validateToken(jwt)) {
			return "redirect:/login";
		}

		String username = JwtUtil.getUsernameFromToken(jwt);
		model.addAttribute("username", username);
		return "payment";
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/checkout")
	public String checkout(Model model) {
		try {
			List<ProductCustomer> listProductCustomer = new ArrayList<>();
			List<Product> listProduct = stripeProductService.searchProduct();

			listProduct.stream().forEach(product -> {
				ProductCustomer productCustomer = new ProductCustomer(product);
				Price price = stripePriceService.getPrice(product);

				if (price != null) {
					BigDecimal priceBigDecimal = new BigDecimal(price.getUnitAmount()).divide(ONE_HUNDRED);
					productCustomer.setCurrency(price.getCurrency());
					productCustomer.setPrice(priceBigDecimal);
				}

				listProductCustomer.add(productCustomer);
			});

			BigDecimal subTotal = listProductCustomer.stream().map(ProductCustomer::getPrice).reduce(BigDecimal::add)
					.get();
			BigDecimal vat = subTotal.multiply(new BigDecimal(10)).divide(ONE_HUNDRED);
			BigDecimal shippingFee = new BigDecimal(0);
			BigDecimal total = subTotal.add(vat).add(shippingFee);
			String currency = listProductCustomer.get(0).getCurrency();

			model.addAttribute("products", listProductCustomer);
			model.addAttribute("currency", currency);
			model.addAttribute("subtotal", subTotal);
			model.addAttribute("vat", vat);
			model.addAttribute("shippingFee", shippingFee);
			model.addAttribute("total", total);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "checkout";
	}

	@GetMapping("/payment/return")
	public String returnPage(@RequestParam("reference") String orderId,
			@RequestParam("status") String status, Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("status", status);
		return "payment_result";
	}
}