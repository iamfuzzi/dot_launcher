package me.fuzzi.dot.launcher.classes.text;

import me.fuzzi.dot.launcher.classes.Main;
import me.fuzzi.dot.launcher.classes.minecraft.*;
import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.Download;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Link;

import java.io.IOException;
import java.util.Scanner;

public class Console {
    public Console() {
        Config config = new Config();
        if (config.search("console") == null || config.search("console").isEmpty() || config.search("console").equals("false")) { // Если параметр console в конфиге отсутствует, равен пустоте или равен false
            return;
        }  else if (config.search("console").equals("true")) { // Если равен true
            console();
        } else {
            return;
        }
    }
    private static void console() {
        System.out.println("Dot Launcher v.0.1 nogui");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("jdk")) {
                JDK jdk =  new JDK();
                jdk.download();
                System.out.println("JDK установлен");
            } else if (input.contains("links ")) {
                if (!isEmpty(input, "links")) {
                    Version version = new Version();
                    System.out.println(version.getJson(arg(input, "links")));
                    System.out.println(version.getJar(arg(input, "links")));
                }
            } else if (input.contains("download")) {
                String[] result = split(input);
                System.out.println("Установка версии " + result[0] + " как " + result[1] + "...");
                Download download = new Download();
                Version version = new Version();
                Folder folder = new Folder();
                download.fromUrl(version.getJar(result[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + result[1], result[1] + ".jar");
                download.fromUrl(version.getJson(result[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + result[1], result[1] + ".json");
            } else if (input.contains("libraries ")) {
                if (!isEmpty(input, "libraries")) {
                    Libraries libraries = new Libraries();
                    libraries.downloadLibraries(arg(input, "libraries"));
                }
            } else if (input.contains("launch ")) {
                if (!isEmpty(input, "launch")) {
                    Main main = new Main();
                    main.getRpc().setUp("В игре");
                    main.getRpc().setDown("Играет в " + arg(input, "launch"));

                    new Thread(() -> {
                        try {
                            Config config = new Config();
                            Folder folder = new Folder();
                            String java;
                            if (config.search("java").equals("dot")) {
                                java = folder.getInit() + folder.getSeparator() + "launcher" + folder.getSeparator() + "jdk" + folder.getSeparator() + "jdk-21_windows-x64_bin" + folder.getSeparator() + "jdk-21.0.5" + folder.getSeparator() + "bin" + folder.getSeparator() + "java.exe";
                            } else {
                                java = config.search("java");
                            }
                            Launch launch = new Launch(arg(input, "launch"), java, config.search("nickname"), "otag");
                            launch.launch();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                    main.getRpc().setUp("В меню...");
                }
            } else if (input.contains("assets ")) {
                if (!isEmpty(input, "assets")) {
                    Assets assets = new Assets(arg(input, "assets"));
                }
            } else if (input.contains("index ")) {
                if (!isEmpty(input, "index")) {
                    Download download = new Download();
                    me.fuzzi.dot.launcher.classes.util.general.Text text = new me.fuzzi.dot.launcher.classes.util.general.Text();
                    JSON json = new JSON();
                    Folder folder = new Folder();

                    String f = text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + arg(input, "index") + folder.getSeparator() + arg(input, "index") + ".json");

                    System.out.println(json.extractUrl(f, "assetIndex", "url"));

                    Link link = new Link();

                    download.fromUrl(json.extractUrl(f, "assetIndex", "url"), folder.getMinecraft() + folder.getSeparator() + "assets" + folder.getSeparator() + "indexes", link.extractFileName(json.extractUrl(f, "assetIndex", "url")));

                }
            } else if (input.equals("debug")) {
                System.out.println("Вывод отладочной информации");
                System.out.println();

                Folder folder = new Folder();
                System.out.println("Folder-методы");
                System.out.println("getSeparator: " + folder.getSeparator());
                System.out.println("getInit: " + folder.getInit());
                System.out.println("getMain: " + folder.getMain());
                System.out.println("getMinecraft: " + folder.getMinecraft());
                System.out.println();

                JDK jdk = new JDK();
                System.out.println("JDK-методы");
                System.out.println("getJdk: " + jdk.getJdk());
            } else if (input.equals("stop") || input.equals("exit")) {
                System.exit(0);
            } else {
                System.out.println("Ошибка ввода: Невозможно распознать комманду как выполняемую!");
            }
        }
    }

    private static boolean isEmpty(String string, String command) {
        String arg = string.replaceAll(command + " ", "").trim();
        if (arg.isEmpty()) {
            System.out.println("Ошибка ввода: Аргумент не может быть пустым!");
            return true;
        }
        return false;
    }
    private static String arg(String string, String command) {
        return string.replaceAll(command + " ", "");
    }
    private static String[] split(String inputString) {
        // Разделяем строку по пробелам
        String[] parts = inputString.split(" ");

        // Проверяем, что строка содержит нужные части
        if (parts.length >= 4 && "download".equals(parts[0]) && "as".equals(parts[2])) {
            String version = parts[1];
            String name = parts[3]; // Предполагаем, что имя состоит из одного слова
            return new String[]{version, name};
        } else {
            return null; // Неверный формат
        }
    }
}