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

import java.io.*;
import java.util.Scanner;

public class CommandList {
    private static volatile boolean isInterrupted;
    public static void registerCommands(Command command) {
        Lang lang = new Lang();

        // Вывод отладочной информации
        command.create(new String[]{"debug"}, 0, args -> {
            System.out.println(lang.getLine("debug.title"));
            System.out.println();
            System.out.println("English text");
            System.out.println("Текст на русском");
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
            System.out.println();
        });

        // Завершение программы
        command.create(new String[]{"exit", "quit", "close", "stop"}, 0, args -> {
            System.out.println(lang.getLine("exit.line"));
            System.exit(0);
        });


        // Запуск скрипта
        command.create(new String[]{"script", "scr"}, 2, args -> {
            Folder folder = new Folder();
            isInterrupted = false;

            BufferedReader reader = null;
            String path;
            try {
                if (args[1].equals("-c")) { // Пользовательские скрипты
                    path = folder.getInit() + folder.getSeparator() + "scripts" + folder.getSeparator() + args[0] + ".dtl";
                } else if (args[1].equals("-s")) { // Системные скрипты
                    path = folder.getMain() + folder.getSeparator() + "scripts" + args[0] + ".dtl";
                } else {
                    System.out.println("--c - пользовательские скрипты (папка\\scripts\\файлы .dtl), --s - системные скрипты (не выполнять, если не знаете что выполняете!)");
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (isInterrupted) { // Если выполнен force, прекратить выполнение
                        break;
                    }
                    if (!line.startsWith(">")) { // Если строка - не комментарий
                        CommandHandler commandHandler = new CommandHandler(command);
                        commandHandler.handleInput(line);
                    }
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

        // Комментарии в консоль
        command.create(new String[]{"console", "cons", "print"}, 1, args -> {
            System.out.println(args[0]);
        });

        // Приостановить выполнение скрипта, время в секундах
        command.create(new String[]{"thread", "sleep", "wait"}, 1, args -> {
            try {
                Thread.sleep(Integer.parseInt(args[0]) * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Прерывает выполнение скрипта
        command.create(new String[]{"force", "fstop", "interrupt", "istop"}, 0, args -> {
            isInterrupted = true;
            System.out.println(lang.getLine("cmd.interrupt"));
        });

        // Получает значение Y или N от пользователя
        command.create(new String[]{"get"}, 3, args -> {
            System.out.println(lang.getLine("cmd.get"));
            System.out.print(lang.getLine("cmd.script.name") + " > ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if ("-wait -yAct -nAct -другой -аргумент".contains(args[0])) { // Сущетсвует ли такой аргумент

                if (input.toLowerCase().equals("y")) { // Если ввод положительный
                    if (args[0].equals("-wait")) {
                        // Ничего
                    } else if (args[0].equals("-yAct")) {
                        CommandHandler commandHandler = new CommandHandler(command);
                        commandHandler.handleInput(args[1]);
                    } else if (args[0].equals("-nAct")) {
                        // Ничего
                    }

                } else if (input.toLowerCase().equals("n")) { // Если ввод отрицательный
                    if (args[0].equals("-wait")) {
                        CommandHandler commandHandler = new CommandHandler(command);
                        commandHandler.handleInput("force");
                    } else if (args[0].equals("-yAct")) {
                        // Ничего
                    } else if (args[0].equals("-nAct")) {
                        CommandHandler commandHandler = new CommandHandler(command);
                        commandHandler.handleInput(args[1]);
                    }

                } else { // Если ввод не соответствует
                    CommandHandler commandHandler = new CommandHandler(command);
                    commandHandler.handleInput("force");
                }

            } else { // Если аргумента команды не существует
                System.out.println(lang.getLine("cmd.get.no"));
                CommandHandler commandHandler = new CommandHandler(command);
                commandHandler.handleInput("force");
            }
        });


        // Скачивание версии и сохранение ее
        command.create(new String[]{"download"}, 2, args -> {
            System.out.println(lang.getLine("warn.unstable"));
            System.out.println(lang.getLine("version.download.1") + " " + args[0] + " " + lang.getLine("version.download.2") + " " + args[1] + "...");
            Download download = new Download();
            Version version = new Version();
            Folder folder = new Folder();
            download.fromUrl(version.getJar(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".jar");
            download.fromUrl(version.getJson(args[0]), folder.getMinecraft() + folder.getSeparator() + "versions" + folder.getSeparator() + args[1], args[1] + ".json");
        });

        // Установка библиотек
        command.create(new String[]{"libraries"}, 1, args -> {
            Libraries libraries = new Libraries();
            libraries.downloadLibraries(args[0]);
        });

        // Установка нативов, иногда не требуется
        command.create(new String[]{"natives"}, 1, args -> {
            Natives natives = new Natives();
            natives.download(args[0]);
        });

        // Установка ассетов
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

        // Установка Fabric
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

        // Запуск игры
        command.create(new String[]{"run", "launch", "play", "start"}, 1, args -> {
            Main main = new Main();
            main.getRpc().setUp(lang.getLine("rpc.playing.up"));
            main.getRpc().setDown(lang.getLine("rpc.playing.down") + " " + args[0]);

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


        // Изменить параметр в настройках лаунчера
        command.create(new String[]{"config", "conf", "cnf", "param"}, 2, args -> {
            Config config = new Config();
            config.write(args[0], args[1]);
        });

        // Установка JDK
        command.create(new String[]{"jdk"}, 0, args -> {
            System.out.println(lang.getLine("jdk.start"));
            JDK jdk =  new JDK();
            jdk.download();
            System.out.println(lang.getLine("jdk.end"));
        });
    }
}