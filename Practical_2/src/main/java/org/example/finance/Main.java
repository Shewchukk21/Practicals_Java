package org.example.finance;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Шлях можна передати аргументом (URL або локальний файл), інакше беремо файл із завдання
        String filePath = args.length > 0 ? args[0] : "https://informer.com.ua/dut/java/pr2.csv";

        // Задача 1: читання. Екземпляри класів не створюються — лише статичні виклики
        List<Transaction> transactions = TransactionCSVReader.readTransactions(filePath);
        System.out.println("Прочитано транзакцій: " + transactions.size());
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println();

        // Задача 2: загальний баланс
        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        // Задача 3: кількість транзакцій за місяць
        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);
        System.out.println();

        // Задача 5: 10 найбільших витрат
        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);
        System.out.println();

        // Самостійна: найбільша і найменша витрати за період
        String from = "01-12-2023";
        String to = "31-12-2023";
        TransactionReportGenerator.printExpenseExtremesReport(from, to,
                TransactionAnalyzer.findLargestExpense(transactions, from, to),
                TransactionAnalyzer.findSmallestExpense(transactions, from, to));
        System.out.println();

        // Самостійна: текстові діаграми витрат по категоріях і місяцях
        TransactionReportGenerator.printExpensesByCategoryChart(TransactionAnalyzer.expensesByCategory(transactions));
        System.out.println();
        TransactionReportGenerator.printExpensesByMonthChart(TransactionAnalyzer.expensesByMonth(transactions));
    }
}
