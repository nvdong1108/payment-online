package com.demo.model;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import com.stripe.model.Product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCustomer extends Product {

    public ProductCustomer(Product product) {
        super();
        this.setId(product.getId());
        this.setName(product.getName());
        this.setDescription(product.getDescription());
        this.setImages(product.getImages());
        this.setName(product.getName());
    }
    String price;

}