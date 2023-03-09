package com.esoterik.client.features.modules.client;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.ClientEvent;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ClickGui extends Module {

    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Integer> backgroundAlpha = this.register(new Setting<Integer>("BackgroundAlpha", 140, 0, 255));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.register(new Setting<Object>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), v -> this.customFov.getValue()));
    public Setting<String> moduleButton = this.register(new Setting<String>("Buttons", ""));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("ColorSync", false));
    private static ClickGui INSTANCE = new ClickGui();

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                esohack.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to \u00a7a" + esohack.commandManager.getPrefix());
            }
            esohack.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)new esohackGui());
    }

    @Override
    public void onLoad() {
        esohack.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        esohack.commandManager.setPrefix(this.prefix.getValue());
    }

    public Color getColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof esohackGui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof esohackGui) {
            mc.displayGuiScreen(null);
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}