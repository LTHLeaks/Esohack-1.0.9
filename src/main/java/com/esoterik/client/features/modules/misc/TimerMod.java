package com.esoterik.client.features.modules.misc;

import com.esoterik.client.esohack;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.Timer;

public class TimerMod extends Module {

    public Setting<Boolean> autoOff = this.register(new Setting<Boolean>("AutoOff", false));
    public Setting<Integer> timeLimit = this.register(new Setting<Object>("Limit", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(2500), v -> this.autoOff.getValue()));
    public Setting<TimerMode> mode = this.register(new Setting<TimerMode>("Mode", TimerMode.NORMAL));
    public Setting<Float> timerSpeed = this.register(new Setting<Float>("Speed", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    public Setting<Float> fastSpeed = this.register(new Setting<Object>("Fast", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(100.0f), v -> this.mode.getValue() == TimerMode.SWITCH, "Fast Speed for switch."));
    public Setting<Integer> fastTime = this.register(new Setting<Object>("FastTime", 20, 1, 500, v -> this.mode.getValue() == TimerMode.SWITCH, "How long you want to go fast.(ms * 10)"));
    public Setting<Integer> slowTime = this.register(new Setting<Object>("SlowTime", 20, 1, 500, v -> this.mode.getValue() == TimerMode.SWITCH, "Recover from too fast.(ms * 10)"));
    public Setting<Boolean> startFast = this.register(new Setting<Object>("StartFast", Boolean.valueOf(false), v -> this.mode.getValue() == TimerMode.SWITCH));
    public float speed = 1.0f;
    private Timer timer = new Timer();
    private Timer turnOffTimer = new Timer();
    private boolean fast = false;

    public TimerMod() {
        super("Timer", "Will speed up the game.", Module.Category.PLAYER, false, true, false);
    }

    @Override
    public void onEnable() {
        this.turnOffTimer.reset();
        this.speed = this.timerSpeed.getValue().floatValue();
        if (!this.startFast.getValue().booleanValue()) {
            this.timer.reset();
        }
    }

    @Override
    public void onUpdate() {
        if (this.autoOff.getValue().booleanValue() && this.turnOffTimer.passedMs(this.timeLimit.getValue().intValue())) {
            this.disable();
            return;
        }
        if (this.mode.getValue() == TimerMode.NORMAL) {
            this.speed = this.timerSpeed.getValue().floatValue();
            return;
        }
        if (!this.fast && this.timer.passedDms(this.slowTime.getValue().intValue())) {
            this.fast = true;
            this.speed = this.fastSpeed.getValue().floatValue();
            this.timer.reset();
        }
        if (this.fast && this.timer.passedDms(this.fastTime.getValue().intValue())) {
            this.fast = false;
            this.speed = this.timerSpeed.getValue().floatValue();
            this.timer.reset();
        }
    }

    @Override
    public void onDisable() {
        this.speed = 1.0f;
        esohack.timerManager.reset();
        this.fast = false;
    }

    @Override
    public String getDisplayInfo() {
        return this.timerSpeed.getValueAsString();
    }

    public static enum TimerMode {
        NORMAL,
        SWITCH;

    }
}

