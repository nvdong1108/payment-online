package com.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.PriceSearchResult;
import com.stripe.model.Product;
import com.stripe.model.ProductCollection;
import com.stripe.model.ProductSearchResult;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceSearchParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductListParams;
import com.stripe.param.ProductSearchParams;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeService {

    String host = "http://localhost:8090";

    public String payment() {
        try {
            Stripe.apiKey = "sk_test_51QsFdSDs1SIogd7U7r9ojcBLR1IyDICMRFOjcWdGSqRXsmGtKd39KF5BosMPFrryQyIaU6WV4QddW8Cp0TFsBEG500T6xqRO9p";

            ProductSearchParams paramsSearchParams = ProductSearchParams.builder()
                    .setQuery("active:'true' AND name~'Red Pen'")
                    .setLimit(1L)
                    .build();
            ProductSearchResult products = Product.search(paramsSearchParams);

            Product pdt = products.getData().get(0);



            PriceSearchParams paramsPriceSearchParams = PriceSearchParams.builder()
                    .setQuery("active:'true' AND product:'" + pdt.getId() + "'")
                    .build();

            PriceSearchResult prices = Price.search(paramsPriceSearchParams);

            Price price = prices.getData().get(0);

            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(pdt.getName())
                    .build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency(price.getCurrency())
                    .setUnitAmount(price.getUnitAmount())
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(priceData)
                    .build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .addLineItem(lineItem)
                    .setSuccessUrl(host + "/success")
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BANCONTACT)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.EPS)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.IDEAL)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.P24)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.SEPA_DEBIT)
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

}
