package com.esoterik.client.mixin.mixins;

import com.esoterik.client.event.events.RenderEntityModelEvent;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.modules.render.CrystalChams;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Shadow
    @Final
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    private static ResourceLocation glint;

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (CrystalChams.INSTANCE.isEnabled()) {
            if (CrystalChams.INSTANCE.scaleMap.containsKey((Object)((EntityEnderCrystal)entity))) {
                GlStateManager.scale((float)CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue(), (float)CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue(), (float)CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue());
            } else {
                GlStateManager.scale((float)CrystalChams.INSTANCE.scale.getValue().floatValue(), (float)CrystalChams.INSTANCE.scale.getValue().floatValue(), (float)CrystalChams.INSTANCE.scale.getValue().floatValue());
            }
            if (CrystalChams.INSTANCE.wireframe.getValue().booleanValue()) {
                RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                CrystalChams.INSTANCE.onRenderModel(event);
            }
        }
        if (CrystalChams.INSTANCE.isEnabled()) {
            Color visibleColor;
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.5f);
            GL11.glEnable((int)2960);
            if (CrystalChams.INSTANCE.rainbow.getValue().booleanValue()) {
                Color rainbowColor1 = CrystalChams.INSTANCE.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(CrystalChams.INSTANCE.speed.getValue() * 100, 0, (float)CrystalChams.INSTANCE.saturation.getValue().intValue() / 100.0f, (float)CrystalChams.INSTANCE.brightness.getValue().intValue() / 100.0f));
                Color rainbowColor = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), CrystalChams.INSTANCE.alpha.getValue(), true);
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)rainbowColor.getRed() / 255.0f), (float)((float)rainbowColor.getGreen() / 255.0f), (float)((float)rainbowColor.getBlue() / 255.0f), (float)((float)CrystalChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
            } else if (CrystalChams.INSTANCE.xqz.getValue().booleanValue() && CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                Color hiddenColor = CrystalChams.INSTANCE.colorSync.getValue() != false ? EntityUtil.getColor(entity, CrystalChams.INSTANCE.hiddenRed.getValue(), CrystalChams.INSTANCE.hiddenGreen.getValue(), CrystalChams.INSTANCE.hiddenBlue.getValue(), CrystalChams.INSTANCE.hiddenAlpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChams.INSTANCE.hiddenRed.getValue(), CrystalChams.INSTANCE.hiddenGreen.getValue(), CrystalChams.INSTANCE.hiddenBlue.getValue(), CrystalChams.INSTANCE.hiddenAlpha.getValue(), true);
                visibleColor = CrystalChams.INSTANCE.colorSync.getValue() != false ? EntityUtil.getColor(entity, CrystalChams.INSTANCE.red.getValue(), CrystalChams.INSTANCE.green.getValue(), CrystalChams.INSTANCE.blue.getValue(), CrystalChams.INSTANCE.alpha.getValue(), true) : EntityUtil.getColor(entity, CrystalChams.INSTANCE.red.getValue(), CrystalChams.INSTANCE.green.getValue(), CrystalChams.INSTANCE.blue.getValue(), CrystalChams.INSTANCE.alpha.getValue(), true);
                Color color = visibleColor;
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)hiddenColor.getRed() / 255.0f), (float)((float)hiddenColor.getGreen() / 255.0f), (float)((float)hiddenColor.getBlue() / 255.0f), (float)((float)CrystalChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)CrystalChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                visibleColor = CrystalChams.INSTANCE.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entity, CrystalChams.INSTANCE.red.getValue(), CrystalChams.INSTANCE.green.getValue(), CrystalChams.INSTANCE.blue.getValue(), CrystalChams.INSTANCE.alpha.getValue(), true);
                Color color = visibleColor;
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                }
                GL11.glEnable((int)10754);
                GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), (float)((float)CrystalChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalChams.INSTANCE.throughWalls.getValue().booleanValue()) {
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                }
            }
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalChams.INSTANCE.scaleMap.containsKey((Object)((EntityEnderCrystal)entity))) {
            GlStateManager.scale((float)(1.0f / CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()), (float)(1.0f / CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()), (float)(1.0f / CrystalChams.INSTANCE.scaleMap.get((Object)((EntityEnderCrystal)entity)).floatValue()));
        } else {
            GlStateManager.scale((float)(1.0f / CrystalChams.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalChams.INSTANCE.scale.getValue().floatValue()), (float)(1.0f / CrystalChams.INSTANCE.scale.getValue().floatValue()));
        }
    }
}

