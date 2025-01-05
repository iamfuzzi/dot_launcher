package me.fuzzi.dot.launcher.classes.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RPC {
    private String up;
    private String down;
    public void discord() {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String appid = "1324375125847441475";
        String steamid = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (a) -> System.out.println("Ready!");
        lib.Discord_Initialize(appid, handlers, true, steamid);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = down;
        presence.state = up;

        lib.Discord_UpdatePresence(presence);

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void setUp(String up) {
        this.up = up;
        update();
    }
    public void setDown(String down) {
        this.down = down;
        update();
    }
    public void update() {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = down;
        presence.state = up;
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }
}
