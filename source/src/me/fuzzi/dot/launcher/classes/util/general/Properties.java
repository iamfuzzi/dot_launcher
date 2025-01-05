package me.fuzzi.dot.launcher.classes.util.general;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Properties {

    // Поиск в файле типа .properties вида "параметр = значение"
    public String search(String file, String parameter) {
        String line;
        String value = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) { // Пока строка не пустая
                line = line.trim();
                if (line.startsWith(parameter + " = ")) { // Если строка имеет вид "Параметр = ", то
                    value = line.substring(line.indexOf('=') + 1).trim().replaceAll("^\"|\"$", ""); // Вывод значения параметра
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    // Изменяет текущее значение value параметра parameter (если его нет - создает)
    public void write(String file, String parameter, String value) {
        List<String> lines = new ArrayList<>();
        boolean parameterFound = false;

        // Чтение файла и обновление параметра
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(parameter + " = ")) {
                    // Если параметр найден, обновляем его значение
                    line = parameter + " = " + value;
                    parameterFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Если параметр не найден, добавляем его в конец
        if (!parameterFound) {
            lines.add(parameter + " = " + value);
        }

        // Запись обновленных строк обратно в файл
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
