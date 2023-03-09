package com.esoterik.client.features.modules.render;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class ViewModel extends Module {

    public Setting<Float> sizeX = this.register(new Setting<Float>("SizeX", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
    public Setting<Float> sizeY = this.register(new Setting<Float>("SizeY", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
    public Setting<Float> sizeZ = this.register(new Setting<Float>("SizeZ", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
    public Setting<Float> rotationX = this.register(new Setting<Float>("rotationX", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    public Setting<Float> rotationY = this.register(new Setting<Float>("rotationY", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    public Setting<Float> rotationZ = this.register(new Setting<Float>("rotationZ", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    public Setting<Float> positionX = this.register(new Setting<Float>("positionX", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f)));
    public Setting<Float> positionY = this.register(new Setting<Float>("positionY", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f)));
    public Setting<Float> positionZ = this.register(new Setting<Float>("positionZ", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f)));
    public Setting<Float> itemFOV = this.register(new Setting<Float>("ItemFOV", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
    private static ViewModel INSTANCE = new ViewModel();

    public ViewModel() {
        super("Viewmodel", "Changes to the viewmodel.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ViewModel getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }
}

