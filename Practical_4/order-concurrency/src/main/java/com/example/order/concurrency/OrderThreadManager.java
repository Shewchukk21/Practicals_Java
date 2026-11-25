package com.example.order.concurrency;

import com.example.order.processing.OrderProcessor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderThreadManager {

    private final ExecutorService executor;

    public OrderThreadManager(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
    }

    public void processAll(List<? extends OrderProcessor<?>> orders) throws InterruptedException {
        for (OrderProcessor<?> order : orders) {
            executor.submit(order::processOrder);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
