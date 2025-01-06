package me.fuzzi.dot.launcher.classes.interfaces;

import me.fuzzi.dot.launcher.classes.frames.New;
import me.fuzzi.dot.launcher.classes.minecraft.Launch;
import me.fuzzi.dot.launcher.classes.util.Folder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class GUI {
    private JFrame frame;
    private JLayeredPane layeredPane;
    private Map<String, String> fonts;

    Folder folder = new Folder();

    // Конструктор
    public GUI(JFrame frame) {
        this.frame = frame;
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        frame.add(layeredPane);

        fonts = new HashMap<>();

        String file = folder.getMain() + folder.getSeparator() + "fonts" + folder.getSeparator();

        fonts.put("Minecraft Rus", file + "Minecraft Rus.ttf");
        // ...другие шрифты
    }


    // Создание объекта прямоугольника
    public void rectangle(int x, int y, int width, int height, String hex, int layer) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g)  {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                d.setColor(toColor(hex));
                d.fillRect(0, 0, width, height);
            }
        };
        panel.setBounds(x, y, width, height);
        layeredPane.add(panel, Integer.valueOf(layer));
    }

    // Создание горизонтального градиента
    public void gradient(int x, int y, int width, int height, String hex1, String hex2, int alpha1, int alpha2, int layer) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                Color color1 = toColorAlpha(hex1, alpha1);
                Color color2 = toColorAlpha(hex2, alpha2);

                GradientPaint gradientPaint = new GradientPaint(0, 0, color1, 0, height, color2);
                d.setPaint(gradientPaint);

                d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            }
        };
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        layeredPane.add(panel, Integer.valueOf(layer));
    }


    // Создание текста
    public void text(int x, int y, int size, String content, String font, String hex, int layer) {
        String fontPath = fonts.get(font);
        if (fontPath == null) {
            System.err.println("Шрифт " + font + " не найден!");
            return;
        }
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont((float) size);
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent (java.awt.Graphics g) {
                    super.paintComponent(g);
                    Graphics2D d = (Graphics2D) g;

                    // Сглаживание
                    d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    d.setFont(customFont);
                    d.setColor(toColor(hex));
                    d.drawString(content, x, y);
                }
            };
            panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            panel.setOpaque(false);
            layeredPane.add(panel, Integer.valueOf(layer));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }


    // Создание картинки
    public void image(int x, int y, int width, int height, String path, int layer) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                // Сглаживание
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                ImageIcon image = new ImageIcon(path);
                d.drawImage(image.getImage(), 0, 0, width, height, null);
            }
        };
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        layeredPane.add(panel, Integer.valueOf(layer));
    }

    // Создание картинки с закругленными краями
    public void roundedImage(int x, int y, int width, int height, int arcWidth, int arcHeight, String path, int layer) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                // Сглаживание
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                ImageIcon image = new ImageIcon(path);
                Image img = image.getImage();

                // Прямоугольник с закругленными краями
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight);
                d.setClip(roundedRectangle);
                d.drawImage(img, 0, 0, width, height, null);
                d.setClip(null);
            }
        };
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        layeredPane.add(panel, Integer.valueOf(layer));
    }

    // Создание обрезанной картинки с закругленными краями
    public void cutRoundedImage(int x, int y, int width, int height, int arcWidth, int arcHeight, int srcX, int srcY, int srcWidth, int srcHeight, String path, int layer) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                // Без сглаживания

                ImageIcon image = new ImageIcon(path);
                Image img = image.getImage();

                // Прямоугольник с закругленными краями
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight);
                d.setClip(roundedRectangle);

                // Отображение части изображения
                d.drawImage(img, 0, 0, width, height, srcX, srcY, srcX + srcWidth, srcY + srcHeight, null);
                d.setClip(null);
            }
        };
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        layeredPane.add(panel, Integer.valueOf(layer));
    }


    // Создание многофункциональной кнопки
    public void button(int x, int y, int width, int height, String action, String arg1, String arg2, int layer) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false); // Есть ли заливка
        button.setBorderPainted(false); // Есть ли рамка
        button.setFocusable(false); // Убирает точку у прозрачной кнопки

        //Current current = new Current();

        // Список возможных событий для кнопок
        button.addActionListener(e -> {
            if (action == "hide") {
                frame.setVisible(false);
            } /*else if (action == "start") {
                new Thread(() -> {
                    try {
                        Temps temps = new Temps();
                        Launch launch = new Launch(temps.getVersion(), "C:\\Program Files\\Java\\jdk-17\\bin\\java.exe", "fuzik", "buhflip");
                        String[] texts = {"Загрузка", "Загрузка.", "Загрузка..", "Загрузка..."};
                        startTextAnimation(100, 100, 14, "Minecraft Rus", "ffffff", texts, 500, 10);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        frame.hide();
                                    }
                                },
                                5000
                        );
                        launch.launch();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }*/ else if (action == "new") {
                new New();
            } /*else if (action == "set") {
                Temps temps = new Temps();
                temps.setVersion(arg1);
            }*/

            /*else if (action == "load") {
                Libraries libraries = new Libraries();
                libraries.downloadLibraries(current.getName());
            } else if (action == "start") {
                Startup startup = new Startup(parameter1, "C:\\Program Files\\Java\\jdk-17\\bin\\java.exe", "fuzik", "buhflip");
                try {
                    startup.launch();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (action == "select") {
                current.setName(parameter1);
            } else if (action == "bg") {
                button.addActionListener(e -> {
                    JPanel panel = new JPanel() {
                        @Override
                        protected void paintComponent(java.awt.Graphics g)  {
                            super.paintComponent(g);
                            Graphics2D d = (Graphics2D) g;

                            d.setColor(toColor("a2e370"));
                            d.fillRect(0, 0, frame.getWidth(), frame.getHeight());
                        }
                    };
                    panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                    layeredPane.add(panel, Integer.valueOf(-99));
                });
            } */
        });
        layeredPane.add(button, Integer.valueOf(layer));
    }

    // Анимация текста
    public void startTextAnimation(int x, int y, int size, String font, String hex, String[] texts, int time, int layer) {
        int[] currentIndex = {0}; // Используем массив для изменения значения внутри анонимного класса

        // Создаем панель для отображения текста
        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D d = (Graphics2D) g;

                // Сглаживание
                d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                String fontPath = fonts.get(font);
                if (fontPath == null) {
                    System.err.println("Шрифт " + font + " не найден!");
                    return;
                }
                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont((float) size);
                    d.setFont(customFont);
                    d.setColor(toColor(hex));
                    d.drawString(texts[currentIndex[0]], x, y);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
            }
        };

        textPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        textPanel.setOpaque(false);
        layeredPane.add(textPanel, Integer.valueOf(layer));

        // Создаем таймер для смены текста
        Timer timer = new Timer(time, e -> {
            currentIndex[0] = (currentIndex[0] + 1) % texts.length; // Переключаем индекс
            textPanel.repaint(); // Перерисовываем панель
        });
        timer.start(); // Запускаем таймер
    }


    // "Короткие методы".  Методы, основанные на других методах, укорачивают реализацию некоторых вещей

    // Установка фона
    public void bg(String hex) {
        rectangle(0, 0, frame.getWidth(), frame.getHeight(), hex, -100);
    }


    // Прайваты. Технические методы для реализации других методов

    // Перевод строки в цвет
    private Color toColor(String hex) {
        Color color = new Color(Integer.parseInt(hex, 16));
        return color;
    }

    // Перевод строки в цвет с прозрачностью
    private Color toColorAlpha(String hex, int opacity) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        int alpha = (int) (opacity * 2.55);

        return new Color(r, g, b, alpha);
    }
}