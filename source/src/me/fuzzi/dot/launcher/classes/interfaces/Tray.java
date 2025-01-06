package me.fuzzi.dot.launcher.classes.interfaces;

import me.fuzzi.dot.launcher.classes.util.Folder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tray {
    private SystemTray tray;
    private TrayIcon icon;

    // Конструктор
    public Tray(JFrame frame, String modules) {
        if(!SystemTray.isSupported()) {
            System.out.println("Ошибка: системный трей не поддерживается");
            return;
        }

        Folder folder = new Folder();

        Image image = Toolkit.getDefaultToolkit().getImage(folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + "dirt.png");
        icon = new TrayIcon(image, frame.getName());

        PopupMenu popup = new PopupMenu();

        // Добавление модулей системного трея
        if (modules.contains("open")) { // Модуль открытия окна
            MenuItem open = new MenuItem("Открыть");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> frame.setVisible(true));
                }
            });
            popup.add(open);
        }
        if (modules.contains("exit")) { // Модуль выхода из лаунчера
            MenuItem exit = new MenuItem("Выключить");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            popup.add(exit);
        }

        icon.setPopupMenu(popup);
        icon.setImageAutoSize(true);

        tray = SystemTray.getSystemTray();
        try {
            tray.add(icon);
        } catch (AWTException e) {
            System.out.println("Ошибка: не удалось добавить иконку");
        }
    }
}