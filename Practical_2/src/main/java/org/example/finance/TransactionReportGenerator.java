package org.example.finance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Виведення звітів у консоль.
 * <p>
 * Абстрактний клас зі статичними методами: генератор звітів не має стану,
 * тому екземпляр не потрібен.
 */
public abstract class TransactionReportGenerator {

    /** Скільки гривень відповідає одному символу '*' у текстових діаграмах. */
    public static final double UAH_PER_STAR = 1000;

    public static void printBalanceReport(double totalBalance) {
        System.out.printf("Загальний баланс: %.2f грн%n", totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println("  " + expense.getDate() + "  " + expense.getDescription() + ": " + expense.getAmount());
        }
    }

    /** Самостійна: найбільша та найменша витрата за період. */
    public static void printExpenseExtremesReport(String from, String to,
                                                  Optional<Transaction> largest,
                                                  Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + from + " — " + to + ":");
        if (largest.isEmpty()) {
            System.out.println("  витрат не знайдено");
            return;
        }
        System.out.println("  найбільша: " + format(largest.get()));
        System.out.println("  найменша:  " + format(smallest.get()));
    }

    /** Самостійна: текстова діаграма витрат по категоріях. */
    public static void printExpensesByCategoryChart(Map<String, Double> expensesByCategory) {
        printChart("Витрати по категоріях", expensesByCategory);
    }

    /** Самостійна: текстова діаграма витрат по місяцях. */
    public static void printExpensesByMonthChart(Map<String, Double> expensesByMonth) {
        printChart("Витрати по місяцях", expensesByMonth);
    }

    /** Будує рядки вигляду "Сільпо            12350.00 грн | ************". */
    public static String buildChart(String title, Map<String, Double> data) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (* = ").append((int) UAH_PER_STAR).append(" грн):\n");
        int labelWidth = data.keySet().stream().mapToInt(String::length).max().orElse(0);
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            sb.append(String.format("  %-" + labelWidth + "s %10.2f грн | %s%n",
                    entry.getKey(), entry.getValue(), stars(entry.getValue())));
        }
        return sb.toString();
    }

    /** Кількість зірочок: сума / 1000, округлена до цілого; ненульова сума менше 500 грн все одно отримує одну *. */
    public static String stars(double amount) {
        int count = (int) Math.round(amount / UAH_PER_STAR);
        if (count == 0 && amount > 0) {
            count = 1;
        }
        return "*".repeat(count);
    }

    private static void printChart(String title, Map<String, Double> data) {
        System.out.print(buildChart(title, data));
    }

    private static String format(Transaction t) {
        return t.getDate() + "  " + t.getDescription() + ": " + t.getAmount();
    }
}
