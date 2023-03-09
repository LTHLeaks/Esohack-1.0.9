package com.esoterik.client.features.modules.player;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.UpdateWalkingPlayerEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastPlace extends Module {
    
    private Setting<Boolean> all;
    private Setting<Boolean> obby;
    private Setting<Boolean> enderChests;
    private Setting<Boolean> crystals;
    private Setting<Boolean> exp;
    private Setting<Boolean> feetExp;
    private Setting<Boolean> fastCrystal;
    private BlockPos mousePos;

    public FastPlace() {
        super("FastPlace", "Fast everything.", Category.PLAYER, true, false, false);
        this.all = (Setting<Boolean>)this.register(new Setting("All", false));
        this.obby = (Setting<Boolean>)this.register(new Setting("Obsidian", false, v -> !this.all.getValue()));
        this.enderChests = (Setting<Boolean>)this.register(new Setting("EnderChests", false, v -> !this.all.getValue()));
        this.crystals = (Setting<Boolean>)this.register(new Setting("Crystals", false, v -> !this.all.getValue()));
        this.exp = (Setting<Boolean>)this.register(new Setting("Experience", false, v -> !this.all.getValue()));
        this.feetExp = (Setting<Boolean>)this.register(new Setting("ExpFeet", false));
        this.fastCrystal = (Setting<Boolean>)this.register(new Setting("PacketCrystal", false));
        this.mousePos = null;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.feetExp.getValue()) {
            final boolean mainHand = FastPlace.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE;
            final boolean offHand = FastPlace.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
            if (FastPlace.mc.gameSettings.keyBindUseItem.isKeyDown() && ((FastPlace.mc.player.getActiveHand() == EnumHand.MAIN_HAND && mainHand) || (FastPlace.mc.player.getActiveHand() == EnumHand.OFF_HAND && offHand))) {
                esohack.rotationManager.lookAtVec3d(FastPlace.mc.player.getPositionVector());
            }
        }
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (this.fastCrystal.getValue() && FastPlace.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            final boolean offhand = FastPlace.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            if (offhand || FastPlace.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                final RayTraceResult result = FastPlace.mc.objectMouseOver;
                if (result == null) {
                    return;
                }
                switch (result.typeOfHit) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = FastPlace.mc.objectMouseOver.getBlockPos();
                        break;
                    }
                    case ENTITY: {
                        if (this.mousePos == null) {
                            break;
                        }
                        final Entity entity = result.entityHit;
                        if (entity != null && this.mousePos.equals((Object)new BlockPos(entity.posX, entity.posY - 1.0, entity.posZ))) {
                            FastPlace.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}