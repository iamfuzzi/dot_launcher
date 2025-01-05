package me.fuzzi.dot.launcher.classes.util.general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Delete {

    // Удаление файла
    public void delete(String file) {
        try {
            Path path = Path.of(file);
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}