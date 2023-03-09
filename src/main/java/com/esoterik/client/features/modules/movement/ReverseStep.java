package com.esoterik.client.features.modules.movement;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class ReverseStep extends Module {

    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 10, 1, 20));

    public ReverseStep() {
        super("ReverseStep", "Go down", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        block5: {
            block4: {
                if (ReverseStep.fullNullCheck()) break block4;
                if (ReverseStep.mc.player.isInWater()) break block4;
                if (ReverseStep.mc.player.isInLava()) break block4;
                if (!ReverseStep.mc.player.isOnLadder()) break block5;
            }
            return;
        }
        if (ReverseStep.mc.player.onGround) {
            ReverseStep.mc.player.motionY -= (double)((float)this.speed.getValue().intValue() / 10.0f);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ReverseStep.mc.player.motionY = 0.0;
    }
}

