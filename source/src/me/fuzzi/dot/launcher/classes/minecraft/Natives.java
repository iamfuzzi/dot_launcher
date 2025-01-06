package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Natives {

    public void download(String versionName) {
        try {
            // Загрузка JSON файла для указанной версии
            Version version = new Version();
            String jsonUrl = version.getJson(versionName);
            JSONObject versionJson = downloadJson(jsonUrl);

            // Получение массива библиотек
            JSONArray libraries = versionJson.getJSONArray("libraries");
            System.out.println("Found " + libraries.length() + " libraries.");

            String osName = getOSName();
            String nativesKey = "natives-" + osName;
            System.out.println("Looking for natives key: " + nativesKey);

            boolean foundNatives = false;

            for (int i = 0; i < libraries.length(); i++) {
                JSONObject library = libraries.getJSONObject(i);
                if (library.has("downloads")) {
                    JSONObject downloads = library.getJSONObject("downloads");
                    if (downloads.has("classifiers")) {
                        JSONObject classifiers = downloads.getJSONObject("classifiers");
                        if (classifiers.has(nativesKey)) {
                            foundNatives = true;
                            JSONObject nativeFile = classifiers.getJSONObject(nativesKey);
                            String nativeUrl = nativeFile.getString("url");
                            System.out.println("Downloading native file from: " + nativeUrl);

                            // Скачивание и извлечение нативных файлов
                            downloadAndExtractNativeFiles(nativeUrl, versionName);
                        } else {
                            System.out.println("No native file found for " + nativesKey + " in library " + library.getString("name"));
                        }
                    } else {
                        System.out.println("No classifiers found in downloads for library " + library.getString("name"));
                    }
                } else {
                    System.out.println("No downloads found for library " + library.getString("name"));
                }
            }

            if (!foundNatives) {
                System.out.println("No native files found for the current OS.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject downloadJson(String jsonUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(jsonUrl).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonString = jsonBuilder.toString();
            System.out.println("Received JSON: " + jsonString); // Выводим полученный JSON
            return new JSONObject(jsonString);
        }
    }

    private String getOSName() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("mac")) {
            return "osx";
        } else if (os.contains("nix") || os.contains("nux")) {
            return "linux";
        }
        return "unknown";
    }

    private void downloadAndExtractNativeFiles(String fileUrl, String versionName) throws IOException {
        // Путь к папке natives
        Folder folder = new Folder();
        String nativesPath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + "natives";

        // Создание папки natives, если она не существует
        Files.createDirectories(Paths.get(nativesPath));

        // Скачивание ZIP файла и извлечение файлов из него
        try (InputStream in = new URL(fileUrl).openStream();
             ZipInputStream zis = new ZipInputStream(in)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && (entry.getName().endsWith(".dylib") || entry.getName().endsWith(".so") || entry.getName().endsWith(".dll"))) {
                    File outputFile = new File(nativesPath, entry.getName());
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    System.out.println ("Extracted: " + outputFile.getAbsolutePath());
                }
                zis.closeEntry();
            }
        }
    }
}