package me.fuzzi.dot.launcher.classes.minecraft;

import me.fuzzi.dot.launcher.classes.util.general.Base64;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Text;

public class Skin {
    public String getSkin(String name) {
        JSON json = new JSON();
        return json.getValueFromObject(getInfo(name), "textures", "SKIN", "url");
    }

    private String getInfo(String name) {
        Text text = new Text();
        JSON json = new JSON();
        Base64 base64 = new Base64();

        String l1 = text.fromUrl("https://api.mojang.com/users/profiles/minecraft/" + name);
        String l2 = json.getValue(l1, "id");
        String l3 = text.fromUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + l2);
        String l4 = json.getValueArray(l3, "properties", "value");
        String l5 = base64.decode(l4);

        return l5;
    }
}