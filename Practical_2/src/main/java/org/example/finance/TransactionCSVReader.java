package org.example.finance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Читає транзакції з CSV і перетворює їх в об'єкти Transaction.
 * <p>
 * Клас абстрактний і має лише статичні методи: він не зберігає жодного стану,
 * тому створювати його екземпляри немає сенсу — методи викликаються напряму:
 * {@code TransactionCSVReader.readTransactions(path)}.
 */
public abstract class TransactionCSVReader {

    /** Читає транзакції за адресою: http(s)-посилання або шлях до файлу. */
    public static List<Transaction> readTransactions(String location) {
        return readTransactions(DataSource.of(location));
    }

    /** Читає транзакції з будь-якого джерела даних. */
    public static List<Transaction> readTransactions(DataSource source) {
        try {
            return parseTransactions(source.readLines());
        } catch (IOException e) {
            System.err.println("Не вдалося прочитати дані: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** Обробка даних: перетворює рядки CSV у список транзакцій. */
    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                transactions.add(parseLine(line));
            } catch (RuntimeException e) {
                System.err.println("Пропущено некоректний рядок: " + line);
            }
        }
        return transactions;
    }

    /** Розбирає один рядок вигляду "05-12-2023,-7850,Сільпо". */
    public static Transaction parseLine(String line) {
        // limit = 3: опис може містити коми, вони не повинні його розрізати
        String[] values = line.split(",", 3);
        return new Transaction(values[0].trim(), Double.parseDouble(values[1].trim()), values[2].trim());
    }
}
