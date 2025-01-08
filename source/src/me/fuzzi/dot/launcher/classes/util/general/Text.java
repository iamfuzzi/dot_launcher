package me.fuzzi.dot.launcher.classes.util.general;

import me.fuzzi.dot.launcher.classes.util.Lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Text {
    Lang lang = new Lang();

    // Создание временного текста из ссылки
    public String fromUrl(String urlString) {
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString); // URL-объект из ссылки

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                // Чтение содержимого
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } else {
                System.out.println(lang.getLine("error.unexpected"));
                System.out.println(status);
            }
        } catch (IOException e) {
            System.out.println(lang.getLine("error.unexpected"));
            e.printStackTrace();

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                System.out.println(lang.getLine("error.unexpected"));
                e.printStackTrace();
            }
        }
        return content.toString(); // Возрващаем текст
    }



    public String fromFile(String file) {
        StringBuilder content = new StringBuilder();

        try {
            // Чтение содержимого файла
            Files.lines(Paths.get(file)).forEach(line -> content.append(line).append("\n"));
        } catch (IOException e) {
            System.out.println(lang.getLine("error.unexpected"));
            e.printStackTrace();
        }

        return content.toString(); // Возвращаем текст
    }
}