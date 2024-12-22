package com.fuzzi.dot.launcher.main.interfaces;

import com.fuzzi.dot.launcher.main.utilities.Folder;

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
            System.out.println("Системный трей не поддерживается!");
            return;
        }

        Folder folder = new Folder();
        String separator = folder.getSeparator();

        Image image = Toolkit.getDefaultToolkit().getImage(folder.getMain() + separator + "textures" + separator + "icons" + separator + "icon.png");
        icon = new TrayIcon(image, frame.getName());

        PopupMenu popup = new PopupMenu();

        // Добавление модулей системного трея
        if (modules.contains("open")) {
            MenuItem open = new MenuItem("Открыть");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> frame.setVisible(true));
                }
            });
            popup.add(open);
        }
        if (modules.contains("exit")) {
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
            System.out.println("Не удалось добавить иконку!");
        }
    }
}
