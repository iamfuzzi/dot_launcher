package me.fuzzi.dot.launcher.classes.interfaces;

import me.fuzzi.dot.launcher.classes.util.Folder;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
    private int offsetX, offsetY;
    private JFrame frame;

    // Конструктор
    public UI(JFrame frame) {
        this.frame = frame;
    }


    // Возможность перемещения окна
    public void moveable(int finalX, int finalY) {
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() <= finalX && e.getY() <= finalY) {
                    offsetX = e.getX();
                    offsetY = e.getY();
                }
            }
        });
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getX() <= finalX && e.getY() <= finalY) {
                    frame.setLocation(frame.getX() + e.getX() - offsetX, frame.getY() + e.getY() - offsetY);
                }
            }
        });
    }

    // Установка иконки
    public void setIcon(String path) {
        Folder folder = new Folder();
        ImageIcon icon = new ImageIcon(folder.getMain() + folder.getSeparator() + "textures" + folder.getSeparator() + path);
        frame.setIconImage(icon.getImage());
    }
}