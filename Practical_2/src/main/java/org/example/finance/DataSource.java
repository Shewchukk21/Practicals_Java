package org.example.finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Задача 1.2*: джерело даних, відокремлене від їх обробки.
 * Джерело лише віддає рядки тексту — звідки завгодно (URL, файл, пам'ять...).
 * Розбором рядків займається TransactionCSVReader, тому нове джерело
 * не вимагає змін у логіці обробки.
 */
@FunctionalInterface
public interface DataSource {

    List<String> readLines() throws IOException;

    /** Дані з інтернету за посиланням. */
    static DataSource fromUrl(String url) {
        return () -> {
            try (InputStream in = URI.create(url).toURL().openStream()) {
                return readAll(in);
            }
        };
    }

    /** Дані з локального файлу. */
    static DataSource fromFile(String path) {
        return () -> Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
    }

    /** Дані з ресурсу в classpath (наприклад, src/test/resources). */
    static DataSource fromResource(String name) {
        return () -> {
            InputStream in = DataSource.class.getClassLoader().getResourceAsStream(name);
            if (in == null) {
                throw new IOException("Ресурс не знайдено: " + name);
            }
            try (in) {
                return readAll(in);
            }
        };
    }

    /** Дані з готового списку рядків (зручно для тестів). */
    static DataSource fromLines(List<String> lines) {
        return () -> lines;
    }

    /** Вибирає джерело автоматично: http(s) — URL, інакше — файл. */
    static DataSource of(String location) {
        return location.startsWith("http://") || location.startsWith("https://")
                ? fromUrl(location)
                : fromFile(location);
    }

    private static List<String> readAll(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return br.lines().toList();
        }
    }
}
