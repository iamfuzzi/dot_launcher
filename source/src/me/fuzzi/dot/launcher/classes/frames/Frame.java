package me.fuzzi.dot.launcher.classes.frames;

import me.fuzzi.dot.launcher.classes.interfaces.Elements;
import me.fuzzi.dot.launcher.classes.interfaces.GUI;
import me.fuzzi.dot.launcher.classes.interfaces.Tray;
import me.fuzzi.dot.launcher.classes.interfaces.UI;
import me.fuzzi.dot.launcher.classes.minecraft.Skin;
import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.Download;
import me.fuzzi.dot.launcher.classes.util.general.Properties;

import javax.swing.*;

public class Frame {
    public Frame() {
        JFrame frame = new JFrame("Dot Launcher");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);

        Folder folder = new Folder();

        // Переменные размера верхней панели
        int upper = 30;
        int size = 16;
        int offset = (upper - size) / 2;

        UI ui = new UI(frame); // Создание UI компонентов
        ui.moveable(frame.getWidth(), upper);
        ui.setIcon("dirt.png");

        Tray tray = new Tray(frame, "open exit"); // Создание системного трея

        Skin skin = new Skin();
        Download download = new Download();
        Config config = new Config();
        download.fromUrl(skin.getSkin(config.search("nickname")), folder.getInit() + folder.getSeparator() + "launcher" + folder.getSeparator() + "temp" + folder.getSeparator(), "skin.png");



        GUI gui = new GUI(frame); // Создание GUI компонентов

        // Основные элементы
        gui.bg("3C3F44");
        gui.rectangle(0, upper - 1, frame.getWidth(), 1, "555555", 0);
        gui.rectangle(49, 30, 1, 700, "555555", 0);
        gui.rectangle(699, 30, 1, 700, "555555", 0);
        gui.rectangle(50, 499, 649, 1, "555555", 0);

        // Логотип и название
        gui.image(4, 4, 22, 22, folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "dirt.png", 1);
        gui.text(32, 20, 12, "Dot Launcher", "Minecraft Rus", "FFFFFF", 2);

        // Кнопка сворачивания окна
        gui.button(frame.getWidth() - size - offset, offset, size, size, "hide", null, null, 10);
        gui.image(frame.getWidth() - size - offset, offset, size, size, folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "icons" + folder.getSeparator() + "close.png", 11);


        Elements elements = new Elements(frame, gui);

        // Левая панель
        elements.head(10, 40, 30, 30, 10); // Голова игрока
        gui.image(10, 90, 30, 30, folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "grass.png", 1); // Иконка настроек
        gui.button(10, 90, 30, 30, "new", null, null, 0); // Кнопка входа в настройки

        // Нижняя панель
        /*gui.button(150, 550, 200, 100, "start", temps.getVersion(), null, 4);
        gui.rectangle(150, 550, 200, 100, "ffffff", 5);*/


        /*Directories directories = new Directories();

        for (int i = 0; i < directories.findMatchingFolders().length; i++) {
            if (directories.findMatchingFolders()[0] == "Нет папок") {
                gui.text(725, 320, 14, "У Вас нет ни одной сборки...", "Minecraft Rus", "AAAAAA", 10);
                gui.text(757, 337, 14, "Давай сделаем одну!", "Minecraft Rus", "AAAAAA", 10);
                gui.button(700 + (300 / 2) - (40 / 2), 30 + (270 / 2) + 200, 40, 40, "new", null, null, 10);
            } else {
                Properties properties = new Properties();
                elements.version(directories.findMatchingFolders()[i], "1.20.1");
            }
        }*/

        // Визуализация окна
        frame.pack();
        frame.setVisible(true);
    }
}
