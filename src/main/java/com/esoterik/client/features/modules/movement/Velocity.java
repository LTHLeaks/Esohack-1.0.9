package com.esoterik.client.features.modules.movement;

import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.PushEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    public Setting<Boolean> noPush = this.register(new Setting<Boolean>("NoPush", true));
    public Setting<Float> horizontal = this.register(new Setting<Float>("Horizontal", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Float> vertical = this.register(new Setting<Float>("Vertical", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Boolean> explosions = this.register(new Setting<Boolean>("Explosions", true));
    public Setting<Boolean> bobbers = this.register(new Setting<Boolean>("Bobbers", true));
    public Setting<Boolean> water = this.register(new Setting<Boolean>("Water", false));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", false));
    public Setting<Boolean> ice = this.register(new Setting<Boolean>("Ice", false));
    private static Velocity INSTANCE = new Velocity();

    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Velocity getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Velocity();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onDisable() {
    }

    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.player != null) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                final SPacketEntityVelocity velocity = event.getPacket();
                if (velocity.getEntityID() == Velocity.mc.player.getEntityId()) {
                    if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue()) {
                final SPacketEntityStatus packet = event.getPacket();
                if (packet.getOpCode() == 31) {
                    final Entity entity = packet.getEntity((World)Velocity.mc.world);
                    if (entity instanceof EntityFishHook) {
                        final EntityFishHook fishHook = (EntityFishHook)entity;
                        if (fishHook.caughtEntity == Velocity.mc.player) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
            if (this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 0 && this.noPush.getValue().booleanValue() && event.entity.equals((Object)Velocity.mc.player)) {
            if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }
            event.x = -event.x * (double)this.horizontal.getValue().floatValue();
            event.y = -event.y * (double)this.vertical.getValue().floatValue();
            event.z = -event.z * (double)this.horizontal.getValue().floatValue();
        } else if (event.getStage() == 1 && this.blocks.getValue().booleanValue()) {
            event.setCanceled(true);
        } else if (event.getStage() == 2 && this.water.getValue().booleanValue() && Velocity.mc.player != null && Velocity.mc.player.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
}

