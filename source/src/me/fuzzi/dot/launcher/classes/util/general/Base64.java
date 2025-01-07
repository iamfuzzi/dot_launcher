package me.fuzzi.dot.launcher.classes.util.general;

import java.nio.charset.StandardCharsets;

public class Base64 {

    // Декодирование из Base64
    public String decode(String value) {
        byte[] decoder = java.util.Base64.getDecoder().decode(value);
        String string = new String(decoder, StandardCharsets.UTF_8);
        return string;
    }
}