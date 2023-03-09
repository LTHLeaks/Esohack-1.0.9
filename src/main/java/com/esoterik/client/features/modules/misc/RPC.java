package com.esoterik.client.features.modules.misc;

import com.esoterik.client.Discord;
import com.esoterik.client.features.modules.Module;

public class RPC extends Module {

    public static RPC INSTANCE;

    public RPC() {
        super("RPC", "Discord rich presence", Module.Category.CLIENT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}

