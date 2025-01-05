package me.fuzzi.dot.launcher.classes.util.general;

public class Link {
    public String extractFileName(String url) {
        // Находим последний символ '/' в URL
        int lastSlashIndex = url.lastIndexOf('/');

        // Извлекаем подстроку, начиная с символа после последнего '/'
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return ""; // Если не удалось
    }
}