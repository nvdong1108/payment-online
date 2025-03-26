package com.demo.service.stripe;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.PriceSearchResult;
import com.stripe.model.Product;
import com.stripe.param.PriceSearchParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripePriceService {

    public Price getPrice(Product product) {
        Stripe.apiKey = "sk_test_51QsFdSDs1SIogd7U7r9ojcBLR1IyDICMRFOjcWdGSqRXsmGtKd39KF5BosMPFrryQyIaU6WV4QddW8Cp0TFsBEG500T6xqRO9p";
        try {

            PriceSearchParams params = PriceSearchParams.builder()
                    .setQuery("active:'true' AND product:'" + product.getId() + "'")
                    .build();

            PriceSearchResult prices = Price.search(params);
            
            return prices.getData().get(0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
