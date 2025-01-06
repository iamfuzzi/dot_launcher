package me.fuzzi.dot.launcher.classes.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Command {
    private final Map<String, CommandDefinition> commands = new HashMap<>();

    public void create(String[] names, int argCount, Consumer<String[]> action) {
        for (String name : names) {
            commands.put(name, new CommandDefinition(argCount, action));
        }
    }

    public void execute(String name, String[] args) {
        CommandDefinition command = commands.get(name);
        if (command != null) {
            if (args.length == command.argCount) {
                command.action.accept(args);
            } else {
                System.out.println("Ошибка: Ожидалось " + command.argCount + " аргументов, но получено " + args.length + ".");
            }
        } else {
            System.out.println("Неизвестная команда: " + name);
        }
    }
    private static class CommandDefinition {
        int argCount;
        Consumer<String[]> action;

        CommandDefinition(int argCount, Consumer<String[]> action) {
            this.argCount = argCount;
            this.action = action;
        }
    }
}