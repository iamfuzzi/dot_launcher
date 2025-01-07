package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Assets {
    Folder folder = new Folder();
    private String jsonFilePath;
    private String assetsDir = folder.getMinecraft() + folder.getSeparator() + "assets" + folder.getSeparator() + "objects" + folder.getSeparator();

    public void download(String name) {
        jsonFilePath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + name + folder.getSeparator() + name + ".json";
        try {
            // Чтение JSON файла
            String jsonContent = readFile(jsonFilePath);
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Получение URL assetIndex
            JSONObject assetIndex = jsonObject.getJSONObject("assetIndex");
            String assetIndexUrl = assetIndex.getString("url");

            // Загрузка assetIndex
            JSONObject assetIndexJson = new JSONObject(readUrl(assetIndexUrl));
            JSONObject objects = assetIndexJson.getJSONObject("objects");

            // Обработка каждого объекта
            for (String key : objects.keySet()) {
                JSONObject assetObject = objects.getJSONObject(key);
                String hash = assetObject.getString("hash");

                // Формирование URL для скачивания
                String downloadUrl = "https://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash;

                // Сохранение файла
                saveFile(downloadUrl, assetsDir + hash.substring(0, 2) + "/" + hash);
            }

            System.out.println("Все файлы успешно загружены.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private String readUrl(String urlString) throws IOException {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private void saveFile(String fileUrl, String savePath) throws IOException {
        // Создание директорий, если они не существуют
        new File(savePath).getParentFile().mkdirs();

        // Скачивание файла
        try (InputStream in = new URL(fileUrl).openStream();
             OutputStream out = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}