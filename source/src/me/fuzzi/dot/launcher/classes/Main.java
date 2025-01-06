package me.fuzzi.dot.launcher.classes;

import me.fuzzi.dot.launcher.classes.discord.RPC;
import me.fuzzi.dot.launcher.classes.text.Command;
import me.fuzzi.dot.launcher.classes.text.CommandHandler;
import me.fuzzi.dot.launcher.classes.text.CommandList;

import java.util.Scanner;

public class Main {
    private RPC rpc = new RPC();

    public static void main(String[] args) {
        Main main = new Main();
        main.rpc.discord();
        main.rpc.setUp("В меню...");

        Scanner scanner = new Scanner(System.in);
        Command command = new Command();
        CommandList.registerCommands(command); // Регистрация команд

        CommandHandler commandHandler = new CommandHandler(command);

        System.out.println("Dot Launcher - console git@iamfuzzi");
        System.out.println();
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            commandHandler.handleInput(input);
        }
    }
    public RPC getRpc() {
        return rpc;
    }
}