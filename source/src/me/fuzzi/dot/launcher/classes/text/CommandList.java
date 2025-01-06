package me.fuzzi.dot.launcher.classes.text;

import me.fuzzi.dot.launcher.classes.Main;
import me.fuzzi.dot.launcher.classes.minecraft.*;
import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.general.Download;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Link;
import me.fuzzi.dot.launcher.classes.util.general.Text;

import java.io.IOException;

public class CommandList {
    public static void registerCommands(Command command) {
        command.createMultiName(new String[]{"jdk"}, 0, args -> {
            System.out.println("Начало установки JDK...");
            JDK jdk =  new JDK();
            jdk.download();
            System.out.println("JDK успешно установлен!");
        });
        command.createMultiName(new String[]{"download"}, 2, args -> {
            System.out.println("Установка версии " + args[0] + " как " + args[1] + "...");
            Download download = new Download();
            Version version = new Version();
            Folder folder = new Folder();
            download.fromUrl(version.getJar(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".jar");
            download.fromUrl(version.getJson(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".json");
        });
        command.createMultiName(new String[]{"libraries"}, 1, args -> {
            Libraries libraries = new Libraries();
            libraries.downloadLibraries(args[0]);
        });
        command.createMultiName(new String[]{"run", "launch", "play"}, 1, args -> {
            Main main = new Main();
            main.getRpc().setUp("В игре");
            main.getRpc().setDown("Играет в " + args[0]);

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
                    Launch launch = new Launch(args[0], java, config.search("nickname"), "otag");
                    launch.launch();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
            main.getRpc().setUp("В меню...");
            main.getRpc().setDown("");
        });
        command.createMultiName(new String[]{"assets"}, 1, args -> {
            Assets assets = new Assets(args[0]);
        });
        command.createMultiName(new String[]{"index"}, 1, args -> {
            Download download = new Download();
            Text text = new Text();
            JSON json = new JSON();
            Folder folder = new Folder();

            String f = text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[0] + folder.getSeparator() + args[0] + ".json");

            System.out.println(json.extractUrl(f, "assetIndex", "url"));

            Link link = new Link();

            download.fromUrl(json.extractUrl(f, "assetIndex", "url"), folder.getMinecraft() + folder.getSeparator() + "assets" + folder.getSeparator() + "indexes", link.extractFileName(json.extractUrl(f, "assetIndex", "url")));
        });
        command.createMultiName(new String[]{"debug"}, 0, args -> {
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
        });
        command.createMultiName(new String[]{"exit", "quit", "close", "stop"}, 0, args -> {
            System.out.println("Выход из программы...");
            System.exit(0);
        });
    }
}