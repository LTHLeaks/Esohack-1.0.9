package com.esoterik.client.mixin.mixins;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderGlobal.class})
public abstract class MixinRenderGlobal {
    @Redirect(method={"setupTerrain"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
    public void initializeHook(ChunkRenderContainer chunkRenderContainer, double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        double y = viewEntityYIn;
        chunkRenderContainer.initialize(viewEntityXIn, y, viewEntityZIn);
    }

    @Redirect(method={"renderEntities"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
    public void setRenderPositionHook(RenderManager renderManager, double renderPosXIn, double renderPosYIn, double renderPosZIn) {
        double y;
        TileEntityRendererDispatcher.staticPlayerY = y = renderPosYIn;
        renderManager.setRenderPosition(renderPosXIn, y, renderPosZIn);
    }

    @Redirect(method={"drawSelectionBox"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
    public AxisAlignedBB offsetHook(AxisAlignedBB axisAlignedBB, double x, double y, double z) {
        double yIn = y;
        return axisAlignedBB.offset(x, y, z);
    }
}

