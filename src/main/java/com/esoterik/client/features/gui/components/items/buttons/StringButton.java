package com.esoterik.client.features.gui.components.items.buttons;

import com.esoterik.client.esohack;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.client.ClickGui;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.SoundEvent;

public class StringButton extends Button {

    private Setting setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? esohack.colorManager.getColorWithAlpha(esohack.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : esohack.colorManager.getColorWithAlpha(esohack.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        if (this.isListening) {
            esohack.textManager.drawStringWithShadow(this.currentString.getString() + esohack.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - (float)esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            esohack.textManager.drawStringWithShadow((this.setting.getName().equals("Buttons") ? "Buttons " : (this.setting.getName().equals("Prefix") ? "Prefix  \u00a77" : "")) + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float)esohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
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
            switch (keyCode) {
                case 1: {
                    break;
                }
                case 28: {
                    this.enterString();
                    break;
                }
                case 14: {
                    this.setString(StringButton.removeLastChar(this.currentString.getString()));
                    break;
                }
                default: {
                    if (!ChatAllowedCharacters.isAllowedCharacter((char)typedChar)) break;
                    this.setString(this.currentString.getString() + typedChar);
                }
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        super.onMouseClick();
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

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    public static class CurrentString {
        private String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}

