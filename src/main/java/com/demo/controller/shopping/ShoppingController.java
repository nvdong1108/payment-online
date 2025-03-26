package com.demo.controller.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.ProductCustomer;
import com.demo.service.stripe.StripePriceService;
import com.demo.service.stripe.StripeProductService;
import com.stripe.model.Price;
import com.stripe.model.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShoppingController {

    @Autowired
    private StripeProductService stripeProductService;

    @Autowired
    private StripePriceService stripePriceService;

    BigDecimal ONE_HUNDRED = new BigDecimal(100);

    @GetMapping("/shopping")
    public String index(
            Model model) {

        try {
            List<ProductCustomer> listProductCustomer = new ArrayList<>();
            List<Product> listProduct = stripeProductService.searchProduct();
            listProduct.stream().forEach(product -> {
                ProductCustomer productCustomer = new ProductCustomer(product);
                Price price = stripePriceService.getPrice(product);
                if (price != null) {
                    BigDecimal priceBigDecimal = new BigDecimal(price.getUnitAmount()).divide(ONE_HUNDRED);
                    productCustomer.setPrice(priceBigDecimal);
                    productCustomer.setCurrency(price.getCurrency());

                }
                listProductCustomer.add(productCustomer);
            });
            model.addAttribute("products", listProductCustomer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/shopping/shopping";
    }

    @GetMapping("/shopping/payment-success")
    public String getMethodName(Model model) {

        String orderId = "1234567890";
        String orderDate = "1234567890";
        String paymentMethod = "1234567890";
        BigDecimal totalAmount = new BigDecimal(1234567890);
        String customerEmail = "1234567890";

        model.addAttribute("orderNumber", orderId);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("customerEmail", customerEmail);
        return "/shopping/payment-success";
    }

}
