package me.fuzzi.dot.launcher.classes.util.general;

import me.fuzzi.dot.launcher.classes.util.Lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Download {

    // Скачать файл из ссылки
    public void fromUrl(String fileURL, String saveDir, String fileName) {
        try {
            URL url = new URL(fileURL);

            try (InputStream in = url.openStream()) {

                // Если папки сохранения не существует
                File save = new File(saveDir);
                if (!save.exists()) {
                    save.mkdirs();
                }

                Path outputPath = Path.of(saveDir, fileName);
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            Lang lang = new Lang();
            System.err.println(lang.getLine("error.unexpected"));
            System.err.println(e.getMessage());
        }
    }
}