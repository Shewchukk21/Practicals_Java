package com.example.order.concurrency;

import com.example.order.processing.Clothing;
import com.example.order.processing.Electronics;
import com.example.order.processing.OrderProcessingException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;
import com.example.order.storage.OrderStorage;
import com.github.javafaker.Faker;

import java.util.List;

// import com.example.order.processing.internal.OrderValidator; // помилка: пакет не експортується

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Faker faker = new Faker();
        OrderStorage<Product> storage = new OrderStorage<>();

        // створення товарів через Builder (Lombok) з даними JavaFaker
        for (int i = 0; i < 3; i++) {
            Electronics electronics = Electronics.builder()
                    .name(faker.commerce().productName())
                    .brand(faker.company().name())
                    .price(faker.number().randomDouble(2, 100, 5000))
                    .build();
            Clothing clothing = Clothing.builder()
                    .name(faker.commerce().productName())
                    .size(faker.options().option("S", "M", "L", "XL"))
                    .price(faker.number().randomDouble(2, 10, 500))
                    .build();
            storage.add(new OrderProcessor<>(electronics));
            storage.add(new OrderProcessor<>(clothing));
        }
        // товар з некоректною ціною — для демонстрації обробки виключення
        storage.add(new OrderProcessor<>(Clothing.builder().name("Broken item").size("M").price(0).build()));

        List<OrderProcessor<? extends Product>> orderList = storage.getAll();
        orderList.forEach(order -> System.out.println(order.getProduct()));

        System.out.println("\n--- Фільтрація за типом (лямбда) ---");
        System.out.println("Electronics: " + OrderProcessor.filterByType(orderList, Electronics.class).size());
        System.out.println("Clothing: " + OrderProcessor.filterByType(orderList, Clothing.class).size());

        System.out.println("\n--- Лямбда-вираз у потоці ---");
        Runnable orderTask = () -> System.out.println("Processing order in thread " + Thread.currentThread().getName());
        Thread thread = new Thread(orderTask);
        thread.start();
        thread.join();

        System.out.println("\n--- Посилання на метод ---");
        orderList.forEach(OrderProcessor::processOrder);

        System.out.println("\n--- Багатопотокова обробка ---");
        new OrderThreadManager(3).processAll(orderList);

        System.out.println("\n--- Виключення при роботі з узагальненими типами ---");
        try {
            Electronics e = orderList.get(1).getProductAs(Electronics.class); // там Clothing
            System.out.println(e);
        } catch (OrderProcessingException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
