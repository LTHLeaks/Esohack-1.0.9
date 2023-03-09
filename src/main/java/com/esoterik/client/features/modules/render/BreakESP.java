package com.esoterik.client.features.modules.render;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.RenderUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class BreakESP extends Module {

    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", false));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("BoxAlpha", 85, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private BlockPos lastPos = null;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer timer = new Timer();

    public BreakESP() {
        super("BreakESP", "Highlights blocks you mine", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onTick() {
        if (!(this.currentPos == null || BreakESP.mc.world.getBlockState(this.currentPos).equals((Object)this.currentBlockState) && BreakESP.mc.world.getBlockState(this.currentPos).getBlock() != Blocks.AIR)) {
            this.currentPos = null;
            this.currentBlockState = null;
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.currentPos != null) {
            Color color = new Color(255, 255, 255, 255);
            Color readyColor = Colors.INSTANCE.isEnabled() ? Colors.INSTANCE.getCurrentColor() : new Color(125, 105, 255, 255);
            RenderUtil.drawBoxESP(this.currentPos, this.timer.passedMs((int)(2000.0f * esohack.serverManager.getTpsFactor())) ? readyColor : color, false, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }
}

