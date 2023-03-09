package com.esoterik.client.manager;

import com.esoterik.client.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryManager implements Util {

    private int recoverySlot = -1;

    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.recoverySlot == 8 ? 7 : this.recoverySlot + 1));
            InventoryManager.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.recoverySlot));
            InventoryManager.mc.player.inventory.currentItem = this.recoverySlot;
            //InventoryManager.mc.playerController.syncCurrentPlayItem();
            this.recoverySlot = -1;
        }
    }

    public void recoverSilent(int slot) {
        this.recoverySlot = slot;
    }
}

