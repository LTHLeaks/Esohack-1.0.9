package com.esoterik.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.esoterik.client.features.modules.misc.RPC;

public class Discord {

    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static RPC discordrpc;
    private static Thread thread;

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("823184074369663016", handlers, true, "");
        Discord.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.presence.details = esohack.getName() + " v" + "1.0.5";
        Discord.presence.state = "balling";
        Discord.presence.largeImageKey = "download";
        Discord.presence.largeImageText = "https://discord.gg/wJq5nMEdNT";
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                Discord.presence.details = esohack.getName() + " v" + "1.0.5";
                Discord.presence.state = "balling";
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }
}