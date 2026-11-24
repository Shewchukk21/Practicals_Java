package org.example.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class MainTest {

    /** Запускає діалог з заданим "введенням користувача" і повертає все, що програма вивела. */
    private static String runWithInput(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        Main.run(new Scanner(input), out);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    // ---------- Невірні вхідні дані: рядки замість чисел ----------

    @Test
    void testParseNumberWithTextThrowsNumberFormatException() {
        NumberFormatException e = Assertions.assertThrows(NumberFormatException.class,
                () -> Main.parseNumber("abc"));
        Assertions.assertTrue(e.getMessage().contains("abc"));
    }

    @Test
    void testParseNumberWithEmptyStringThrowsNumberFormatException() {
        Assertions.assertThrows(NumberFormatException.class, () -> Main.parseNumber("   "));
    }

    @Test
    void testParseNumberAcceptsDotAndComma() {
        Assertions.assertEquals(2.5, Main.parseNumber("2.5"));
        Assertions.assertEquals(2.5, Main.parseNumber(" 2,5 "));
        Assertions.assertEquals(-7.0, Main.parseNumber("-7"));
    }

    // ---------- Повний діалог з користувачем ----------

    @Test
    void testSuccessfulCalculation() {
        String output = runWithInput("6\n*\n7\nq\n");
        Assertions.assertTrue(output.contains("Результат: 42"), output);
        Assertions.assertTrue(output.contains("Обробку запиту завершено."), "finally має виконатись");
    }

    @Test
    void testTextInsteadOfNumberInDialog() {
        String output = runWithInput("abc\nq\n");
        Assertions.assertTrue(output.contains("потрібно ввести число"), output);
        Assertions.assertTrue(output.contains("Обробку запиту завершено."), "finally має виконатись і при помилці");
    }

    @Test
    void testDivisionByZeroInDialog() {
        String output = runWithInput("5\n/\n0\nq\n");
        Assertions.assertTrue(output.contains("Ділення на нуль неможливе"), output);
        Assertions.assertTrue(output.contains("Обробку запиту завершено."));
    }

    @Test
    void testNegativeSqrtInDialog() {
        String output = runWithInput("-9\nsqrt\nq\n");
        Assertions.assertTrue(output.contains("Помилка вхідних даних"), output);
        Assertions.assertFalse(output.contains("Введіть друге число"), "Для sqrt друге число не запитується");
    }

    @Test
    void testUnknownOperationInDialog() {
        String output = runWithInput("1\n^\n2\nq\n");
        Assertions.assertTrue(output.contains("Невідома операція"), output);
    }

    @Test
    void testProgramContinuesAfterError() {
        // Спочатку помилка, потім коректний запит — програма не повинна "впасти"
        String output = runWithInput("5\n/\n0\n10\n-\n4\nq\n");
        Assertions.assertTrue(output.contains("Ділення на нуль"), output);
        Assertions.assertTrue(output.contains("Результат: 6"), output);
    }

    @Test
    void testInputEndsInTheMiddleOfRequest() {
        String output = runWithInput("5\n");
        Assertions.assertTrue(output.contains("ввід завершився"), output);
        Assertions.assertTrue(output.contains("До побачення!"));
    }
}
