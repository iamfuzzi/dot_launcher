package me.fuzzi.dot.launcher.classes;

import me.fuzzi.dot.launcher.classes.discord.RPC;
import me.fuzzi.dot.launcher.classes.text.Console;

public class Main {
    private RPC rpc = new RPC();
    public static void main(String[] args) {
        Main main = new Main();
        main.rpc.discord();
        main.rpc.setUp("В меню...");
        new Console();
    }
    public RPC getRpc() {
        return rpc;
    }
}