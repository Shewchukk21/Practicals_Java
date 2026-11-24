package org.example.calculator;

/**
 * Калькулятор з базовими арифметичними операціями.
 * Кожен метод перевіряє вхідні значення і кидає виключення, якщо вони некоректні.
 */
public class Calculator {

    /** Додавання. */
    public double add(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a + b;
    }

    /** Віднімання. */
    public double subtract(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a - b;
    }

    /** Множення. */
    public double multiply(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a * b;
    }

    /**
     * Ділення.
     * Для double Java сама не кидає виключення при діленні на нуль
     * (5.0 / 0 дає Infinity), тому кидаємо ArithmeticException вручну.
     */
    public double divide(double a, double b) throws InvalidInputException {
        validate(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе");
        }
        return a / b;
    }

    /** Квадратний корінь: число не може бути від'ємним. */
    public double sqrt(double a) throws InvalidInputException {
        validate(a);
        if (a < 0) {
            throw new InvalidInputException("Неможливо обчислити квадратний корінь з від'ємного числа: " + a);
        }
        return Math.sqrt(a);
    }

    /**
     * Виконує операцію за її символом: "+", "-", "*", "/" або "sqrt".
     * Для "sqrt" використовується лише перше число.
     */
    public double calculate(String operation, double a, double b) throws InvalidInputException {
        return switch (operation) {
            case "+" -> add(a, b);
            case "-" -> subtract(a, b);
            case "*" -> multiply(a, b);
            case "/" -> divide(a, b);
            case "sqrt" -> sqrt(a);
            default -> throw new InvalidInputException("Невідома операція: \"" + operation + "\"");
        };
    }

    /** Чи потрібне операції друге число. */
    public static boolean isUnary(String operation) {
        return "sqrt".equals(operation);
    }

    /** Перевірка, що всі числа скінченні (не NaN і не нескінченність). */
    private static void validate(double... values) throws InvalidInputException {
        for (double value : values) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new InvalidInputException("Некоректне число: " + value);
            }
        }
    }
}
