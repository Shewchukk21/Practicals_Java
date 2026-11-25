package com.example.order.processing;

import com.example.order.processing.internal.OrderValidator;

import java.util.List;

public class OrderProcessor<T extends Product> {

    private final T product;

    public OrderProcessor(T product) {
        this.product = product;
    }

    public T getProduct() {
        return product;
    }

    public void process() throws OrderProcessingException {
        OrderValidator.validate(product);
        System.out.println("Processing order for: " + product.getName()
                + " in thread " + Thread.currentThread().getName());
    }

    // для виклику через посилання на метод: orderList.forEach(OrderProcessor::processOrder)
    public void processOrder() {
        try {
            process();
        } catch (OrderProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // приведення товару до конкретного підтипу з обробкою помилки типу
    public <R extends Product> R getProductAs(Class<R> type) throws OrderProcessingException {
        if (!type.isInstance(product)) {
            throw new OrderProcessingException("Product " + product.getName() + " is not "
                    + type.getSimpleName() + ", but " + product.getClass().getSimpleName());
        }
        return type.cast(product);
    }

    // фільтрація замовлень за типом товару за допомогою лямбда-виразу
    public static <R extends Product> List<OrderProcessor<? extends Product>> filterByType(
            List<OrderProcessor<? extends Product>> orders, Class<R> type) {
        return orders.stream()
                .filter(order -> type.isInstance(order.getProduct()))
                .toList();
    }
}
