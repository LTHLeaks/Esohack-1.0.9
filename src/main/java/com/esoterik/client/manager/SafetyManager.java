package com.esoterik.client.manager;

import com.esoterik.client.features.Feature;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SafetyManager extends Feature implements Runnable {

    private final Timer syncTimer = new Timer();
    private ScheduledExecutorService service;
    private final AtomicBoolean SAFE = new AtomicBoolean(false);

    @Override
    public void run() {
    }

    public void doSafetyCheck() {
        if (!SafetyManager.fullNullCheck()) {
            boolean safe = true;
            EntityPlayer closest = Managers.getInstance().safety.getValue() != false ? EntityUtil.getClosestEnemy(18.0) : null;
            EntityPlayer entityPlayer = closest;
            if (Managers.getInstance().safety.getValue().booleanValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            ArrayList crystals = new ArrayList(SafetyManager.mc.world.loadedEntityList);
            for (Object crystal : crystals) {
                if (!(crystal instanceof EntityEnderCrystal) || !((double)DamageUtil.calculateDamage((BlockPos) crystal, (Entity)SafetyManager.mc.player) > 4.0) || closest != null && !(closest.getDistanceSq((BlockPos) crystal) < 40.0)) continue;
                safe = false;
                break;
            }
            if (safe) {
                for (BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false, Managers.getInstance().oneDot15.getValue())) {
                    if (!((double)DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.player) > 4.0) || closest != null && !(closest.getDistanceSq(pos) < 40.0)) continue;
                    safe = false;
                    break;
                }
            }
            this.SAFE.set(safe);
        }
    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "\u00a7aSecure";
        }
        return "\u00a7cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE.get();
    }

    public ScheduledExecutorService getService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().safetyCheck.getValue().intValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}

