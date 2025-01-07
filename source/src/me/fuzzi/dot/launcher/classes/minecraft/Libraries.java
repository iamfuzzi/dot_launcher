package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Libraries {
    private Folder folder = new Folder();
    private String buildName;

    public String getBuildName() {
        return buildName;
    }
    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public void downloadLibraries(String build) {
        setBuildName(build);
        String jsonFilePath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + build + folder.getSeparator() + build + ".json";
        File jsonFile = new File(jsonFilePath);

        // Чтение JSON-файла
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Парсинг JSON
        JSONObject jsonObject = new JSONObject(jsonContent.toString());
        JSONArray libraries = jsonObject.getJSONArray("libraries");

        // Определение текущей операционной системы
        String currentOs = System.getProperty("os.name").toLowerCase();

        // Загрузка библиотек
        for (int i = 0; i < libraries.length(); i++) {
            JSONObject library = libraries.getJSONObject(i);
            JSONObject downloads = library.getJSONObject("downloads");

            // Попытка получить объект artifact
            JSONObject artifact;
            try {
                artifact = downloads.getJSONObject("artifact");
            } catch (JSONException e) {
                System.out.println("Пропущена библиотека: " + library.getString("name") + " (artifact не найден)");
                continue; // Пропускаем текущую библиотеку и переходим к следующей
            }

            String url = artifact.getString("url");
            String path = artifact.getString("path");

            // Проверка наличия условий операционной системы
            boolean shouldDownload = true;
            if (library.has("rules")) {
                JSONArray rules = library.getJSONArray("rules");
                shouldDownload = evaluateRules(rules, currentOs);
            }

            // Загрузка JAR-файла, если условия операционной системы выполняются
            if (shouldDownload) {
                downloadJar(url, path);
            } else {
                System.out.println("Пропущена библиотека: " + library.getString("name") + " (несовместимая ОС)");
            }
        }
    }

    private boolean evaluateRules(JSONArray rules, String currentOs) {
        boolean shouldDownload = false;

        for (int i = 0; i < rules.length(); i++) {
            JSONObject rule = rules.getJSONObject(i);
            String action = rule.getString("action");

            if (rule.has("os")) {
                JSONObject osRule = rule.getJSONObject("os");
                String osName = osRule.getString("name").toLowerCase();

                if (currentOs.contains(osName)) {
                    shouldDownload = action.equals("allow");
                } else {
                    shouldDownload = action.equals("disallow");
                }
            } else {
                shouldDownload = action.equals("allow");
            }

            if (shouldDownload) {
                break;
            }
        }

        return shouldDownload;
    }

    private void downloadJar(String jarUrl, String path) {
        // Указываем путь к директории .minecraft/libraries
        String librariesDir = folder.getMinecraft() + folder.getSeparator() + "libraries" + folder.getSeparator() + getBuildName();
        File dir = new File(librariesDir, path.substring(0, path.lastIndexOf("/")));

        // Создаем директорию, если она не существует
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Полный путь к файлу
        File outputFile = new File(dir, path.substring(path.lastIndexOf("/") + 1));

        try {
            URL url = new URL(jarUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            // Проверка ответа
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = connection.getInputStream();
                     OutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Скачан: " + outputFile.getAbsolutePath());
                }
            } else {
                System.out.println("Не удалось скачать: " + jarUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}