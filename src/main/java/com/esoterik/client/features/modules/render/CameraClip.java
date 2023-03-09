package com.esoterik.client.features.modules.render;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class CameraClip extends Module {

    public Setting<Boolean> extend = this.register(new Setting<Boolean>("Extend", false));
    public Setting<Double> distance = this.register(new Setting<Object>("Distance", 10.0, 0.0, 50.0, v -> this.extend.getValue(), "By how much you want to extend the distance."));
    private static CameraClip INSTANCE = new CameraClip();

    public CameraClip() {
        super("CameraClip", "Makes your Camera clip.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static CameraClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }
}

