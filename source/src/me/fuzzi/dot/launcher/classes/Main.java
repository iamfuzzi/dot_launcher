package me.fuzzi.dot.launcher.classes;

import me.fuzzi.dot.launcher.classes.discord.RPC;
import me.fuzzi.dot.launcher.classes.frames.Frame;
import me.fuzzi.dot.launcher.classes.text.Command;
import me.fuzzi.dot.launcher.classes.text.CommandHandler;
import me.fuzzi.dot.launcher.classes.text.CommandList;
import me.fuzzi.dot.launcher.classes.util.Config;
import me.fuzzi.dot.launcher.classes.util.Lang;
import me.fuzzi.dot.launcher.classes.util.Update;

import java.util.Scanner;

public class Main {
    private RPC rpc = new RPC();

    public static void main(String[] args) {
        new Update();

        Lang lang = new Lang();

        Main main = new Main();
        main.rpc.discord();
        main.rpc.setUp(lang.getLine("rpc.menu.up"));

        Config config = new Config();
        if (config.search("console") == null || config.search("console").isEmpty() || config.search("console").equals("false") || !config.search("console").equals("true")) {
            new Frame();
        } else {
            Scanner scanner = new Scanner(System.in, "Cp866");
            Command command = new Command();
            CommandList.registerCommands(command); // Регистрация команд

            CommandHandler commandHandler = new CommandHandler(command);

            System.out.println("Dot Launcher (console) - git@iamfuzzi");
            System.out.println();
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                commandHandler.handleInput(input);
            }
        }
    }
    public RPC getRpc() {
        return rpc;
    }
}