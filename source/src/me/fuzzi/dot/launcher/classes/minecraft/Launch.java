package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Properties;
import me.fuzzi.dot.launcher.classes.util.general.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launch {
    Folder folder = new Folder();

    private final String versionName; // Название версии (например, "Тестовая сборка")
    private final String javaPath; // Путь к Java (например, "java" или полный путь к java.exe)
    private final String playerName; // Имя игрока
    private final String accessToken; // Токен для авторизации
    private final String assetsDir = folder.getMinecraft() + folder.getSeparator() + "assets";

    public Launch(String versionName, String javaPath, String playerName, String accessToken) {
        this.versionName = versionName;
        this.javaPath = javaPath;
        this.playerName = playerName;
        this.accessToken = accessToken;
    }

    public void launch() throws IOException {
        // Путь к папке с пользовательскими данными (например, .minecraft/home/Тестовая сборка)
        String userDir = folder.getMinecraft() + folder.getSeparator() + "home" + folder.getSeparator() + versionName;

        JSON json = new JSON();
        Text text = new Text();
        String assetIndex = json.getValue(text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + versionName + ".json"), "assets");

        File userDirFile = new File(userDir);
        if (!userDirFile.exists() && !userDirFile.mkdirs()) {
            throw new IOException("Failed to create directory: " + userDir);
        }

        // Путь к файлу версии (например, .minecraft/versions/Тестовая сборка/Тестовая сборка.jar)
        String versionJarPath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + versionName + ".jar";

        // Читаем библиотеки
        String librariesPath = folder.getMinecraft() + folder.getSeparator() + "libraries";
        List<String> libraries = getLibraries(new File(librariesPath));

        // Формируем classpath (все библиотеки + jar-файл версии)
        StringBuilder classpath = new StringBuilder();
        for (String lib : libraries) {
            classpath.append(lib).append(File.pathSeparator);
        }
        classpath.append(versionJarPath);

        // Формируем аргументы JVM
        List<String> jvmArgs = new ArrayList<>();
        jvmArgs.add(javaPath);
        jvmArgs.add("-Xmx1G"); // Ограничение памяти (1 ГБ)
        jvmArgs.add("-Xms256M"); // Начальный объём памяти (256 МБ)
        jvmArgs.add("-Dminecraft.home=" + userDir); // Устанавливаем переменную окружения для Minecraft
        jvmArgs.add("-cp");
        jvmArgs.add(classpath.toString());
        jvmArgs.add("net.minecraft.client.main.Main"); // Главный класс Minecraft

        // Формируем игровые аргументы
        List<String> gameArgs = new ArrayList<>();
        gameArgs.add("--username");
        gameArgs.add(playerName);
        gameArgs.add("--version");
        gameArgs.add(versionName);
        gameArgs.add("--gameDir");
        gameArgs.add(userDir);
        gameArgs.add("--assetsDir");
        gameArgs.add(assetsDir);
        gameArgs.add("--assetIndex");
        gameArgs.add(assetIndex);
        gameArgs.add("--accessToken");
        gameArgs.add(accessToken);

        // Объединяем все аргументы
        List<String> processArgs = new ArrayList<>();
        processArgs.addAll(jvmArgs);
        processArgs.addAll(gameArgs);

        // Запускаем процесс
        ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
        processBuilder.directory(userDirFile); // Устанавливаем рабочую директорию
        processBuilder.redirectErrorStream(true); // Объединяем stdout и stderr

        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out); // Выводим логи игры в консоль
    }

    // Рекурсивно собираем все библиотеки из папки libraries
    private List<String> getLibraries(File librariesDir) {
        List<String> libraries = new ArrayList<>();
        if (librariesDir.exists() && librariesDir.isDirectory()) {
            for (File file : librariesDir.listFiles()) {
                if (file.isDirectory()) {
                    libraries.addAll(getLibraries(file));
                } else if (file.getName().endsWith(".jar")) {
                    libraries.add(file.getAbsolutePath());
                }
            }
        }
        return libraries;
    }
}