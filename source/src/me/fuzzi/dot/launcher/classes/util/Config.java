package me.fuzzi.dot.launcher.classes.util;

import me.fuzzi.dot.launcher.classes.util.general.Properties;

public class Config {
    private Properties properties = new Properties();
    private Folder folder = new Folder();
    private String file = folder.getInit() + folder.getSeparator() + "launcher.properties";


    // Поиск в файле launcher.properties
    public String search(String parameter) {
        return properties.search(file, parameter);
    }
    public void write(String parameter, String value) {
        properties.write(file, parameter, value);
    }
}