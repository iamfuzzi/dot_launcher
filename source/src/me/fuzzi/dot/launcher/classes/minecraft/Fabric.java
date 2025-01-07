package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class Fabric {
    public void download(String fid, String name) throws IOException {
        JSON json = new JSON();
        Text text = new Text();
        Version version = new Version();

        // Получаем ID версии
        String id = json.getValue(text.fromUrl(version.getJson(name)), "id");

        // Новый URL для получения JSON
        String jsonUrl = "https://meta.fabricmc.net/v2/versions/loader/" + id + "/" + fid;

        // Получаем JSON-данные
        JSONObject jsonObject = fetchJsonObject(jsonUrl);
        JSONObject loader = jsonObject.getJSONObject("loader");
        String loaderVersion = loader.getString("version");

        // Проверяем, соответствует ли версия
        if (loaderVersion.equals(fid)) {
            JSONObject launcherMeta = jsonObject.getJSONObject("launcherMeta");
            String mainClass = launcherMeta.getJSONObject("mainClass").getString("client");
            JSONArray commonLibraries = launcherMeta.getJSONObject("libraries").getJSONArray("common");

            // Создаем новый JSON объект для обновления
            JSONObject newJson = new JSONObject();
            newJson.put("mainClass", mainClass);
            newJson.put("fabric", loaderVersion);

            // Добавляем библиотеки
            JSONArray librariesArray = new JSONArray();
            for (Object libObj : commonLibraries) {
                JSONObject lib = (JSONObject) libObj;
                String nameLib = lib.getString("name");
                String[] nameParts = nameLib.split(":");

                // Формируем путь к библиотеке
                String path = String.join("/", nameParts[0].replace(".", "/"), nameParts[1], nameParts[2], nameParts[1] + "-" + nameParts[2] + ".jar");

                JSONObject libraryJson = new JSONObject();
                JSONObject downloads = new JSONObject();
                JSONObject artifact = new JSONObject();

                artifact.put("path", path);
                artifact.put("sha1", lib.getString("sha1"));
                artifact.put("size", lib.getInt("size"));
                artifact.put("url", lib.getString("url") + path);
                downloads.put("artifact", artifact);
                libraryJson.put("downloads", downloads);
                libraryJson.put("name", nameLib);

                librariesArray.put(libraryJson);
            }
            newJson.put("libraries", librariesArray);

            // Записываем новый JSON в файл
            writeJsonToFile(name, newJson);
        }
    }

    private JSONObject fetchJsonObject(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to fetch JSON: " + connection.getResponseMessage());
        }

        StringBuilder jsonResponse = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
        }

        return new JSONObject(jsonResponse.toString());
    }

    private void writeJsonToFile(String name, JSONObject newJson) throws IOException {
        Folder folder = new Folder();
        String filePath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + name + folder.getSeparator() + name + ".json";
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Создаем директорию, если она не существует

        JSONObject existingJson;
        if (file.exists()) {
            // Читаем существующий JSON
            String existingJsonString = new String(Files.readAllBytes(file.toPath()));
            existingJson = new JSONObject(existingJsonString);
        } else {
            existingJson = new JSONObject();
        }

        // Обновляем mainClass, если он есть в новом JSON
        if (newJson.has("mainClass")) {
            existingJson.put("mainClass", newJson.getString("mainClass"));
        }

        // Обновляем fabric, если он есть в новом JSON
        if (newJson.has("fabric")) {
            existingJson.put("fabric", newJson.getString("fabric"));
        }

        // Об новляем библиотеки
        JSONArray existingLibraries = existingJson.optJSONArray("libraries");
        if (existingLibraries == null) {
            existingLibraries = new JSONArray();
        }

        // Добавляем новые библиотеки, если их нет в существующих
        for (Object libObj : newJson.getJSONArray("libraries")) {
            JSONObject newLib = (JSONObject) libObj;
            String newLibName = newLib.getString("name");
            boolean exists = false;

            // Проверяем, существует ли библиотека
            for (Object existingLibObj : existingLibraries) {
                JSONObject existingLib = (JSONObject) existingLibObj;
                if (existingLib.getString("name").equals(newLibName)) {
                    exists = true;
                    break;
                }
            }

            // Если библиотеки нет, добавляем ее
            if (!exists) {
                existingLibraries.put(newLib);
            }
        }

        existingJson.put("libraries", existingLibraries);

        // Записываем обновленный JSON в файл
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(existingJson.toString(4)); // Записываем с отступами
        }
    }
}