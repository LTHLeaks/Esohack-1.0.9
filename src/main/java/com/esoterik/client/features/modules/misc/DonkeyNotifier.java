package com.esoterik.client.features.modules.misc;

import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;

import java.util.HashSet;
import java.util.Set;

public class DonkeyNotifier extends Module {

    private static DonkeyNotifier instance;
    private Set<Entity> entities = new HashSet<Entity>();

    public DonkeyNotifier() {
        super("DonkeyNotifier", "Notifies you when a donkey is discovered", Module.Category.MISC, true, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        this.entities.clear();
    }

    @Override
    public void onUpdate() {
        for (Entity entity : DonkeyNotifier.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityDonkey) || this.entities.contains((Object)entity)) continue;
            Command.sendMessage("Donkey Detected at: " + entity.posX + "x, " + entity.posY + "y, " + entity.posZ + "z.");
            this.entities.add(entity);
        }
    }
}