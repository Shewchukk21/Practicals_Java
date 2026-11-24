package org.example.calculator;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        run(new Scanner(System.in), System.out);
    }

    /**
     * Діалог з користувачем. Scanner і PrintStream передаються параметрами,
     * щоб у тестах можна було підставити "фальшивий" ввід і перехопити вивід.
     */
    public static void run(Scanner in, PrintStream out) {
        Calculator calculator = new Calculator();
        out.println("Калькулятор. Операції: +  -  *  /  sqrt. Для виходу введіть q.");

        while (true) {
            out.print("\nВведіть перше число: ");
            if (!in.hasNextLine()) {
                break;                      // ввід закінчився
            }
            String first = in.nextLine().trim();
            if (first.equalsIgnoreCase("q")) {
                break;
            }

            try {
                double a = parseNumber(first);

                out.print("Введіть операцію (+, -, *, /, sqrt): ");
                String operation = in.nextLine().trim();

                double b = 0;
                if (!Calculator.isUnary(operation)) {
                    out.print("Введіть друге число: ");
                    b = parseNumber(in.nextLine());
                }

                double result = calculator.calculate(operation, a, b);
                out.println("Результат: " + format(result));

            } catch (NumberFormatException e) {
                // Ввели текст замість числа
                out.println("Помилка: потрібно ввести число. " + e.getMessage());
            } catch (ArithmeticException e) {
                // Ділення на нуль
                out.println("Помилка: " + e.getMessage());
            } catch (InvalidInputException e) {
                // Наше власне виключення
                out.println("Помилка вхідних даних: " + e.getMessage());
            } catch (NoSuchElementException e) {
                // Ввід обірвався посеред запиту
                out.println("Помилка: ввід завершився раніше, ніж очікувалось.");
                break;
            } finally {
                // Виконується завжди: і після успіху, і після помилки
                out.println("Обробку запиту завершено.");
            }
        }

        out.println("До побачення!");
    }

    /**
     * Перетворює текст у число. Приймає і кому, і крапку ("2,5" та "2.5").
     * Якщо це не число — кидає NumberFormatException.
     */
    public static double parseNumber(String text) {
        String normalized = text.trim().replace(',', '.');
        if (normalized.isEmpty()) {
            throw new NumberFormatException("порожній рядок");
        }
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("\"" + text.trim() + "\" не є числом");
        }
    }

    /** 5.0 виводимо як "5", а 2.5 — як "2.5". */
    private static String format(double value) {
        return value == Math.rint(value) && Math.abs(value) < 1e15
                ? String.valueOf((long) value)
                : String.valueOf(value);
    }
}
