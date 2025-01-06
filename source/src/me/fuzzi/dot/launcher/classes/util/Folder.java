package me.fuzzi.dot.launcher.classes.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Folder {

    // Получает разделитель
    public String getSeparator() {
        return File.separator;
    }

    // Получает основную папку проекта (.launcher, launcher.properties)
    public String getInit() {
        String dir =  System.getProperty("user.dir");
        Path path = Paths.get(dir);
        return path.getParent().toString();
    }

    // Получает папку майнкрафта из конфига
    public String getMinecraft() {
        Config config = new Config();
        return config.search("minecraftFolder");
    }

    // Получает основную рабочую папку (classes, textures, libraries)
    public String getMain() {
        return getInit() + getSeparator() + "source" + getSeparator() + "src" + getSeparator() + "me" + getSeparator() + "fuzzi" + getSeparator() + "dot" + getSeparator() + "launcher";
    }
}
