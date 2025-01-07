package me.fuzzi.dot.launcher.classes.util.general;

import me.fuzzi.dot.launcher.classes.util.Lang;

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
            Lang lang = new Lang();
            System.err.println(lang.getLine("error.unexpected"));
            System.err.println(e.getMessage());
        }
    }
}