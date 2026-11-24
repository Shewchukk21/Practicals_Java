package org.example.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    // ---------- Звичайні операції ----------

    @Test
    void testAdd() throws InvalidInputException {
        Assertions.assertEquals(5.0, calculator.add(2, 3));
        Assertions.assertEquals(-1.5, calculator.add(-3, 1.5));
    }

    @Test
    void testSubtract() throws InvalidInputException {
        Assertions.assertEquals(-1.0, calculator.subtract(2, 3));
    }

    @Test
    void testMultiply() throws InvalidInputException {
        Assertions.assertEquals(-7.5, calculator.multiply(2.5, -3));
    }

    @Test
    void testDivide() throws InvalidInputException {
        Assertions.assertEquals(2.5, calculator.divide(5, 2));
    }

    @Test
    void testSqrt() throws InvalidInputException {
        Assertions.assertEquals(3.0, calculator.sqrt(9));
        Assertions.assertEquals(0.0, calculator.sqrt(0));
    }

    @Test
    void testCalculateBySymbol() throws InvalidInputException {
        Assertions.assertEquals(10.0, calculator.calculate("+", 4, 6));
        Assertions.assertEquals(-2.0, calculator.calculate("-", 4, 6));
        Assertions.assertEquals(24.0, calculator.calculate("*", 4, 6));
        Assertions.assertEquals(4.0, calculator.calculate("/", 8, 2));
        Assertions.assertEquals(4.0, calculator.calculate("sqrt", 16, 0));
    }

    // ---------- Ділення на нуль ----------

    @Test
    void testDivideByZeroThrowsArithmeticException() {
        ArithmeticException e = Assertions.assertThrows(ArithmeticException.class,
                () -> calculator.divide(10, 0));
        Assertions.assertEquals("Ділення на нуль неможливе", e.getMessage());
    }

    @Test
    void testDivideZeroByZeroThrowsArithmeticException() {
        Assertions.assertThrows(ArithmeticException.class, () -> calculator.divide(0, 0));
    }

    @Test
    void testDivideByZeroThroughCalculate() {
        Assertions.assertThrows(ArithmeticException.class, () -> calculator.calculate("/", 1, 0));
    }

    // ---------- Власне виключення InvalidInputException ----------

    @Test
    void testSqrtOfNegativeThrowsInvalidInputException() {
        InvalidInputException e = Assertions.assertThrows(InvalidInputException.class,
                () -> calculator.sqrt(-4));
        Assertions.assertTrue(e.getMessage().contains("від'ємного"));
    }

    @Test
    void testUnknownOperationThrowsInvalidInputException() {
        Assertions.assertThrows(InvalidInputException.class, () -> calculator.calculate("%", 1, 2));
    }

    @Test
    void testNaNInputThrowsInvalidInputException() {
        Assertions.assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 1));
        Assertions.assertThrows(InvalidInputException.class, () -> calculator.multiply(2, Double.POSITIVE_INFINITY));
        Assertions.assertThrows(InvalidInputException.class, () -> calculator.sqrt(Double.NaN));
    }

    @Test
    void testInvalidInputIsCheckedBeforeDivisionByZero() {
        // Спочатку перевіряються самі числа, тому тут InvalidInputException, а не ArithmeticException
        Assertions.assertThrows(InvalidInputException.class, () -> calculator.divide(Double.NaN, 0));
    }
}
