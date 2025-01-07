package me.fuzzi.dot.launcher.classes.text;

import me.fuzzi.dot.launcher.classes.Main;
import me.fuzzi.dot.launcher.classes.minecraft.*;
import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Folder;
import me.fuzzi.dot.launcher.classes.util.Lang;
import me.fuzzi.dot.launcher.classes.util.general.Download;
import me.fuzzi.dot.launcher.classes.util.general.JSON;
import me.fuzzi.dot.launcher.classes.util.general.Link;
import me.fuzzi.dot.launcher.classes.util.general.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CommandList {
    public static void registerCommands(Command command) {
        Lang lang = new Lang();
        command.create(new String[]{"jdk"}, 0, args -> {
            System.out.println(lang.getLine("jdk.start"));
            JDK jdk =  new JDK();
            jdk.download();
            System.out.println(lang.getLine("jdk.end"));
        });
        command.create(new String[]{"download"}, 2, args -> {
            System.out.println(lang.getLine("warn.unstable"));
            System.out.println(lang.getLine("version.download.1") + " " + args[0] + " " + lang.getLine("version.download.2") + " " + args[1] + "...");
            Download download = new Download();
            Version version = new Version();
            Folder folder = new Folder();
            download.fromUrl(version.getJar(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".jar");
            download.fromUrl(version.getJson(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".json");
        });
        command.create(new String[]{"libraries"}, 1, args -> {
            Libraries libraries = new Libraries();
            libraries.downloadLibraries(args[0]);
        });
        command.create(new String[]{"run", "launch", "play"}, 1, args -> {
            Main main = new Main();
            main.getRpc().setUp(lang.getLine("rpc.playing.up"));
            main.getRpc().setDown(lang.getLine("rpc.playing.down") + args[0]);

            try {
                Config config = new Config();
                Launch launch = new Launch(args[0], config.search("nickname"), "otag", 0, 2048);
                launch.launch();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            main.getRpc().setUp(lang.getLine("rpc.menu.up"));
            main.getRpc().setDown("");
        });
        command.create(new String[]{"assets"}, 1, args -> {
            System.out.println(lang.getLine("assets.start.1") + " " + args[0] + ". " + lang.getLine("assets.start.2"));
            Download download = new Download();
            Text text = new Text();
            JSON json = new JSON();
            Folder folder = new Folder();

            String f = text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[0] + folder.getSeparator() + args[0] + ".json");

            Link link = new Link();

            download.fromUrl(json.extractUrl(f, "assetIndex", "url"), folder.getMinecraft() + folder.getSeparator() + "assets" + folder.getSeparator() + "indexes", link.extractFileName(json.extractUrl(f, "assetIndex", "url")));

            Assets assets = new Assets();
            assets.download(args[0]);
            System.out.println(lang.getLine("assets.end"));
        });
        command.create(new String[]{"debug"}, 0, args -> {
            System.out.println(lang.getLine("debug.title"));
            System.out.println();

            Folder folder = new Folder();
            System.out.println("Folder");
            System.out.println("getSeparator: " + folder.getSeparator());
            System.out.println("getInit: " + folder.getInit());
            System.out.println("getMain: " + folder.getMain());
            System.out.println("getMinecraft: " + folder.getMinecraft());
            System.out.println();

            JDK jdk = new JDK();
            System.out.println("JDK");
            System.out.println("getJdk: " + jdk.getJdk());
        });
        command.create(new String[]{"exit", "quit", "close", "stop"}, 0, args -> {
            System.out.println(lang.getLine("exit.line"));
            System.exit(0);
        });
        command.create(new String[]{"natives"}, 1, args -> {
            Natives natives = new Natives();
            natives.download(args[0]);
        });
        command.create(new String[]{"fabric"}, 2, args -> {
            System.out.println(lang.getLine("warn.unstable"));
            System.out.println(lang.getLine("fabric.start.1") + " " + args[0] + " " + lang.getLine("fabric.start.2") + " " + args[1]);
            Fabric fabric = new Fabric();
            try {
                fabric.download(args[0], args[1]);
            } catch (IOException e) {
                System.out.println(lang.getLine("error.unexpected"));
                throw new RuntimeException(e);
            }
            Folder folder = new Folder();
            Download download = new Download();
            JSON json = new JSON();
            Text text = new Text();
            String version = json.getValue(text.fromFile(folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1] + folder.getSeparator() + args[1] + ".json"), "id");
            download.fromUrl("https://maven.fabricmc.net/net/fabricmc/fabric-loader/" + args[0] + "/fabric-loader-" + args[0] + ".jar", folder.getMinecraft() + folder.getSeparator() + "libraries" + folder.getSeparator() + args[1] + folder.getSeparator() + "net" + folder.getSeparator() + "fabricmc" + folder.getSeparator() + "fabric-loader" + folder.getSeparator() + args[0], "fabric-loader-" + args[0] + ".jar");
            download.fromUrl("https://maven.fabricmc.net/net/fabricmc/intermediary/" + version + "/intermediary-" + version + ".jar", folder.getMinecraft() + folder.getSeparator() + "libraries" + folder.getSeparator() + args[1] + folder.getSeparator() + "net" + folder.getSeparator() + "fabricmc" + folder.getSeparator() + "intermediary" + folder.getSeparator() + version, "intermediary-" + version + ".jar");
            System.out.println(lang.getLine("fabric.end"));
        });
        command.create(new String[]{"script", "scr"}, 1, args -> {
            Folder folder = new Folder();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(folder.getInit() + folder.getSeparator() + "scripts" + folder.getSeparator() + args[0] + ".dtl"));
                String line;
                while ((line = reader.readLine()) != null) {
                    CommandHandler commandHandler = new CommandHandler(command);
                    commandHandler.handleInput(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        command.create(new String[]{"com", "comment", "console", "cons"}, 1, args -> {
            System.out.println(args[0]);
        });
        command.create(new String[]{"thread", "sleep"}, 1, args -> {
            try {
                Thread.sleep(Integer.parseInt(args[0]) * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        command.create(new String[]{"config", "conf", "cnf"}, 2, args -> {
            Config config = new Config();
            config.write(args[0], args[1]);
        });
    }
}