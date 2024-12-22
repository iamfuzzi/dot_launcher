package com.fuzzi.dot.launcher.main.frames;

import com.fuzzi.dot.launcher.main.interfaces.Tray;

import javax.swing.*;

public class Frame {
    public Frame() {
        // Основные параметры
        JFrame frame = new JFrame("Dot Launcher");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);

        // Создание иконки в системном трее
        new Tray(frame, "open exit");


    }
}
