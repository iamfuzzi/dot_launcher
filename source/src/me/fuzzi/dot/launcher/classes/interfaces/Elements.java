package me.fuzzi.dot.launcher.classes.interfaces;

import me.fuzzi.dot.launcher.classes.util.Folder;

import javax.swing.*;
import java.io.File;

public class Elements {
    private JFrame frame;
    private GUI gui;
    private int count;

    // Конструктор
    public Elements(JFrame frame, GUI gui) {
        this.frame = frame;
        this.gui = gui;
    }

    public void head(int x, int y, int size, int round, int layer) {
        Folder folder = new Folder();
        String path = folder.getInit() + folder.getSeparator() + "launcher" + folder.getSeparator() + "temp" + folder.getSeparator() + "skin.png";
        gui.cutRoundedImage(x, y, size, size, round, round, 8, 8, 8, 8, path, layer);
        gui.cutRoundedImage(x, y, size, size, round, round, 40, 8, 8, 8, path, layer + 1);
    }


    // Элемент версий. Создает объект отображения сборок в меню
    public void version(String name, String version) {
        Folder folder = new Folder();
        count++;

        gui.rectangle(700, 30 + count * 100, 300, 1, "555555", 4);
        gui.button(700, 30 + count * 100, 300, 100, "set", name, null, 4);

        String iconPath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + name + folder.getSeparator() + ".launcher" + folder.getSeparator() + "pack.png";
        String bannerPath = folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + name + folder.getSeparator() + ".launcher" + folder.getSeparator() + "banner.png";
        File icon = new File(iconPath);
        File banner = new File(bannerPath);

        // Добавляет заголовок
        gui.text(800, 60 + (count - 1) * 100, 16, name, "Minecraft Rus", "FFFFFF", 7);
        gui.text(800, 80 + (count - 1) * 100, 12, version, "Minecraft Rus", "AAAAAA", 7);

        // Добавляет иконку
        if (!icon.exists()) {
            iconPath = folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "icons" + folder.getSeparator() + "icon.png";
        }
        gui.roundedImage(710, 40 + (count - 1) * 100, 80, 80, 15, 15, iconPath, 10);

        // Добавляет баннер
        if (banner.exists()) {
            gui.image(700 , 30 + (count - 1) * 100, 300, 100, bannerPath, 4);
            gui.gradient(700, 30 + (count - 1) * 100, 300, 100, "3C3F44", "3C3F44", 50, 100, 6);
        }
    }


}