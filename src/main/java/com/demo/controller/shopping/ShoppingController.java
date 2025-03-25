package com.demo.controller.shopping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/shopping")
    public String index(Model model) {
        try {
            List<ProductCustomer> listProductCustomer = new ArrayList<>();
            List<Product> listProduct = stripeProductService.searchProduct();
            listProduct.stream().forEach(product -> {
                ProductCustomer productCustomer = new ProductCustomer(product);
                Price price = stripePriceService.getPrice(product.getName());
                if (price != null) {
                    productCustomer.setPrice(price.getId());
                }
                listProductCustomer.add(productCustomer);
            });
            model.addAttribute("products", listProductCustomer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/shopping/shopping";
    }

}
