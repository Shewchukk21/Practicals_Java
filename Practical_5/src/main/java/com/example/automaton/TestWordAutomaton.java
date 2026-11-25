package com.example.automaton;

// Скінченний автомат, що шукає в рядку слово "TEST"
public class TestWordAutomaton {

    public State run(String input) {
        State state = State.S;
        for (char c : input.toCharArray()) {
            state = next(state, c);
        }
        return state;
    }

    // Переходи як на діаграмі, з одним виправленням (завдання 3*):
    // на діаграмі в станах 1 і 2 будь-який "неправильний" символ веде в S.
    // Але якщо цей символ — 'T', то він може бути початком нового слова TEST,
    // тому з нього треба йти в стан 1, а не в S. Інакше "TTEST" або "TETEST" не дадуть F.
    // (У стані 3 символ 'T' і так веде в F, тож там виправлення не потрібне.)
    State next(State state, char c) {
        return switch (state) {
            case S -> c == 'T' ? State.ONE : State.S;
            case ONE -> c == 'E' ? State.TWO : restart(c);
            case TWO -> c == 'S' ? State.THREE : restart(c);
            case THREE -> c == 'T' ? State.F : State.S;
            case F -> State.F;
        };
    }

    private State restart(char c) {
        return c == 'T' ? State.ONE : State.S;
    }
}
