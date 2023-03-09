package com.esoterik.client.features.modules.movement;

import com.esoterik.client.event.events.KeyEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown extends Module {

    public Setting<Boolean> guiMove = this.register(new Setting<Boolean>("GuiMove", true));
    public Setting<Boolean> noSlow = this.register(new Setting<Boolean>("NoSlow", true));
    public Setting<Boolean> soulSand = this.register(new Setting<Boolean>("SoulSand", true));
    private static NoSlowDown INSTANCE = new NoSlowDown();
    private static KeyBinding[] keys = new KeyBinding[]{NoSlowDown.mc.gameSettings.keyBindForward, NoSlowDown.mc.gameSettings.keyBindBack, NoSlowDown.mc.gameSettings.keyBindLeft, NoSlowDown.mc.gameSettings.keyBindRight, NoSlowDown.mc.gameSettings.keyBindJump, NoSlowDown.mc.gameSettings.keyBindSprint};

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoSlowDown getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlowDown();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        block2: {
            block3: {
                if (!this.guiMove.getValue().booleanValue()) break block2;
                if (!(NoSlowDown.mc.currentScreen instanceof GuiOptions) && !(NoSlowDown.mc.currentScreen instanceof GuiVideoSettings) && !(NoSlowDown.mc.currentScreen instanceof GuiScreenOptionsSounds) && !(NoSlowDown.mc.currentScreen instanceof GuiContainer) && !(NoSlowDown.mc.currentScreen instanceof GuiIngameMenu)) break block3;
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)Keyboard.isKeyDown((int)bind.getKeyCode()));
                }
                break block2;
            }
            if (NoSlowDown.mc.currentScreen != null) break block2;
            for (KeyBinding bind : keys) {
                if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
            }
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.noSlow.getValue().booleanValue() && NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onKeyEvent(KeyEvent event) {
        if (this.guiMove.getValue().booleanValue() && event.getStage() == 0 && !(NoSlowDown.mc.currentScreen instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }
}

