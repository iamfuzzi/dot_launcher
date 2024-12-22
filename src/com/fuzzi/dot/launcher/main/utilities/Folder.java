package com.fuzzi.dot.launcher.main.utilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Folder {

    // Возвращает разделитель
    public String getSeparator() {
        return File.separator;
    }

    // Возвращает папку лаунчера
    public String getLauncherFolder() {
        return System.getProperty("user.dir");
    }

    // Возвращает путь к папке .minecraft (получает из файла launcher.properties)
    public String getMinecraftFolder() {
        Config config = new Config();
        Folder folder = new Folder();
        return config.search(folder.getInitFolder() + folder.getSeparator() + "launcher.properties", "minecraftFolder");
    }

    // Возвращает путь к главной папке (та, где находятся ассеты, классы и т.д.)
    public String getMain() {
        return getLauncherFolder() + getSeparator() + "src" + getSeparator() + "com" + getSeparator() + "fuzzi" + getSeparator() + "dot" + getSeparator() + "launcher";
    }

    // Возвращает путь установки лаунчера (тот, где launcher.properties и папка .launcher)
    public String getInitFolder() {
        Path path = Paths.get(getLauncherFolder());
        return path.getParent().toString();
    }
}