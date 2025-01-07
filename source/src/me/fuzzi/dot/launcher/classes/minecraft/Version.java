package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

public class Version {
    private String manifest = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
    JSON json = new JSON();
    Text text = new Text();

    // Получение путей для скачивания .jar и .json файлов сборки
    public String getJson(String version) {
        return json.getVersion(text.fromUrl(manifest), version);
    }
    public String getJar(String version) {
        return json.getValueFromObject(text.fromUrl(getJson(version)), "downloads", "client", "url");
    }
}