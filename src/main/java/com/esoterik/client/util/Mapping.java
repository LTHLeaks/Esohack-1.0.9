package com.esoterik.client.util;

import net.minecraft.client.Minecraft;

public class Mapping {

    public static final String tickLength = Mapping.isObfuscated() ? "tickLength" : "tickLength";
    public static final String timer = Mapping.isObfuscated() ? "timer" : "timer";
    public static final String placedBlockDirection = Mapping.isObfuscated() ? "placedBlockDirection" : "placedBlockDirection";
    public static final String playerPosLookYaw = Mapping.isObfuscated() ? "yaw" : "yaw";
    public static final String playerPosLookPitch = Mapping.isObfuscated() ? "pitch" : "pitch";
    public static final String isInWeb = Mapping.isObfuscated() ? "isInWeb" : "isInWeb";
    public static final String cPacketPlayerYaw = Mapping.isObfuscated() ? "yaw" : "yaw";
    public static final String cPacketPlayerPitch = Mapping.isObfuscated() ? "pitch" : "pitch";
    public static final String renderManagerRenderPosX = Mapping.isObfuscated() ? "renderPosX" : "renderPosX";
    public static final String renderManagerRenderPosY = Mapping.isObfuscated() ? "renderPosY" : "renderPosY";
    public static final String renderManagerRenderPosZ = Mapping.isObfuscated() ? "renderPosZ" : "renderPosZ";
    public static final String rightClickDelayTimer = Mapping.isObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer";
    public static final String sPacketEntityVelocityMotionX = Mapping.isObfuscated() ? "motionX" : "motionX";
    public static final String sPacketEntityVelocityMotionY = Mapping.isObfuscated() ? "motionY" : "motionY";
    public static final String sPacketEntityVelocityMotionZ = Mapping.isObfuscated() ? "motionZ" : "motionZ";

    public static boolean isObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") == null;
        }
        catch (Exception e) {
            return true;
        }
    }
}

