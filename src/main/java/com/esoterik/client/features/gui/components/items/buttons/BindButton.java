package com.esoterik.client.features.gui.components.items.buttons;

import com.esoterik.client.esohack;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.client.ClickGui;
import com.esoterik.client.features.setting.Bind;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BindButton extends Button {

    private Setting setting;
    public boolean isListening;

    public BindButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? esohack.colorManager.getColorWithAlpha(((ClickGui)esohack.moduleManager.getModuleByName((String)"ClickGui")).hoverAlpha.getValue()) : esohack.colorManager.getColorWithAlpha(((ClickGui)esohack.moduleManager.getModuleByName((String)"ClickGui")).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        if (this.isListening) {
            esohack.textManager.drawStringWithShadow("Listening...", this.x + 2.3f, this.y - 1.7f - (float)esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            esohack.textManager.drawStringWithShadow(this.setting.getName() + " " + "\u00a77" + ((Bind)this.setting.getValue()).toString(), this.x + 2.3f, this.y - 1.7f - (float)esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            super.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}

