package me.fuzzi.dot.launcher.classes.text;

import me.fuzzi.dot.launcher.classes.util.Lang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Command {
    Lang lang = new Lang();
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
                System.out.println(lang.getLine("cmd.args.1") + " " + command.argCount + " " + lang.getLine("cmd.args.2") + " " + args.length);
            }
        } else {
            System.out.println(lang.getLine("cmd.unknown") + ": " + name);
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