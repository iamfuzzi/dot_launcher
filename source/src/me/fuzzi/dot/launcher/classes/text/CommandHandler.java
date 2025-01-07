package me.fuzzi.dot.launcher.classes.text;

import me.fuzzi.dot.launcher.classes.util.Lang;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    Lang lang = new Lang();
    private final Command command;

    public CommandHandler(Command command) {
        this.command = command;
    }

    public void handleInput(String input) {
        // Проверка на пустую строку или строку, состоящую только из пробелов
        if (input.trim().isEmpty()) {
            System.out.println("Ошибка: Команда не может состоять только из пробелов.");
            return; // Прекращаем выполнение, если команда некорректна
        }

        String[] parts = parseInput(input);
        String commandName = parts[0];
        String[] commandArgs = new String[parts.length - 1];
        System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

        // Проверка, что все аргументы начинаются с '-'
        if (validateArgs(commandArgs)) {
            command.execute(commandName, cleanArgs(commandArgs));
        } else {
            System.out.println(lang.getLine("cmd.args.format"));
        }
    }

    private String[] parseInput(String input) {
        List<String> args = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes; // Переключаем состояние кавычек
            } else if (c == ' ' && !inQuotes) {
                if (currentArg.length() > 0) {
                    args.add(currentArg.toString());
                    currentArg.setLength(0); // Очищаем текущий аргумент
                }
            } else {
                currentArg.append(c);
            }
        }

        // Добавляем последний аргумент, если он есть
        if (currentArg.length() > 0) {
            args.add(currentArg.toString());
        }

        return args.toArray(new String[0]);
    }

    private boolean validateArgs(String[] args) {
        for (String arg : args) {
            if (!arg.startsWith("-")) {
                return false; // Если хотя бы один аргумент не начинается с '-', возвращаем false
            }
        }
        return true; // Все аргументы корректны
    }

    private String[] cleanArgs(String[] args) {
        String[] cleanedArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            cleanedArgs[i] = args[i].substring(1); // Убираем '-'
        }
        return cleanedArgs;
    }
}