package org.example.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель однієї банківської транзакції.
 * Lombok генерує геттери, сеттери, equals/hashCode, toString та конструктори.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String date;        // дата у форматі dd-MM-yyyy
    private double amount;      // сума: > 0 — дохід, < 0 — витрата
    private String description; // опис, він же категорія ("Сільпо", "Аптека"...)
}
