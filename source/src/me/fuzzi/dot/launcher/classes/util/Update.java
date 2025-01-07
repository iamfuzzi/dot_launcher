package me.fuzzi.dot.launcher.classes.util;

import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

public class Update {
    public Update() {
        Config config = new Config();
        if (config.search("disableUpdates").equals("true")) {
            return;
        } else {
            JSON json = new JSON();
            Text text = new Text();
            String url = "https://raw.githubusercontent.com/iamfuzzi/dot_launcher/refs/heads/main/version_manifest.json";

            int[] dbFragments = new int[4]; // Максимум 4 элемента
            String[] db = json.getValue(text.fromUrl(url), "version").split("\\."); // Разделяем строку по точкам

            for (int i = 0; i < 4; i++) {
                if (i >= 4) break;
                if (db[i].equals("a")) {
                    dbFragments[i] = 0; // Замена 'a' на 0
                } else if (db[i].equals("b")) {
                    dbFragments[i] = 1;
                } else if (db[i].equals("") ||db[i].isEmpty()) {
                    dbFragments[i] = 2;
                }
            }
            for (int i = 1; i < 4 ; i++) {
                dbFragments[i] = Integer.parseInt(db[i]);
            }


            int[] pcFragments = new int[4]; // Максимум 4 элемента
            String[] pc = config.search("version").split("\\."); // Разделяем строку по точкам

            for (int i = 0; i < 4; i++) {
                if (i >= 4) break;
                if (pc[i].equals("a")) {
                    pcFragments[i] = 0; // Замена 'a' на 0
                } else if (pc[i].equals("b")) {
                    pcFragments[i] = 1;
                } else if (pc[i].equals("") ||pc[i].isEmpty()) {
                    pcFragments[i] = 2;
                }
            }
            for (int i = 1; i < 4 ; i++) {
                pcFragments[i] = Integer.parseInt(pc[i]);
            }

            for (int i = 0; i < 4; i++) {
                if (dbFragments[i] > pcFragments[i] && json.getValueObject(text.fromUrl(url), "versions", json.getValue(text.fromUrl(url), "version"), "stable").equals("true")) {
                    Lang lang = new Lang();
                    System.out.println(lang.getLine("update.found") +  " " + json.getValue(text.fromUrl(url), "version"));
                    return;
                }
            }
        }
    }
}