package org.example.calculator;

/**
 * Власне виключення: кидається, коли вхідні дані для операції некоректні
 * (від'ємне число під коренем, NaN/нескінченність, невідома операція).
 * <p>
 * Наслідується від Exception, тому є "перевіреним" (checked):
 * компілятор змушує або обробити його в try-catch, або оголосити в throws.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}
