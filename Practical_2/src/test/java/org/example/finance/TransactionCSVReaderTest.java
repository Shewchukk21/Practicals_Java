package org.example.finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/** Самостійна: тест читання даних із CSV. */
class TransactionCSVReaderTest {

    @Test
    void testReadTransactionsFromCsvResource() {
        List<Transaction> transactions =
                TransactionCSVReader.readTransactions(DataSource.fromResource("test_transactions.csv"));

        // Порожній рядок у файлі пропускається
        Assertions.assertEquals(4, transactions.size(), "Кількість прочитаних транзакцій неправильна");
        Assertions.assertEquals(new Transaction("05-12-2023", -7850.0, "Сільпо"), transactions.get(0));
        Assertions.assertEquals(new Transaction("10-12-2023", 80000.0, "Зарплата"), transactions.get(1));
        Assertions.assertEquals("Кав'ярня", transactions.get(2).getDescription(), "Кирилиця/апостроф прочитані неправильно");
    }

    @Test
    void testParseSkipsInvalidLines() {
        List<Transaction> transactions = TransactionCSVReader.parseTransactions(List.of(
                "01-01-2024,100,Дохід",
                "зламаний рядок",
                "02-01-2024,не-число,Витрата",
                "03-01-2024,-50,Витрата"));

        Assertions.assertEquals(2, transactions.size(), "Некоректні рядки мали бути пропущені");
    }

    @Test
    void testDescriptionWithComma() {
        Transaction t = TransactionCSVReader.parseLine("01-01-2024,-300,Кафе, вечеря");
        Assertions.assertEquals("Кафе, вечеря", t.getDescription());
    }

    @Test
    void testMissingResourceReturnsEmptyList() {
        List<Transaction> transactions =
                TransactionCSVReader.readTransactions(DataSource.fromResource("no_such_file.csv"));
        Assertions.assertTrue(transactions.isEmpty());
    }
}
