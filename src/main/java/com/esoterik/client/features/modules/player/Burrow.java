package com.esoterik.client.features.modules.player;

import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.BurrowUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Burrow extends Module {

    private final Setting<Integer> offset = this.register(new Setting<Integer>("Offset", 3, -5, 5));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OBBY));
    private BlockPos originalPos;
    private int oldSlot = -1;
    Block returnBlock = null;

    public Burrow() {
        super("Cripple", "TPs you into a block", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        switch (this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.OBSIDIAN;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.ENDER_CHEST;
                break;
            }
            case CHEST: {
                this.returnBlock = Blocks.CHEST;
            }
        }
        if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals((Object)this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case OBBY: {
                if (BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) break;
                Command.sendMessage("Can't find obby in hotbar!");
                this.toggle();
                break;
            }
            case ECHEST: {
                if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1) break;
                Command.sendMessage("Can't find echest in hotbar!");
                this.toggle();
                break;
            }
            case CHEST: {
                if (BurrowUtil.findHotbarBlock(BlockChest.class) != -1) break;
                Command.sendMessage("Can't find chest in hotbar!");
                this.toggle();
            }
        }
        switch (this.mode.getValue()) {
            case OBBY: {
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockObsidian.class));
                break;
            }
            case ECHEST: {
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockEnderChest.class));
                break;
            }
            case CHEST: {
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockChest.class));
            }
        }
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
        BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (double)this.offset.getValue().intValue(), Burrow.mc.player.posZ, false));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.player.setSneaking(false);
        BurrowUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals((Object)Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static enum Mode {
        OBBY,
        ECHEST,
        CHEST;

    }
}

