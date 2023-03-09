package com.esoterik.client.manager;

import com.esoterik.client.esohack;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.gui.font.CustomFont;
import com.esoterik.client.features.modules.client.FontMod;
import com.esoterik.client.util.Timer;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class TextManager extends Feature {

    private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private final Timer idleTimer = new Timer();
    private boolean idling;

    public TextManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        FontMod cFont = esohack.moduleManager.getModuleByClass(FontMod.class);
        try {
            this.setFontRenderer(new Font(cFont.fontName.getValue(), (int)cFont.fontStyle.getValue(), cFont.fontSize.getValue()), cFont.antiAlias.getValue(), cFont.fractionalMetrics.getValue());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (esohack.moduleManager.isModuleEnabled(FontMod.class)) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
    }

    public int getStringWidth(String text) {
        if (esohack.moduleManager.isModuleEnabled(FontMod.class)) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        if (esohack.moduleManager.isModuleEnabled(FontMod.class)) {
            String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return TextManager.mc.fontRenderer.FONT_HEIGHT;
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = TextManager.mc.displayWidth;
        this.scaledHeight = TextManager.mc.displayHeight;
        this.scaleFactor = 1;
        boolean flag = mc.isUnicode();
        int i = TextManager.mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        double scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceil((double)scaledWidthD);
        this.scaledHeight = MathHelper.ceil((double)scaledHeightD);
    }

    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}

