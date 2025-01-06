package me.fuzzi.dot.launcher.classes.frames;

import me.fuzzi.dot.launcher.classes.interfaces.GUI;
import me.fuzzi.dot.launcher.classes.interfaces.Tray;
import me.fuzzi.dot.launcher.classes.interfaces.UI;
import me.fuzzi.dot.launcher.classes.util.Folder;

import javax.swing.*;

public class New {
    public static void main(String[] args) {
        new New();
    }
    public New() {
        JFrame frame = new JFrame("Dot Launcher | Новая сборка");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);

        Folder folder = new Folder();

        // Переменные размера верхней панели
        int upper = 30;
        int size = 16;
        int offset = (upper - size) / 2;

        Tray tray = new Tray(frame, "open, exit");

        UI ui = new UI(frame); // Создание UI компонентов
        ui.moveable(frame.getWidth(), upper);
        ui.setIcon("dirt.png");

        GUI gui = new GUI(frame); // Создание GUI компонентов

        // Основные элементы
        gui.bg("3C3F44");
        gui.rectangle(0, upper - 1, frame.getWidth(), 1, "555555", 0);

        // Логотип и название
        gui.image(4, 4, 22, 22, folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "dirt.png", 1);
        gui.text(32, 20, 12, "Dot Launcher | Новая сборка", "Minecraft Rus", "FFFFFF", 2);

        // Кнопка сворачивания окна
        gui.button(frame.getWidth() - size - offset, offset, size, size, "hide", null, null, 10);
        gui.image(frame.getWidth() - size - offset, offset, size, size, folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "icons" + folder.getSeparator() + "close.png", 11);


        // Визуализация окна
        frame.pack();
        frame.setVisible(true);
    }
}