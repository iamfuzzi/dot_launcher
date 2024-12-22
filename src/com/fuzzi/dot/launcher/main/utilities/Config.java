package com.fuzzi.dot.launcher.main.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    // Выполняет поиск в фале параметров лаунчера. Получает название, возвращает значение
    public String search(String file, String parameter) {
        String line;
        String value = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(parameter + " = ")) {
                    value = line.substring(line.indexOf('=') + 1).trim().replaceAll("^\"|\"$", "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}