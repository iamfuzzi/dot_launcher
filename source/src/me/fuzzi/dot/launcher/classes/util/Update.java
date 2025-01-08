package me.fuzzi.dot.launcher.classes.util;

import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

public class Update {
    public Update() {
        Config config = new Config();
        if (config.search("checkUpdates").equals("false")) {
            return;
        } else {
            JSON json = new JSON();
            Text text = new Text();
            String url = "https://raw.githubusercontent.com/iamfuzzi/dot_launcher/refs/heads/main/version_manifest.json";

            int[] dbFragments = new int[4];
            String[] db = json.getValue(text.fromUrl(url), "version").split("\\.");

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


            int[] pcFragments = new int[4];
            String[] pc = config.search("version").split("\\.");

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

            Lang lang = new Lang();
            for (int i = 0; i < 4; i++) {
                if (dbFragments[i] > pcFragments[i] && json.getValueObject(text.fromUrl(url), "versions", json.getValue(text.fromUrl(url), "version"), "stable").equals("true")) {
                    System.out.println(lang.getLine("update.found.1") +  " " + json.getValue(text.fromUrl(url), "version") + "! " + lang.getLine("update.found.2"));
                    return;
                }
            }
            System.out.println(lang.getLine("update.found.no"));
        }
    }
}