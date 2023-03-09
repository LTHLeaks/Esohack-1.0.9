package com.esoterik.client.features.modules.client;

import com.esoterik.client.esohack;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.util.TextUtil;

public class Gondal extends Module {

    public Gondal() {
        super("Gondal", "I love gondal", Module.Category.CLIENT, true, false, false);
    }

    private void doenable() {
        esohack.setName("gondal.club");
        esohack.commandManager.setClientMessage(TextUtil.coloredString("[", TextUtil.Color.WHITE) + TextUtil.coloredString(esohack.getName(), TextUtil.Color.DARK_PURPLE) + TextUtil.coloredString("]", TextUtil.Color.WHITE));
    }

    private void dodisable() {
        esohack.setName("esohack");
        esohack.commandManager.setClientMessage(TextUtil.coloredString("[", TextUtil.Color.WHITE) + TextUtil.coloredString(esohack.getName(), TextUtil.Color.DARK_PURPLE) + TextUtil.coloredString("]", TextUtil.Color.WHITE));
    }

    @Override
    public void onUpdate() {
        if (Gondal.fullNullCheck()) {
            return;
        }
        this.doenable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.dodisable();
    }
}

