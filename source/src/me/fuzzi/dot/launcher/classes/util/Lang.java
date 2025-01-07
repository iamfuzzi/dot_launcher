package me.fuzzi.dot.launcher.classes.util;

import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

public class Lang {
    private String lang;
    public Lang() {
        Config config = new Config();
        lang = config.search("lang");
    }
    public String getLine(String line) {
        Text text = new Text();
        JSON json = new JSON();
        Folder folder = new Folder();
        return json.getValue(text.fromFile(folder.getMain() + folder.getSeparator() + "lang" + folder.getSeparator() + lang + ".json"), line);
    }
}