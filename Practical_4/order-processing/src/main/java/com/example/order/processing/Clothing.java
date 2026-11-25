package com.example.order.processing;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Clothing extends Product {

    private final String size;

    @Builder
    public Clothing(String name, double price, String size) {
        super(name, price);
        this.size = size;
    }

    @Override
    public String toString() {
        return "Clothing{name='" + getName() + "', size='" + size + "', price=" + getPrice() + "}";
    }
}
