package com.example.order.processing.internal;

import com.example.order.processing.OrderProcessingException;
import com.example.order.processing.Product;

// Внутрішній клас модуля: пакет не експортується в module-info.java
public class OrderValidator {

    public static void validate(Product product) throws OrderProcessingException {
        if (product.getPrice() <= 0) {
            throw new OrderProcessingException("Invalid price for product: " + product.getName());
        }
    }
}
