package org.example.finance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Аналіз транзакцій.
 * <p>
 * Абстрактний клас зі статичними методами: список транзакцій передається
 * параметром, тому не потрібно створювати об'єкт аналізатора.
 */
public abstract class TransactionAnalyzer {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    /** Задача 2: загальний баланс усіх транзакцій. */
    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    /** Задача 3: кількість транзакцій за місяць у форматі "MM-yyyy". */
    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            if (monthOf(transaction).equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    /** Задача 5: 10 найбільших витрат (від найбільшої до меншої). */
    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)                        // лише витрати
                .sorted(Comparator.comparing(Transaction::getAmount))  // -9100 стоїть раніше за -600
                .limit(10)
                .collect(Collectors.toList());
    }

    /** Самостійна: всі витрати в періоді [from; to] включно. Дати у форматі dd-MM-yyyy. */
    public static List<Transaction> findExpensesInPeriod(List<Transaction> transactions, String from, String to) {
        LocalDate start = LocalDate.parse(from, DATE_FORMATTER);
        LocalDate end = LocalDate.parse(to, DATE_FORMATTER);
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = dateOf(t);
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }

    /** Самостійна: найбільша витрата за період (якщо витрат не було — порожній Optional). */
    public static Optional<Transaction> findLargestExpense(List<Transaction> transactions, String from, String to) {
        return findExpensesInPeriod(transactions, from, to).stream()
                .min(Comparator.comparing(Transaction::getAmount)); // найменше число = найбільша витрата
    }

    /** Самостійна: найменша витрата за період. */
    public static Optional<Transaction> findSmallestExpense(List<Transaction> transactions, String from, String to) {
        return findExpensesInPeriod(transactions, from, to).stream()
                .max(Comparator.comparing(Transaction::getAmount));
    }

    /** Самостійна: сума витрат по категоріях (за спаданням суми). Суми додатні. */
    public static Map<String, Double> expensesByCategory(List<Transaction> transactions) {
        Map<String, Double> totals = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())));
        // Сортуємо: на що витрачено найбільше — вгорі
        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /** Самостійна: сума витрат по місяцях у хронологічному порядку. Ключ — "MM-yyyy". */
    public static Map<String, Double> expensesByMonth(List<Transaction> transactions) {
        // TreeMap по YearMonth сортує хронологічно, потім перетворюємо ключ у "MM-yyyy"
        Map<YearMonth, Double> byMonth = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(t -> YearMonth.from(dateOf(t)),
                        TreeMap::new, Collectors.summingDouble(t -> -t.getAmount())));
        Map<String, Double> result = new LinkedHashMap<>();
        byMonth.forEach((month, sum) -> result.put(month.format(MONTH_FORMATTER), sum));
        return result;
    }

    private static LocalDate dateOf(Transaction transaction) {
        return LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
    }

    private static String monthOf(Transaction transaction) {
        return dateOf(transaction).format(MONTH_FORMATTER);
    }
}
