package com.esoterik.client.features.modules.player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.InventoryUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.ItemStack;
import java.util.Map;

public class Replenish extends Module {

    private final Setting<Integer> threshold;
    private final Setting<Integer> replenishments;
    private final Setting<Integer> updates;
    private final Setting<Integer> actions;
    private final Setting<Boolean> pauseInv;
    private final Setting<Boolean> putBack;
    private final Timer timer;
    private final Timer replenishTimer;
    private Map<Integer, ItemStack> hotbar;
    private final Queue<InventoryUtil.Task> taskList;

    public Replenish() {
        super("Replenish", "Replenishes your hotbar", Category.PLAYER, false, false, false);
        this.threshold = (Setting<Integer>)this.register(new Setting("Threshold", 0, 0, 63));
        this.replenishments = (Setting<Integer>)this.register(new Setting("RUpdates", 0, 0, 1000));
        this.updates = (Setting<Integer>)this.register(new Setting("HBUpdates", 100, 0, 1000));
        this.actions = (Setting<Integer>)this.register(new Setting("Actions", 2, 1, 30));
        this.pauseInv = (Setting<Boolean>)this.register(new Setting("PauseInv", true));
        this.putBack = (Setting<Boolean>)this.register(new Setting("PutBack", true));
        this.timer = new Timer();
        this.replenishTimer = new Timer();
        this.hotbar = new ConcurrentHashMap<Integer, ItemStack>();
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    }

    @Override
    public void onUpdate() {
        if (Replenish.mc.currentScreen instanceof GuiContainer && (!(Replenish.mc.currentScreen instanceof GuiInventory) || this.pauseInv.getValue())) {
            return;
        }
        if (this.timer.passedMs(this.updates.getValue())) {
            this.mapHotbar();
        }
        if (this.replenishTimer.passedMs(this.replenishments.getValue())) {
            for (int i = 0; i < this.actions.getValue(); ++i) {
                final InventoryUtil.Task task = this.taskList.poll();
                if (task != null) {
                    task.run();
                }
            }
            this.replenishTimer.reset();
        }
    }

    @Override
    public void onDisable() {
        this.hotbar.clear();
    }

    @Override
    public void onLogout() {
        this.onDisable();
    }

    private void mapHotbar() {
        final Map<Integer, ItemStack> map = new ConcurrentHashMap<Integer, ItemStack>();
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Replenish.mc.player.inventory.getStackInSlot(i);
            map.put(i, stack);
        }
        if (this.hotbar.isEmpty()) {
            this.hotbar = map;
            return;
        }
        final Map<Integer, Integer> fromTo = new ConcurrentHashMap<Integer, Integer>();
        for (final Map.Entry<Integer, ItemStack> hotbarItem : map.entrySet()) {
            final ItemStack stack2 = hotbarItem.getValue();
            final Integer slotKey = hotbarItem.getKey();
            if (slotKey != null && stack2 != null && (stack2.isEmpty() || stack2.getItem() == Items.AIR || (stack2.getCount() <= this.threshold.getValue() && stack2.getCount() < stack2.getMaxStackSize()))) {
                ItemStack previousStack = hotbarItem.getValue();
                if (stack2.isEmpty() || stack2.getItem() != Items.AIR) {
                    previousStack = this.hotbar.get(slotKey);
                }
                if (previousStack == null || previousStack.isEmpty() || previousStack.getItem() == Items.AIR) {
                    continue;
                }
                final int replenishSlot = this.getReplenishSlot(previousStack);
                if (replenishSlot == -1) {
                    continue;
                }
                fromTo.put(replenishSlot, InventoryUtil.convertHotbarToInv(slotKey));
            }
        }
        if (!fromTo.isEmpty()) {
            for (final Map.Entry<Integer, Integer> slotMove : fromTo.entrySet()) {
                this.taskList.add(new InventoryUtil.Task(slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task(slotMove.getValue()));
                this.taskList.add(new InventoryUtil.Task(slotMove.getKey()));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
        this.hotbar = map;
    }

    private int getReplenishSlot(final ItemStack stack) {
        final AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (final Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getKey() < 36 && InventoryUtil.areStacksCompatible(stack, entry.getValue())) {
                slot.set(entry.getKey());
                return slot.get();
            }
        }
        return slot.get();
    }
}