package me.fuzzi.dot.launcher.classes.util.general;

import me.fuzzi.dot.launcher.classes.util.Lang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    public void unzip(String zipFilePath, String destDir) {
        Path destDirPath = Path.of(destDir); // Папка распаковки

        // Если папка не существует
        if (!Files.exists(destDirPath)) {
            try {
                Files.createDirectories(destDirPath);
            } catch (IOException e) {
                Lang lang = new Lang();
                System.err.println(lang.getLine("error.unexpected"));
                System.err.println(e.getMessage());
                return;
            }
        }

        // Открываем ZIP-файл и разархивируем его
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Path.of(zipFilePath)))) {
            ZipEntry entry;

            while ((entry = zipIn.getNextEntry()) != null) { // Пока следующий файл существует, разархивируем
                Path filePath = destDirPath.resolve(entry.getName());

                // Если папка - создаем папку
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    // Если файл - копируем
                    try {
                        Files.createDirectories(filePath.getParent()); // Создаем папку, если ее нет
                        Files.copy(zipIn, filePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        Lang lang = new Lang();
                        System.err.println(lang.getLine("error.unexpected"));
                        System.err.println(e.getMessage());
                    }
                }
                zipIn.closeEntry();
            }
        } catch (IOException e) {
            Lang lang = new Lang();
            System.err.println(lang.getLine("error.unexpected"));
            System.err.println(e.getMessage());
        }
    }
}