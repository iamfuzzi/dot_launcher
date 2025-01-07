package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.Lang;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launch {
    Folder folder = new Folder();
    Lang lang = new Lang();

    // Декларация нестатичных переменных
    private final String versionName; // Название версии (например, "Тестовая сборка")
    private final String playerName; // Имя игрока
    private final String accessToken; // Токен для авторизации
    private final int minMem; // Минимальное кол-во памяти в Мб
    private final int maxMem; // Максимальное кол-во памяти в Мб

    // Констурктор для обозначения нестатичных переменных
    public Launch(String versionName, String playerName, String accessToken, int minMem, int maxMem) {
        this.versionName = versionName;
        this.playerName = playerName;
        this.accessToken = accessToken;
        this.minMem = minMem;
        this.maxMem = maxMem;
    }

    // Основной метод запуска игры
    public void launch() throws IOException {

        // Создание статичных переменных или переменных, которые содержат нестатичные переменные

        // Путь к папке home
        String homeDir = folder.getMinecraft() + folder.getSeparator() + "home" + folder.getSeparator() + versionName;

        // Путь к файлу версии (например, .minecraft/versions/Тестовая сборка/Тестовая сборка.jar)
        String jarPath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + versionName + ".jar";

        // Путь к папке ассетов
        String assetsDir = folder.getMinecraft() + folder.getSeparator() + "assets";

        // Путь к папке с нативами
        String nativesDir = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + "natives";

        // Путь к Java
        JDK jdk = new JDK();
        String javaPath = jdk.getJdk();

        JSON json = new JSON();
        Text text = new Text();

        // Номер ассет индекса
        String assetIndex = json.getValue(text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + versionName + ".json"), "assets");

        // Мэйн класс
        String mainClass = json.getValue(text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + versionName + folder.getSeparator() + versionName + ".json"), "mainClass");

        // Создание рабочей папки, если ее не существует
        File userDirFile = new File(homeDir);
        if (!userDirFile.exists() && !userDirFile.mkdirs()) {
            throw new IOException(lang.getLine("error.dir.create") + ": " + homeDir);
        }

        // Читаем библиотеки
        String librariesPath = folder.getMinecraft() + folder.getSeparator() + "libraries" + folder.getSeparator() + versionName;
        List<String> libraries = getLibraries(new File(librariesPath));

        // Формируем classpath (все библиотеки + jar-файл версии)
        StringBuilder classpath = new StringBuilder();
        for (String lib : libraries) {
            classpath.append(lib).append(File.pathSeparator);
        }
        classpath.append(jarPath);

        // Аргументы JVM
        List<String> jvmArgs = new ArrayList<>();
        jvmArgs.add(javaPath);
        //jvmArgs.add("-Xms" + minMem + "M"); // Минимальное кол-во памяти
        //jvmArgs.add("-Xmx" + maxMem + "M"); // Максимальное кол-во памяти
        jvmArgs.add("-Xmx2G");
        jvmArgs.add("-Dminecraft.home=" + homeDir); // Рабочая папка
        jvmArgs.add("-Djava.library.path=" + nativesDir); // Путь до нативов
        //jvmArgs.add("-DFabricMcEmu= net.minecraft.client.main.Main");
        jvmArgs.add("-cp");
        jvmArgs.add(classpath.toString()); // Пути до всех библиотек и до исполняемого .jar файла
        jvmArgs.add(mainClass); // Главный класс Minecraft

        // Игровые аргументы
        List<String> gameArgs = new ArrayList<>();

        gameArgs.add("--username"); // Никнейм
        gameArgs.add(playerName);
        gameArgs.add("--version"); // Название версии
        gameArgs.add(versionName);
        gameArgs.add("--gameDir"); // Рабочая папка
        gameArgs.add(homeDir);
        gameArgs.add("--assetsDir"); // Папка ассетов
        gameArgs.add(assetsDir);
        gameArgs.add("--assetIndex"); // Индекс ассетов
        gameArgs.add(assetIndex);
        gameArgs.add("--accessToken"); // Токен доступа
        gameArgs.add(accessToken);

        /*

        Приколы

        gameArgs.add("--demo"); // Демо

        Для лицензии

        gameArgs.add("--uuid"); // UUID
        gameArgs.add("--cilentId"); // ID клиента
        gameArgs.add("--xuid"); // Не знаю
        gameArgs.add("--userType"); // Тип аккаунта
        gameArgs.add("--versionType"); // Не знаю

         */

        // Все рагументы в одну строку
        List<String> processArgs = new ArrayList<>();
        processArgs.addAll(jvmArgs);
        processArgs.addAll(gameArgs);

        // Запуск процесса
        ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
        processBuilder.directory(userDirFile); // Установка рабочей папки
        processBuilder.redirectErrorStream(true); // Показ ошибок в консоли

        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out); // Вывод логов в консоль
    }

    // Собираем все библиотеки из папки libraries
    private List<String> getLibraries(File librariesDir) {
        List<String> libraries = new ArrayList<>(); // Создаем массив

        if (librariesDir.exists() && librariesDir.isDirectory()) { // Если существует папка библиотек

            for (File file : librariesDir.listFiles()) { // Для каждого файла

                if (file.isDirectory()) { // Если не файл, а папка, то
                    libraries.addAll(getLibraries(file)); // Проделать дальше

                } else if (file.getName().endsWith(".jar")) { // Если файл окначивается на .jar, то
                    libraries.add(file.getAbsolutePath()); // Добавить полный путь до файла
                }
            }
        }
        return libraries;
    }
}