package com.example.order.storage;

import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Потокобезпечне сховище замовлень
public class OrderStorage<T extends Product> {

    private final List<OrderProcessor<? extends T>> orders = new CopyOnWriteArrayList<>();

    public void add(OrderProcessor<? extends T> order) {
        orders.add(order);
    }

    public List<OrderProcessor<? extends T>> getAll() {
        return List.copyOf(orders);
    }
}
