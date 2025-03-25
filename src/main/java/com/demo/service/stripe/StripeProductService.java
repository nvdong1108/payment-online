package com.demo.service.stripe;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Product;
import com.stripe.model.ProductSearchResult;
import com.stripe.param.ProductSearchParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeProductService {

    public List<Product> searchProduct() {

        Stripe.apiKey = "sk_test_51QsFdSDs1SIogd7U7r9ojcBLR1IyDICMRFOjcWdGSqRXsmGtKd39KF5BosMPFrryQyIaU6WV4QddW8Cp0TFsBEG500T6xqRO9p";

        try {
            ProductSearchParams params = ProductSearchParams.builder()
                    .setQuery("active:'true'")
                    .build();

            ProductSearchResult products = Product.search(params);

            return products.getData();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
