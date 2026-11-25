package com.example.automaton;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestWordAutomatonTest {

    private final TestWordAutomaton automaton = new TestWordAutomaton();

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            // приклади із завдання
            "abcTESTabc, F",
            "abcTES,     3",
            // слово знайдено
            "TEST,       F",
            "TESTabc,    F",
            "abcTEST,    F",
            "TESTTEST,   F",
            "TESTxyz,    F",
            // кожен кінцевий стан, коли слово не дописане
            "'',         S",   // порожній рядок
            "abc,        S",
            "T,          1",
            "TE,         2",
            "TES,        3",
            "abcT,       1",
            "abcTE,      2",
            // ланцюжок обривається на кожному кроці — повернення в S
            "Tx,         S",
            "TEx,        S",
            "TESx,       S",
            "TEXx,       S",
            "TEXT,       1",   // остання T — початок нового слова
            // регістр має значення
            "test,       S",
            "TeSx,       S",
    })
    void recognizesWordTest(String input, String expectedState) {
        assertEquals(expectedState, automaton.run(input).toString());
    }

    // Завдання 3*: рядки зі словом "TEST", на яких автомат з діаграми не доходить до F.
    // Причина: при помилці в станах 1 і 2 автомат іде в S і "забуває" символ T,
    // хоча саме ця T могла бути початком нового слова TEST.
    @ParameterizedTest(name = "\"{0}\" -> F")
    @CsvSource({
            "TTEST",      // 1 --T--> S, а треба 1
            "TETEST",     // 2 --T--> S, а треба 1
            "TTTTEST",
            "TETTTEST",
            "abcTTESTabc",
    })
    void findsTestAfterBrokenPrefix(String input) {
        assertEquals(State.F, automaton.run(input));
    }
}
