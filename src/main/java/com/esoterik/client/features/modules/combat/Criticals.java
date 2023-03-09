package com.esoterik.client.features.modules.combat;

import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Criticals extends Module {

    private Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKET));
    public Setting<Boolean> noDesync = this.register(new Setting<Boolean>("NoDesync", true));
    private Setting<Integer> packets = this.register(new Setting<Object>("Packets", 2, 1, 4, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    private Setting<Integer> desyncDelay = this.register(new Setting<Object>("DesyncDelay", 10, 0, 500, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    public Setting<Boolean> cancelFirst = this.register(new Setting<Boolean>("CancelFirst32k", true));
    public Setting<Integer> delay32k = this.register(new Setting<Object>("32kDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> this.cancelFirst.getValue()));
    private Timer timer = new Timer();
    private Timer timer32k = new Timer();
    private boolean firstCanceled = false;
    private boolean resetTimer = false;

    public Criticals() {
        super("Criticals", "Scores criticals for you", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (!this.cancelFirst.getValue().booleanValue()) {
            this.firstCanceled = false;
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            if (this.firstCanceled) {
                this.timer32k.reset();
                this.resetTimer = true;
                this.timer.setMs(this.desyncDelay.getValue() + 1);
                this.firstCanceled = false;
                return;
            }
            if (this.resetTimer && !this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                return;
            }
            if (this.resetTimer && this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                this.resetTimer = false;
            }
            if (!this.timer.passedMs(this.desyncDelay.getValue().intValue())) {
                return;
            }
            if (!(!Criticals.mc.player.onGround || Criticals.mc.gameSettings.keyBindJump.isKeyDown() || !(packet.getEntityFromWorld((World)Criticals.mc.world) instanceof EntityLivingBase) && this.noDesync.getValue().booleanValue() || Criticals.mc.player.isInWater() || Criticals.mc.player.isInLava())) {
                if (this.mode.getValue() == Mode.PACKET) {
                    switch (this.packets.getValue()) {
                        case 1: {
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + (double)0.1f, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            break;
                        }
                        case 2: {
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.1E-5, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            break;
                        }
                        case 3: {
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0125, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            break;
                        }
                        case 4: {
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 4.0E-6, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.0E-6, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                            Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer());
                            Criticals.mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld((World)Criticals.mc.world)));
                            break;
                        }
                    }
                } else {
                    Criticals.mc.player.jump();
                }
                this.timer.reset();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        JUMP,
        PACKET;

    }
}

