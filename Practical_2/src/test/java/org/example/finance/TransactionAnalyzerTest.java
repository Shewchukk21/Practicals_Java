package org.example.finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        // Створення тестових даних
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        // Виклик статичного методу — екземпляр аналізатора не потрібен
        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");

        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    /** Самостійна: тест на визначення 10 найбільших витрат. */
    @Test
    public void testFindTopExpenses() {
        // 12 витрат від -100 до -1200 і два доходи, які не повинні потрапити в результат
        List<Transaction> transactions = new java.util.ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction("01-01-2024", -100.0 * i, "Витрата " + i));
        }
        transactions.add(new Transaction("02-01-2024", 50000.0, "Зарплата"));
        transactions.add(new Transaction("03-01-2024", 10.0, "Кешбек"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(10, top.size(), "Має бути рівно 10 витрат");
        Assertions.assertEquals(-1200.0, top.get(0).getAmount(), "Першою має бути найбільша витрата");
        Assertions.assertEquals(-300.0, top.get(9).getAmount(), "Останньою має бути 10-та за величиною витрата");
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "У результаті не повинно бути доходів");
    }

    @Test
    public void testFindTopExpensesWhenFewerThanTen() {
        List<Transaction> transactions = List.of(
                new Transaction("01-01-2024", -10.0, "A"),
                new Transaction("01-01-2024", 100.0, "Дохід"),
                new Transaction("01-01-2024", -30.0, "B"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(2, top.size());
        Assertions.assertEquals("B", top.get(0).getDescription());
    }

    @Test
    public void testLargestAndSmallestExpenseInPeriod() {
        List<Transaction> transactions = List.of(
                new Transaction("30-11-2023", -9999.0, "Поза періодом"),
                new Transaction("01-12-2023", -500.0, "Межа періоду"),
                new Transaction("15-12-2023", -3000.0, "Велика"),
                new Transaction("20-12-2023", 80000.0, "Дохід"),
                new Transaction("31-12-2023", -100.0, "Мала"),
                new Transaction("01-01-2024", -50.0, "Поза періодом"));

        Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, "01-12-2023", "31-12-2023");
        Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, "01-12-2023", "31-12-2023");

        Assertions.assertEquals("Велика", largest.orElseThrow().getDescription());
        Assertions.assertEquals("Мала", smallest.orElseThrow().getDescription());
    }

    @Test
    public void testNoExpensesInPeriod() {
        List<Transaction> transactions = List.of(new Transaction("10-12-2023", 1000.0, "Дохід"));

        Assertions.assertTrue(TransactionAnalyzer.findLargestExpense(transactions, "01-12-2023", "31-12-2023").isEmpty());
    }

    @Test
    public void testExpensesByCategoryAndMonth() {
        List<Transaction> transactions = List.of(
                new Transaction("05-12-2023", -1000.0, "Сільпо"),
                new Transaction("05-01-2024", -2500.0, "Сільпо"),
                new Transaction("07-01-2024", -700.0, "Аптека"),
                new Transaction("10-01-2024", 85000.0, "Зарплата"));

        Map<String, Double> byCategory = TransactionAnalyzer.expensesByCategory(transactions);
        Assertions.assertEquals(Map.of("Сільпо", 3500.0, "Аптека", 700.0), byCategory);
        Assertions.assertEquals("Сільпо", byCategory.keySet().iterator().next(), "Найбільша категорія має бути першою");

        Map<String, Double> byMonth = TransactionAnalyzer.expensesByMonth(transactions);
        Assertions.assertEquals(List.of("12-2023", "01-2024"), List.copyOf(byMonth.keySet()), "Місяці мають іти хронологічно");
        Assertions.assertEquals(3200.0, byMonth.get("01-2024"));
    }

    @Test
    public void testStars() {
        Assertions.assertEquals("", TransactionReportGenerator.stars(0));
        Assertions.assertEquals("*", TransactionReportGenerator.stars(300));
        Assertions.assertEquals("***", TransactionReportGenerator.stars(3200));
        Assertions.assertEquals("*********", TransactionReportGenerator.stars(9100));
    }
}
