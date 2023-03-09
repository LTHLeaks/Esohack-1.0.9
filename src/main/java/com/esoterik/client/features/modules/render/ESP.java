package com.esoterik.client.features.modules.render;

import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.event.events.RenderEntityModelEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ESP extends Module {

    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OUTLINE));
    private final Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    private final Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    private final Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    private final Setting<Boolean> items = this.register(new Setting<Boolean>("Items", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("BoxAlpha", 120, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Boolean> colorFriends = this.register(new Setting<Boolean>("Friends", true));
    private final Setting<Boolean> self = this.register(new Setting<Boolean>("Self", true));
    private final Setting<Boolean> onTop = this.register(new Setting<Boolean>("onTop", true));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private static ESP INSTANCE = new ESP();

    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.items.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityItem) || !(ESP.mc.player.getDistanceSq(entity) < 2500.0)) continue;
                Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.depthMask((boolean)true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() != 0 || event.entity == null || event.entity.isInvisible() && this.invisibles.getValue() == false || this.self.getValue() == false && event.entity.equals((Object)ESP.mc.player) || this.players.getValue() == false && event.entity instanceof EntityPlayer || this.animals.getValue() == false && EntityUtil.isPassive(event.entity) || !this.mobs.getValue().booleanValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer)) {
            return;
        }
        Color color = EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        boolean fancyGraphics = ESP.mc.gameSettings.fancyGraphics;
        ESP.mc.gameSettings.fancyGraphics = false;
        float gamma = ESP.mc.gameSettings.gammaSetting;
        ESP.mc.gameSettings.gammaSetting = 10000.0f;
        if (this.onTop.getValue().booleanValue()) {
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue().floatValue());
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderTwo();
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderFive();
        } else {
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode((int)1032, (int)6913);
            } else {
                GL11.glPolygonMode((int)1028, (int)6913);
            }
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());
            GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!this.onTop.getValue().booleanValue()) {
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        try {
            ESP.mc.gameSettings.fancyGraphics = fancyGraphics;
            ESP.mc.gameSettings.gammaSetting = gamma;
        }
        catch (Exception exception) {
            // empty catch block
        }
        event.setCanceled(true);
    }

    public static enum Mode {
        WIREFRAME,
        OUTLINE;

    }
}

