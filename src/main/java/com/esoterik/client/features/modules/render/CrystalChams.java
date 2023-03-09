package com.esoterik.client.features.modules.render;

import com.esoterik.client.event.events.RenderEntityModelEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrystalChams extends Module {

    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("ColorSync", false));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<Boolean> throughWalls = this.register(new Setting<Boolean>("ThroughWalls", true));
    public Setting<Boolean> wireframe = this.register(new Setting<Boolean>("Outline", false));
    public Setting<Float> lineWidth = this.register(new Setting<Float>("OutlineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    public Setting<Boolean> wireframeThroughWalls = this.register(new Setting<Boolean>("OutlineThroughWalls", true));
    public Setting<Integer> speed = this.register(new Setting<Object>("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Boolean> xqz = this.register(new Setting<Object>("XQZ", Boolean.valueOf(false), v -> this.rainbow.getValue() == false && this.throughWalls.getValue() != false));
    public Setting<Integer> saturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> brightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Map<EntityEnderCrystal, Float> scaleMap = new ConcurrentHashMap<EntityEnderCrystal, Float>();
    public Setting<Integer> hiddenRed = this.register(new Setting<Object>("Hidden Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.xqz.getValue() != false && this.rainbow.getValue() == false));
    public Setting<Integer> hiddenGreen = this.register(new Setting<Object>("Hidden Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.xqz.getValue() != false && this.rainbow.getValue() == false));
    public Setting<Integer> hiddenBlue = this.register(new Setting<Object>("Hidden Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.xqz.getValue() != false && this.rainbow.getValue() == false));
    public Setting<Integer> hiddenAlpha = this.register(new Setting<Object>("Hidden Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.xqz.getValue() != false && this.rainbow.getValue() == false));
    public Setting<Float> scale = this.register(new Setting<Float>("Scale", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(2.0f)));
    public static CrystalChams INSTANCE = new CrystalChams();

    public CrystalChams() {
        super("CrystalChams", "Renders players through walls.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static CrystalChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrystalChams();
        }
        return INSTANCE;
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue().booleanValue()) {
            return;
        }
        Color color = this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), false);
        boolean fancyGraphics = CrystalChams.mc.gameSettings.fancyGraphics;
        CrystalChams.mc.gameSettings.fancyGraphics = false;
        float gamma = CrystalChams.mc.gameSettings.gammaSetting;
        CrystalChams.mc.gameSettings.gammaSetting = 10000.0f;
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6913);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        if (this.wireframeThroughWalls.getValue().booleanValue()) {
            GL11.glDisable((int)2929);
        }
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.color((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}

