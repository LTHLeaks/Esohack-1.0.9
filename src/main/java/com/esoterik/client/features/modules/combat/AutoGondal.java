package com.esoterik.client.features.modules.combat;

import java.util.HashSet;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.ClientEvent;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.event.events.UpdateWalkingPlayerEvent;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.setting.Bind;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Items;

import java.util.concurrent.*;

import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import java.awt.Color;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import net.minecraft.entity.Entity;
import java.util.Queue;

public class AutoGondal extends Module {
    
    private final Setting<Settings> setting;
    public Setting<Raytrace> raytrace;
    public Setting<Boolean> place;
    public Setting<Integer> placeDelay;
    public Setting<Float> placeRange;
    public Setting<Float> minDamage;
    public Setting<Float> maximumSelfDamage;
    public Setting<Integer> wasteAmount;
    public Setting<Boolean> wasteMinDmgCount;
    public Setting<Float> facePlace;
    public Setting<Float> placetrace;
    public Setting<Boolean> antiSurround;
    public Setting<Boolean> limitFacePlace;
    public Setting<Boolean> oneDot15;
    public Setting<Boolean> doublePop;
    public Setting<Float> popDamage;
    public Setting<Integer> popTime;
    public Setting<Boolean> explode;
    public Setting<Switch> switchMode;
    public Setting<Integer> breakDelay;
    public Setting<Float> breakRange;
    public Setting<Integer> packets;
    public Setting<Float> breaktrace;
    public Setting<Boolean> manual;
    public Setting<Boolean> manualMinDmg;
    public Setting<Integer> manualBreak;
    public Setting<Boolean> sync;
    public Setting<Boolean> instant;
    public Setting<Boolean> render;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> box;
    public Setting<Boolean> outline;
    public Setting<Boolean> text;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    public Setting<Float> range;
    public Setting<Target> targetMode;
    public Setting<Integer> minArmor;
    private final Setting<Integer> switchCooldown;
    public Setting<AutoSwitch> autoSwitch;
    public Setting<Bind> switchBind;
    public Setting<Boolean> offhandSwitch;
    public Setting<Boolean> switchBack;
    public Setting<Boolean> lethalSwitch;
    public Setting<Boolean> mineSwitch;
    public Setting<Rotate> rotate;
    public Setting<Boolean> suicide;
    public Setting<Boolean> webAttack;
    public Setting<Boolean> fullCalc;
    public Setting<Boolean> extraSelfCalc;
    public Setting<AntiFriendPop> antiFriendPop;
    public Setting<Boolean> noCount;
    public Setting<Boolean> calcEvenIfNoDamage;
    public Setting<Boolean> predictFriendDmg;
    public Setting<Logic> logic;
    public Setting<Boolean> doubleMap;
    public Setting<DamageSync> damageSync;
    public Setting<Integer> damageSyncTime;
    public Setting<Float> dropOff;
    public Setting<Integer> confirm;
    public Setting<Boolean> syncedFeetPlace;
    public Setting<Boolean> fullSync;
    public Setting<Boolean> syncCount;
    public Setting<Boolean> hyperSync;
    public Setting<Boolean> gigaSync;
    public Setting<Boolean> syncySync;
    public Setting<Boolean> enormousSync;
    public Setting<Boolean> holySync;
    private final Setting<Integer> eventMode;
    private final Setting<ThreadMode> threadMode;
    public Setting<Integer> threadDelay;
    public Setting<Integer> syncThreads;
    public Setting<Boolean> altPosition;
    public Setting<Boolean> doublePopOnDamage;
    private Queue<Entity> attackList;
    private Map<Entity, Float> crystalMap;
    private final Timer switchTimer;
    private final Timer manualTimer;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer syncTimer;
    public static EntityPlayer target;
    private Entity efficientTarget;
    private double currentDamage;
    private double renderDamage;
    private double lastDamage;
    private boolean didRotation;
    private boolean switching;
    private BlockPos placePos;
    private BlockPos renderPos;
    private boolean mainHand;
    private boolean rotating;
    private boolean offHand;
    private int crystalCount;
    private int minDmgCount;
    private int lastSlot;
    private float yaw;
    private float pitch;
    private BlockPos webPos;
    private final Timer renderTimer;
    private BlockPos lastPos;
    public static Set<BlockPos> placedPos;
    public static Set<BlockPos> brokenPos;
    private boolean posConfirmed;
    private boolean foundDoublePop;
    private final AtomicBoolean shouldInterrupt;
    private ScheduledExecutorService executor;
    private final Timer syncroTimer;
    private Thread thread;
    private EntityPlayer currentSyncTarget;
    private BlockPos syncedPlayerPos;
    private BlockPos syncedCrystalPos;
    private static AutoGondal instance;
    private final Map<EntityPlayer, Timer> totemPops;

    public AutoGondal() {
        super("AutoGondal", "Best CA on the market", Category.COMBAT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", Settings.PLACE));
        this.raytrace = (Setting<Raytrace>)this.register(new Setting("Raytrace", Raytrace.NONE, v -> this.setting.getValue() == Settings.MISC));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", true, v -> this.setting.getValue() == Settings.PLACE));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("PlaceDelay", 0, 0, 500, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", 6.0f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", 4.0f, 0.1f, 20.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.maximumSelfDamage = (Setting<Float>)this.register(new Setting("MaxSelfDamage", 4.0f, 0.1f, 20.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.wasteAmount = (Setting<Integer>)this.register(new Setting("WasteAmount", 1, 1, 5, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.wasteMinDmgCount = (Setting<Boolean>)this.register(new Setting("CountMinDmg", true, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.facePlace = (Setting<Float>)this.register(new Setting("FacePlace", 8.0f, 0.1f, 20.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.placetrace = (Setting<Float>)this.register(new Setting("Placetrace", 6.0f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.BREAK));
        this.antiSurround = (Setting<Boolean>)this.register(new Setting("AntiSurround", false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.limitFacePlace = (Setting<Boolean>)this.register(new Setting("LimitFacePlace", true, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15", false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.doublePop = (Setting<Boolean>)this.register(new Setting("AntiTotem", false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.popDamage = (Setting<Float>)this.register(new Setting("PopDamage", 4.0f, 0.0f, 6.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue()));
        this.popTime = (Setting<Integer>)this.register(new Setting("PopTime", 500, 0, 1000, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue()));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", true, v -> this.setting.getValue() == Settings.BREAK));
        this.switchMode = (Setting<Switch>)this.register(new Setting("Attack", Switch.BREAKSLOT, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("BreakDelay", 0, 0, 500, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", 6.0f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.packets = (Setting<Integer>)this.register(new Setting("Packets", 1, 1, 6, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breaktrace = (Setting<Float>)this.register(new Setting("Breaktrace", 6.0f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.PLACE));
        this.manual = (Setting<Boolean>)this.register(new Setting("Manual", false, v -> this.setting.getValue() == Settings.BREAK));
        this.manualMinDmg = (Setting<Boolean>)this.register(new Setting("ManMinDmg", false, v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue()));
        this.manualBreak = (Setting<Integer>)this.register(new Setting("ManualDelay", 500, 0, 500, v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue()));
        this.sync = (Setting<Boolean>)this.register(new Setting("Sync", true, v -> this.setting.getValue() == Settings.BREAK && (this.explode.getValue() || this.manual.getValue())));
        this.instant = (Setting<Boolean>)this.register(new Setting("Predict", false, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue()));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", true, v -> this.setting.getValue() == Settings.RENDER));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", false, v -> this.setting.getValue() == Settings.RENDER));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.text = (Setting<Boolean>)this.register(new Setting("Text", false, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.5f, 0.1f, 5.0f, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("Range", 12.0f, 0.1f, 20.0f, v -> this.setting.getValue() == Settings.MISC));
        this.targetMode = (Setting<Target>)this.register(new Setting("Target", Target.CLOSEST, v -> this.setting.getValue() == Settings.MISC));
        this.minArmor = (Setting<Integer>)this.register(new Setting("MinArmor", 0, 0, 125, v -> this.setting.getValue() == Settings.MISC));
        this.switchCooldown = (Setting<Integer>)this.register(new Setting("Cooldown", 500, 0, 1000, v -> this.setting.getValue() == Settings.MISC));
        this.autoSwitch = (Setting<AutoSwitch>)this.register(new Setting("Switch", AutoSwitch.TOGGLE, v -> this.setting.getValue() == Settings.MISC));
        this.switchBind = (Setting<Bind>)this.register(new Setting("SwitchBind", new Bind(-1), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() == AutoSwitch.TOGGLE));
        this.offhandSwitch = (Setting<Boolean>)this.register(new Setting("Offhand", false, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.switchBack = (Setting<Boolean>)this.register(new Setting("Switchback", false, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE && this.offhandSwitch.getValue()));
        this.lethalSwitch = (Setting<Boolean>)this.register(new Setting("LethalSwitch", false, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.mineSwitch = (Setting<Boolean>)this.register(new Setting("MineSwitch", false, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.rotate = (Setting<Rotate>)this.register(new Setting("Rotate", Rotate.OFF, v -> this.setting.getValue() == Settings.MISC));
        this.suicide = (Setting<Boolean>)this.register(new Setting("Suicide", false, v -> this.setting.getValue() == Settings.MISC));
        this.webAttack = (Setting<Boolean>)this.register(new Setting("WebAttack", false, v -> this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE));
        this.fullCalc = (Setting<Boolean>)this.register(new Setting("ExtraCalc", false, v -> this.setting.getValue() == Settings.MISC));
        this.extraSelfCalc = (Setting<Boolean>)this.register(new Setting("MinSelfDmg", true, v -> this.setting.getValue() == Settings.MISC));
        this.antiFriendPop = (Setting<AntiFriendPop>)this.register(new Setting("FriendPop", AntiFriendPop.NONE, v -> this.setting.getValue() == Settings.MISC));
        this.noCount = (Setting<Boolean>)this.register(new Setting("AntiCount", false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK)));
        this.calcEvenIfNoDamage = (Setting<Boolean>)this.register(new Setting("BigFriendCalc", false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.targetMode.getValue() != Target.DAMAGE));
        this.predictFriendDmg = (Setting<Boolean>)this.register(new Setting("PredictFriend", false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.instant.getValue()));
        this.logic = (Setting<Logic>)this.register(new Setting("Logic", Logic.BREAKPLACE, v -> this.setting.getValue() == Settings.DEV));
        this.doubleMap = (Setting<Boolean>)this.register(new Setting("DoubleMap", false, v -> this.setting.getValue() == Settings.DEV && this.logic.getValue() == Logic.PLACEBREAK));
        this.damageSync = (Setting<DamageSync>)this.register(new Setting("DamageSync", DamageSync.NONE, v -> this.setting.getValue() == Settings.DEV));
        this.damageSyncTime = (Setting<Integer>)this.register(new Setting("SyncDelay", 500, 0, 1000, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.dropOff = (Setting<Float>)this.register(new Setting("DropOff", 5.0f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() == DamageSync.BREAK));
        this.confirm = (Setting<Integer>)this.register(new Setting("Confirm", 250, 0, 1000, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.syncedFeetPlace = (Setting<Boolean>)this.register(new Setting("FeetSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.fullSync = (Setting<Boolean>)this.register(new Setting("FullSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.syncCount = (Setting<Boolean>)this.register(new Setting("SyncCount", true, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.hyperSync = (Setting<Boolean>)this.register(new Setting("HyperSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.gigaSync = (Setting<Boolean>)this.register(new Setting("GigaSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.syncySync = (Setting<Boolean>)this.register(new Setting("SyncySync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.enormousSync = (Setting<Boolean>)this.register(new Setting("EnormousSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.holySync = (Setting<Boolean>)this.register(new Setting("UnbelievableSync", false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", 3, 1, 3, v -> this.setting.getValue() == Settings.DEV));
        this.threadMode = (Setting<ThreadMode>)this.register(new Setting("Thread", ThreadMode.NONE, v -> this.setting.getValue() == Settings.DEV));
        this.threadDelay = (Setting<Integer>)this.register(new Setting("ThreadDelay", 25, 1, 1000, v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
        this.syncThreads = (Setting<Integer>)this.register(new Setting("SyncThreads", 1000, 1, 10000, v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
        this.altPosition = (Setting<Boolean>)this.register(new Setting("AltPos", false, v -> this.setting.getValue() == Settings.DEV));
        this.doublePopOnDamage = (Setting<Boolean>)this.register(new Setting("DamagePop", false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue() && this.targetMode.getValue() == Target.DAMAGE));
        this.attackList = new ConcurrentLinkedQueue<Entity>();
        this.crystalMap = new HashMap<Entity, Float>();
        this.switchTimer = new Timer();
        this.manualTimer = new Timer();
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.syncTimer = new Timer();
        this.efficientTarget = null;
        this.currentDamage = 0.0;
        this.renderDamage = 0.0;
        this.lastDamage = 0.0;
        this.didRotation = false;
        this.switching = false;
        this.placePos = null;
        this.renderPos = null;
        this.mainHand = false;
        this.rotating = false;
        this.offHand = false;
        this.crystalCount = 0;
        this.minDmgCount = 0;
        this.lastSlot = -1;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.webPos = null;
        this.renderTimer = new Timer();
        this.lastPos = null;
        this.posConfirmed = false;
        this.foundDoublePop = false;
        this.shouldInterrupt = new AtomicBoolean(false);
        this.syncroTimer = new Timer();
        this.totemPops = new ConcurrentHashMap<EntityPlayer, Timer>();
        AutoGondal.instance = this;
    }

    public static AutoGondal getInstance() {
        if (AutoGondal.instance == null) {
            AutoGondal.instance = new AutoGondal();
        }
        return AutoGondal.instance;
    }

    @Override
    public void onTick() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 3) {
            this.doAutoGondal();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
        else if (this.eventMode.getValue() == 2) {
            this.doAutoGondal();
        }
    }

    @Override
    public void onUpdate() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 1) {
            this.doAutoGondal();
        }
    }

    @Override
    public void onToggle() {
        AutoGondal.brokenPos.clear();
        AutoGondal.placedPos.clear();
        this.totemPops.clear();
        this.rotating = false;
    }

    @Override
    public void onDisable() {
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
        if (this.executor != null) {
            this.executor.shutdown();
        }
    }

    @Override
    public void onEnable() {
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.switching) {
            return "§aSwitch";
        }
        if (AutoGondal.target != null) {
            return AutoGondal.target.getName();
        }
        return null;
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue() != Rotate.OFF && this.rotating && this.eventMode.getValue() != 2 && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            /*packet.yaw = this.yaw;
            packet.pitch = this.pitch;*/
            this.rotating = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (this.explode.getValue() && this.instant.getValue() && event.getPacket() instanceof SPacketSpawnObject && (this.syncedCrystalPos == null || !this.syncedFeetPlace.getValue() || this.damageSync.getValue() == DamageSync.NONE)) {
            final SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() == 51) {
                final BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                if (AutoGondal.placedPos.contains(pos.down())) {
                    if (this.predictFriendDmg.getValue() && (this.antiFriendPop.getValue() == AntiFriendPop.BREAK || this.antiFriendPop.getValue() == AntiFriendPop.ALL)) {
                        for (final EntityPlayer friend : AutoGondal.mc.world.playerEntities) {
                            if (friend != null && !AutoGondal.mc.player.equals((Object)friend) && friend.getDistanceSq(pos) <= MathUtil.square(this.range.getValue() + this.placeRange.getValue())) {
                                if (!esohack.friendManager.isFriend(friend)) {
                                    continue;
                                }
                                if (DamageUtil.calculateDamage(pos, (Entity)friend) > EntityUtil.getHealth((Entity)friend) + 0.5) {
                                    return;
                                }
                                continue;
                            }
                        }
                    }
                    /*final CPacketUseEntity attackPacket = new CPacketUseEntity();
                    attackPacket.entityId = packet.getEntityID();
                    attackPacket.action = CPacketUseEntity.Action.ATTACK;*/
                    //AutoGondal.mc.player.connection.sendPacket((Packet)attackPacket);
                }
            }
        }
        else if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion packet2 = event.getPacket();
            final BlockPos pos = new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ()).down();
            if (this.damageSync.getValue() == DamageSync.PLACE) {
                if (AutoGondal.placedPos.contains(pos)) {
                    AutoGondal.placedPos.remove(pos);
                    this.posConfirmed = true;
                }
            }
            else if (this.damageSync.getValue() == DamageSync.BREAK && AutoGondal.brokenPos.contains(pos)) {
                AutoGondal.brokenPos.remove(pos);
                this.posConfirmed = true;
            }
        }
        else if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities packet3 = event.getPacket();
            for (final int id : packet3.getEntityIDs()) {
                final Entity entity = AutoGondal.mc.world.getEntityByID(id);
                if (entity instanceof EntityEnderCrystal) {
                    AutoGondal.brokenPos.remove(new BlockPos(entity.getPositionVector()).down());
                    AutoGondal.placedPos.remove(new BlockPos(entity.getPositionVector()).down());
                }
            }
        }
        else if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet4 = event.getPacket();
            if (packet4.getOpCode() == 35 && packet4.getEntity((World)AutoGondal.mc.world) instanceof EntityPlayer) {
                this.totemPops.put((EntityPlayer)packet4.getEntity((World)AutoGondal.mc.world), new Timer().reset());
            }
        }
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if ((this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC) && this.renderPos != null && this.render.getValue() && (this.box.getValue() || this.text.getValue() || this.outline.getValue())) {
            RenderUtil.drawBoxESP(this.renderPos, ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue()) {
                RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent keyInputEvent) {
        if (Keyboard.getEventKeyState() && !(AutoGondal.mc.currentScreen instanceof esohackGui) && this.switchBind.getValue().getKey() == Keyboard.getEventKey()) {
            if (this.switchBack.getValue().booleanValue() && this.offhandSwitch.getValue().booleanValue() && this.offHand) {
                Offhand offhand = esohack.moduleManager.getModuleByClass(Offhand.class);
                if (offhand.isOff()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
                } else {
                    offhand.setMode(Offhand.Mode2.TOTEMS);
                    offhand.doSwitch();
                }
                return;
            }
            this.switching = !this.switching;
        }
    }

    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && this.isEnabled() && (event.getSetting().equals(this.threadDelay) || event.getSetting().equals(this.threadMode))) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            if (this.thread != null) {
                this.shouldInterrupt.set(true);
            }
        }
    }

    private void processMultiThreading() {
        if (this.isOff()) {
            return;
        }
        if (this.threadMode.getValue() == ThreadMode.POOL) {
            this.handlePool();
        }
        else if (this.threadMode.getValue() == ThreadMode.WHILE) {
            this.handleWhile();
        }
    }

    private void handlePool() {
        if (this.executor == null || this.executor.isTerminated() || this.executor.isShutdown() || this.syncroTimer.passedMs(this.syncThreads.getValue())) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            this.executor = this.getExecutor();
            this.syncroTimer.reset();
        }
    }

    private void handleWhile() {
        if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncroTimer.passedMs(this.syncThreads.getValue())) {
            if (this.thread == null) {
                this.thread = new Thread(RAutoGondal.getInstance(this));
            }
            else if (this.syncroTimer.passedMs(this.syncThreads.getValue()) && !this.shouldInterrupt.get()) {
                this.shouldInterrupt.set(true);
                this.syncroTimer.reset();
                return;
            }
            if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                this.thread = new Thread(RAutoGondal.getInstance(this));
            }
            if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                try {
                    this.thread.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.syncroTimer.reset();
            }
        }
    }

    private ScheduledExecutorService getExecutor() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(RAutoGondal.getInstance(this), 0L, this.threadDelay.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }

    public void doAutoGondal() {
        if (this.check()) {
            switch (this.logic.getValue()) {
                case PLACEBREAK: {
                    this.placeCrystal();
                    if (this.doubleMap.getValue()) {
                        this.mapCrystals();
                    }
                    this.breakCrystal();
                    break;
                }
                case BREAKPLACE: {
                    this.breakCrystal();
                    this.placeCrystal();
                    break;
                }
            }
            this.manualBreaker();
        }
    }

    private boolean check() {
        if (fullNullCheck()) {
            return false;
        }
        if (this.syncTimer.passedMs(this.damageSyncTime.getValue())) {
            this.currentSyncTarget = null;
            this.syncedCrystalPos = null;
            this.syncedPlayerPos = null;
        }
        else if (this.syncySync.getValue() && this.syncedCrystalPos != null) {
            this.posConfirmed = true;
        }
        this.foundDoublePop = false;
        if (this.renderTimer.passedMs(500L)) {
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.mainHand = (AutoGondal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
        this.offHand = (AutoGondal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        this.currentDamage = 0.0;
        this.placePos = null;
        if (this.lastSlot != AutoGondal.mc.player.inventory.currentItem || AutoTrap.isPlacing || Surround.isPlacing) {
            this.lastSlot = AutoGondal.mc.player.inventory.currentItem;
            this.switchTimer.reset();
        }
        if (this.offHand || this.mainHand) {
            this.switching = false;
        }
        if ((!this.offHand && !this.mainHand && this.switchMode.getValue() == Switch.BREAKSLOT && !this.switching) || !DamageUtil.canBreakWeakness((EntityPlayer)AutoGondal.mc.player) || !this.switchTimer.passedMs(this.switchCooldown.getValue())) {
            this.renderPos = null;
            AutoGondal.target = null;
            return this.rotating = false;
        }
        if (this.mineSwitch.getValue() && AutoGondal.mc.gameSettings.keyBindAttack.isKeyDown() && (this.switching || this.autoSwitch.getValue() == AutoSwitch.ALWAYS) && AutoGondal.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoGondal.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            this.switchItem();
        }
        this.mapCrystals();
        if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(this.confirm.getValue())) {
            this.syncTimer.setMs(this.damageSyncTime.getValue() + 1);
        }
        return true;
    }

    private void mapCrystals() {
        this.efficientTarget = null;
        if (this.packets.getValue() != 1) {
            this.attackList = new ConcurrentLinkedQueue<Entity>();
            this.crystalMap = new HashMap<Entity, Float>();
        }
        this.crystalCount = 0;
        this.minDmgCount = 0;
        Entity maxCrystal = null;
        float maxDamage = 0.5f;
        for (final Entity crystal : AutoGondal.mc.world.loadedEntityList) {
            if (crystal instanceof EntityEnderCrystal && this.isValid(crystal)) {
                if (this.syncedFeetPlace.getValue() && crystal.getPosition().down().equals((Object)this.syncedCrystalPos) && this.damageSync.getValue() != DamageSync.NONE) {
                    ++this.minDmgCount;
                    ++this.crystalCount;
                    if (this.syncCount.getValue()) {
                        this.minDmgCount = this.wasteAmount.getValue() + 1;
                        this.crystalCount = this.wasteAmount.getValue() + 1;
                    }
                    if (this.hyperSync.getValue()) {
                        maxCrystal = null;
                        break;
                    }
                    continue;
                }
                else {
                    boolean count = false;
                    boolean countMin = false;
                    float selfDamage = 0.0f;
                    if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        if (this.altPosition.getValue()) {
                            selfDamage = DamageUtil.calculateDamageAlt(crystal, (Entity)AutoGondal.mc.player);
                        }
                        else {
                            selfDamage = DamageUtil.calculateDamage(crystal, (Entity)AutoGondal.mc.player);
                        }
                    }
                    if (selfDamage + 0.5 < EntityUtil.getHealth((Entity)AutoGondal.mc.player) || !DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        final Entity beforeCrystal = maxCrystal;
                        final float beforeDamage = maxDamage;
                        for (final EntityPlayer player : AutoGondal.mc.world.playerEntities) {
                            if (player.getDistanceSq(crystal) <= MathUtil.square(this.range.getValue())) {
                                if (EntityUtil.isValid((Entity)player, this.range.getValue() + this.breakRange.getValue())) {
                                    final float damage = DamageUtil.calculateDamage(crystal, (Entity)player);
                                    if (damage <= selfDamage && (damage <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && damage <= EntityUtil.getHealth((Entity)player)) {
                                        continue;
                                    }
                                    if (damage > maxDamage) {
                                        maxDamage = damage;
                                        maxCrystal = crystal;
                                    }
                                    if (this.packets.getValue() == 1) {
                                        if (damage >= this.minDamage.getValue() || !this.wasteMinDmgCount.getValue()) {
                                            count = true;
                                        }
                                        countMin = true;
                                    }
                                    else {
                                        if (this.crystalMap.get(crystal) != null && this.crystalMap.get(crystal) >= damage) {
                                            continue;
                                        }
                                        this.crystalMap.put(crystal, damage);
                                    }
                                }
                                else {
                                    if ((this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.ALL) || !esohack.friendManager.isFriend(player.getName())) {
                                        continue;
                                    }
                                    final float damage = DamageUtil.calculateDamage(crystal, (Entity)player);
                                    if (damage <= EntityUtil.getHealth((Entity)player) + 0.5) {
                                        continue;
                                    }
                                    maxCrystal = beforeCrystal;
                                    maxDamage = beforeDamage;
                                    this.crystalMap.remove(crystal);
                                    if (this.noCount.getValue()) {
                                        count = false;
                                        countMin = false;
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (!countMin) {
                        continue;
                    }
                    ++this.minDmgCount;
                    if (!count) {
                        continue;
                    }
                    ++this.crystalCount;
                }
            }
        }
        if (this.damageSync.getValue() == DamageSync.BREAK && (maxDamage > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue()) || this.damageSync.getValue() == DamageSync.NONE)) {
            this.lastDamage = maxDamage;
        }
        if (this.enormousSync.getValue() && this.syncedFeetPlace.getValue() && this.damageSync.getValue() != DamageSync.NONE && this.syncedCrystalPos != null) {
            if (this.syncCount.getValue()) {
                this.minDmgCount = this.wasteAmount.getValue() + 1;
                this.crystalCount = this.wasteAmount.getValue() + 1;
            }
            return;
        }
        if (this.webAttack.getValue() && this.webPos != null) {
            if (AutoGondal.mc.player.getDistanceSq(this.webPos.up()) > MathUtil.square(this.breakRange.getValue())) {
                this.webPos = null;
            }
            else {
                for (final Entity entity : AutoGondal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.webPos.up()))) {
                    if (entity instanceof EntityEnderCrystal) {
                        this.attackList.add(entity);
                        this.efficientTarget = entity;
                        this.webPos = null;
                        this.lastDamage = 0.5;
                        return;
                    }
                }
            }
        }
        if (this.manual.getValue() && this.manualMinDmg.getValue() && AutoGondal.mc.gameSettings.keyBindUseItem.isKeyDown() && ((this.offHand && AutoGondal.mc.player.getActiveHand() == EnumHand.OFF_HAND) || (this.mainHand && AutoGondal.mc.player.getActiveHand() == EnumHand.MAIN_HAND)) && maxDamage < this.minDamage.getValue()) {
            this.efficientTarget = null;
            return;
        }
        if (this.packets.getValue() == 1) {
            this.efficientTarget = maxCrystal;
        }
        else {
            this.crystalMap = MathUtil.sortByValue(this.crystalMap, true);
            for (final Map.Entry<Entity, Float> entry : this.crystalMap.entrySet()) {
                final Entity crystal2 = entry.getKey();
                final float damage2 = entry.getValue();
                if (damage2 >= this.minDamage.getValue() || !this.wasteMinDmgCount.getValue()) {
                    ++this.crystalCount;
                }
                this.attackList.add(crystal2);
                ++this.minDmgCount;
            }
        }
    }

    private void placeCrystal() {
        int crystalLimit = this.wasteAmount.getValue();
        if (this.placeTimer.passedMs(this.placeDelay.getValue()) && this.place.getValue() && (this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC || (this.switchMode.getValue() == Switch.BREAKSLOT && this.switching))) {
            if ((this.offHand || this.mainHand || (this.switchMode.getValue() != Switch.ALWAYS && !this.switching)) && this.crystalCount >= crystalLimit && (!this.antiSurround.getValue() || this.lastPos == null || !this.lastPos.equals((Object)this.placePos))) {
                return;
            }
            this.calculateDamage(this.getTarget(this.targetMode.getValue() == Target.UNSAFE));
            if (AutoGondal.target != null && this.placePos != null) {
                if (!this.offHand && !this.mainHand && this.autoSwitch.getValue() != AutoSwitch.NONE && (this.currentDamage > this.minDamage.getValue() || (this.lethalSwitch.getValue() && EntityUtil.getHealth((Entity)AutoGondal.target) < this.facePlace.getValue())) && !this.switchItem()) {
                    return;
                }
                if (this.currentDamage < this.minDamage.getValue() && this.limitFacePlace.getValue()) {
                    crystalLimit = 1;
                }
                if ((this.offHand || this.mainHand || this.autoSwitch.getValue() != AutoSwitch.NONE) && (this.crystalCount < crystalLimit || (this.antiSurround.getValue() && this.lastPos != null && this.lastPos.equals((Object)this.placePos))) && (this.currentDamage > this.minDamage.getValue() || this.minDmgCount < crystalLimit) && this.currentDamage >= 1.0 && (DamageUtil.isArmorLow(AutoGondal.target, this.minArmor.getValue()) || EntityUtil.getHealth((Entity)AutoGondal.target) < this.facePlace.getValue() || this.currentDamage > this.minDamage.getValue())) {
                    final float damageOffset = (this.damageSync.getValue() == DamageSync.BREAK) ? (this.dropOff.getValue() - 5.0f) : 0.0f;
                    boolean syncflag = false;
                    if (this.syncedFeetPlace.getValue() && this.placePos.equals((Object)this.lastPos) && this.isEligableForFeetSync(AutoGondal.target, this.placePos) && !this.syncTimer.passedMs(this.damageSyncTime.getValue()) && AutoGondal.target.equals((Object)this.currentSyncTarget) && AutoGondal.target.getPosition().equals((Object)this.syncedPlayerPos) && this.damageSync.getValue() != DamageSync.NONE) {
                        this.syncedCrystalPos = this.placePos;
                        this.lastDamage = this.currentDamage;
                        if (this.fullSync.getValue()) {
                            this.lastDamage = 100.0;
                        }
                        syncflag = true;
                    }
                    if (syncflag || this.currentDamage - damageOffset > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue()) || this.damageSync.getValue() == DamageSync.NONE) {
                        if (!syncflag && this.damageSync.getValue() != DamageSync.BREAK) {
                            this.lastDamage = this.currentDamage;
                        }
                        this.renderPos = this.placePos;
                        this.renderDamage = this.currentDamage;
                        if (this.switchItem()) {
                            this.currentSyncTarget = AutoGondal.target;
                            this.syncedPlayerPos = AutoGondal.target.getPosition();
                            if (this.foundDoublePop) {
                                this.totemPops.put(AutoGondal.target, new Timer().reset());
                            }
                            this.rotateToPos(this.placePos);
                            AutoGondal.placedPos.add(this.placePos);
                            BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                            this.lastPos = this.placePos;
                            this.placeTimer.reset();
                            this.posConfirmed = false;
                            if (this.syncTimer.passedMs(this.damageSyncTime.getValue())) {
                                this.syncedCrystalPos = null;
                                this.syncTimer.reset();
                            }
                        }
                    }
                }
            }
            else {
                this.renderPos = null;
            }
        }
    }

    private boolean switchItem() {
        if (this.offHand || this.mainHand) {
            return true;
        }
        switch (this.autoSwitch.getValue()) {
            case NONE: {
                return false;
            }
            case TOGGLE: {
                if (!this.switching) {
                    return false;
                }
            }
            case ALWAYS: {
                if (this.doSwitch()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean doSwitch() {
        if (this.offhandSwitch.getValue().booleanValue()) {
            Offhand offhand = esohack.moduleManager.getModuleByClass(Offhand.class);
            if (offhand.isOff()) {
                Command.sendMessage("<" + this.getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
                this.switching = false;
                return false;
            }
            offhand.setMode(Offhand.Mode2.CRYSTALS);
            offhand.doSwitch();
            this.switching = false;
            return true;
        }
        if (AutoGondal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            this.mainHand = false;
        } else {
            InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class, false);
            this.mainHand = true;
        }
        this.switching = false;
        return true;
    }

    private void calculateDamage(final EntityPlayer targettedPlayer) {
        if (targettedPlayer == null && this.targetMode.getValue() != Target.DAMAGE && !this.fullCalc.getValue()) {
            return;
        }
        float maxDamage = 0.5f;
        EntityPlayer currentTarget = null;
        BlockPos currentPos = null;
        float maxSelfDamage = 0.0f;
        this.foundDoublePop = false;
        BlockPos setToAir = null;
        IBlockState state = null;
        if (this.webAttack.getValue() && targettedPlayer != null) {
            final BlockPos playerPos = new BlockPos(targettedPlayer.getPositionVector());
            final Block web = AutoGondal.mc.world.getBlockState(playerPos).getBlock();
            if (web == Blocks.WEB) {
                setToAir = playerPos;
                state = AutoGondal.mc.world.getBlockState(playerPos);
                AutoGondal.mc.world.setBlockToAir(playerPos);
            }
        }
        for (final BlockPos pos : BlockUtil.possiblePlacePositions(this.placeRange.getValue(), this.antiSurround.getValue(), this.oneDot15.getValue())) {
            if (BlockUtil.rayTracePlaceCheck(pos, (this.raytrace.getValue() == Raytrace.PLACE || this.raytrace.getValue() == Raytrace.FULL) && AutoGondal.mc.player.getDistanceSq(pos) > MathUtil.square(this.placetrace.getValue()), 1.0f)) {
                float selfDamage = -1.0f;
                if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                    selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoGondal.mc.player);
                }
                if (selfDamage + 0.5 >= EntityUtil.getHealth((Entity)AutoGondal.mc.player)) {
                    continue;
                }
                if (targettedPlayer != null) {
                    final float playerDamage = DamageUtil.calculateDamage(pos, (Entity)targettedPlayer);
                    if (this.calcEvenIfNoDamage.getValue() && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.PLACE)) {
                        boolean friendPop = false;
                        for (final EntityPlayer friend : AutoGondal.mc.world.playerEntities) {
                            if (friend != null && !AutoGondal.mc.player.equals((Object)friend) && friend.getDistanceSq(pos) <= MathUtil.square(this.range.getValue() + this.placeRange.getValue())) {
                                if (!esohack.friendManager.isFriend(friend)) {
                                    continue;
                                }
                                final float friendDamage = DamageUtil.calculateDamage(pos, (Entity)friend);
                                if (friendDamage > EntityUtil.getHealth((Entity)friend) + 0.5) {
                                    friendPop = true;
                                    break;
                                }
                                continue;
                            }
                        }
                        if (friendPop) {
                            continue;
                        }
                    }
                    if (this.isDoublePoppable(targettedPlayer, playerDamage) && (currentPos == null || targettedPlayer.getDistanceSq(pos) < targettedPlayer.getDistanceSq(currentPos))) {
                        currentTarget = targettedPlayer;
                        maxDamage = playerDamage;
                        currentPos = pos;
                        this.foundDoublePop = true;
                    }
                    else {
                        if (this.foundDoublePop) {
                            continue;
                        }
                        if ((playerDamage <= maxDamage && (!this.extraSelfCalc.getValue() || playerDamage < maxDamage || selfDamage >= maxSelfDamage)) || (playerDamage <= selfDamage && (playerDamage <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && playerDamage <= EntityUtil.getHealth((Entity)targettedPlayer))) {
                            continue;
                        }
                        maxDamage = playerDamage;
                        currentTarget = targettedPlayer;
                        currentPos = pos;
                        maxSelfDamage = selfDamage;
                    }
                }
                else {
                    final float maxDamageBefore = maxDamage;
                    final EntityPlayer currentTargetBefore = currentTarget;
                    final BlockPos currentPosBefore = currentPos;
                    final float maxSelfDamageBefore = maxSelfDamage;
                    for (final EntityPlayer player : AutoGondal.mc.world.playerEntities) {
                        if (EntityUtil.isValid((Entity)player, this.placeRange.getValue() + this.range.getValue())) {
                            final float playerDamage2 = DamageUtil.calculateDamage(pos, (Entity)player);
                            if (this.doublePopOnDamage.getValue() && this.isDoublePoppable(player, playerDamage2) && (currentPos == null || player.getDistanceSq(pos) < player.getDistanceSq(currentPos))) {
                                currentTarget = player;
                                maxDamage = playerDamage2;
                                currentPos = pos;
                                maxSelfDamage = selfDamage;
                                this.foundDoublePop = true;
                                if (this.antiFriendPop.getValue() == AntiFriendPop.BREAK) {
                                    break;
                                }
                                if (this.antiFriendPop.getValue() == AntiFriendPop.PLACE) {
                                    break;
                                }
                                continue;
                            }
                            else {
                                if (this.foundDoublePop) {
                                    continue;
                                }
                                if ((playerDamage2 <= maxDamage && (!this.extraSelfCalc.getValue() || playerDamage2 < maxDamage || selfDamage >= maxSelfDamage)) || (playerDamage2 <= selfDamage && (playerDamage2 <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && playerDamage2 <= EntityUtil.getHealth((Entity)player))) {
                                    continue;
                                }
                                maxDamage = playerDamage2;
                                currentTarget = player;
                                currentPos = pos;
                                maxSelfDamage = selfDamage;
                            }
                        }
                        else {
                            if ((this.antiFriendPop.getValue() != AntiFriendPop.ALL && this.antiFriendPop.getValue() != AntiFriendPop.PLACE) || player == null || player.getDistanceSq(pos) > MathUtil.square(this.range.getValue() + this.placeRange.getValue()) || !esohack.friendManager.isFriend(player)) {
                                continue;
                            }
                            final float friendDamage2 = DamageUtil.calculateDamage(pos, (Entity)player);
                            if (friendDamage2 > EntityUtil.getHealth((Entity)player) + 0.5) {
                                maxDamage = maxDamageBefore;
                                currentTarget = currentTargetBefore;
                                currentPos = currentPosBefore;
                                maxSelfDamage = maxSelfDamageBefore;
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        if (setToAir != null) {
            AutoGondal.mc.world.setBlockState(setToAir, state);
            this.webPos = currentPos;
        }
        AutoGondal.target = currentTarget;
        this.currentDamage = maxDamage;
        this.placePos = currentPos;
    }

    private EntityPlayer getTarget(final boolean unsafe) {
        if (this.targetMode.getValue() == Target.DAMAGE) {
            return null;
        }
        EntityPlayer currentTarget = null;
        for (final EntityPlayer player : AutoGondal.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, this.placeRange.getValue() + this.range.getValue())) {
                continue;
            }
            if (unsafe && EntityUtil.isSafe((Entity)player)) {
                continue;
            }
            if (this.minArmor.getValue() > 0 && DamageUtil.isArmorLow(player, this.minArmor.getValue())) {
                currentTarget = player;
                break;
            }
            if (currentTarget == null) {
                currentTarget = player;
            }
            else {
                if (AutoGondal.mc.player.getDistanceSq((Entity)player) >= AutoGondal.mc.player.getDistanceSq((Entity)currentTarget)) {
                    continue;
                }
                currentTarget = player;
            }
        }
        if (unsafe && currentTarget == null) {
            return this.getTarget(false);
        }
        return currentTarget;
    }

    private void breakCrystal() {
        if (this.explode.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue()) && (this.switchMode.getValue() == Switch.ALWAYS || this.mainHand || this.offHand)) {
            if (this.packets.getValue() == 1 && this.efficientTarget != null) {
                if (this.syncedFeetPlace.getValue() && this.gigaSync.getValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                this.rotateTo(this.efficientTarget);
                EntityUtil.attackEntity(this.efficientTarget, this.sync.getValue(), true);
                AutoGondal.brokenPos.add(new BlockPos(this.efficientTarget.getPositionVector()).down());
            }
            else if (!this.attackList.isEmpty()) {
                if (this.syncedFeetPlace.getValue() && this.gigaSync.getValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                for (int i = 0; i < this.packets.getValue(); ++i) {
                    final Entity entity = this.attackList.poll();
                    if (entity != null) {
                        this.rotateTo(entity);
                        EntityUtil.attackEntity(entity, this.sync.getValue(), true);
                        AutoGondal.brokenPos.add(new BlockPos(entity.getPositionVector()).down());
                    }
                }
            }
            this.breakTimer.reset();
        }
    }

    private void manualBreaker() {
        if (this.rotate.getValue() != Rotate.OFF && this.eventMode.getValue() != 2 && this.rotating) {
            if (this.didRotation) {
                final EntityPlayerSP player = AutoGondal.mc.player;
                player.rotationPitch += (float)4.0E-4;
                this.didRotation = false;
            }
            else {
                final EntityPlayerSP player2 = AutoGondal.mc.player;
                player2.rotationPitch -= (float)4.0E-4;
                this.didRotation = true;
            }
        }
        if ((this.offHand || this.mainHand) && this.manual.getValue() && this.manualTimer.passedMs(this.manualBreak.getValue()) && AutoGondal.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoGondal.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && AutoGondal.mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && AutoGondal.mc.player.inventory.getCurrentItem().getItem() != Items.BOW && AutoGondal.mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE) {
            final RayTraceResult result = AutoGondal.mc.objectMouseOver;
            if (result != null) {
                switch (result.typeOfHit) {
                    case ENTITY: {
                        final Entity entity = result.entityHit;
                        if (entity instanceof EntityEnderCrystal) {
                            EntityUtil.attackEntity(entity, this.sync.getValue(), true);
                            this.manualTimer.reset();
                            break;
                        }
                        break;
                    }
                    case BLOCK: {
                        final BlockPos mousePos = AutoGondal.mc.objectMouseOver.getBlockPos().up();
                        for (final Entity target : AutoGondal.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(mousePos))) {
                            if (target instanceof EntityEnderCrystal) {
                                EntityUtil.attackEntity(target, this.sync.getValue(), true);
                                this.manualTimer.reset();
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void rotateTo(final Entity entity) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case BREAK:
            case ALL: {
                final float[] angle = MathUtil.calcAngle(AutoGondal.mc.player.getPositionEyes(AutoGondal.mc.getRenderPartialTicks()), entity.getPositionVector());
                if (this.eventMode.getValue() == 2) {
                    esohack.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
                break;
            }
        }
    }

    private void rotateToPos(final BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case PLACE:
            case ALL: {
                final float[] angle = MathUtil.calcAngle(AutoGondal.mc.player.getPositionEyes(AutoGondal.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
                if (this.eventMode.getValue() == 2) {
                    esohack.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
                break;
            }
        }
    }

    private boolean isDoublePoppable(final EntityPlayer player, final float damage) {
        if (this.doublePop.getValue()) {
            final float health = EntityUtil.getHealth((Entity)player);
            if (health <= 1.0 && damage > health + 0.5 && damage <= this.popDamage.getValue()) {
                final Timer timer = this.totemPops.get(player);
                return timer == null || timer.passedMs(this.popTime.getValue());
            }
        }
        return false;
    }

    private boolean isValid(final Entity entity) {
        return entity != null && AutoGondal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breakRange.getValue()) && (this.raytrace.getValue() == Raytrace.NONE || this.raytrace.getValue() == Raytrace.PLACE || AutoGondal.mc.player.canEntityBeSeen(entity) || (!AutoGondal.mc.player.canEntityBeSeen(entity) && AutoGondal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breaktrace.getValue())));
    }

    private boolean isEligableForFeetSync(final EntityPlayer player, final BlockPos pos) {
        if (this.holySync.getValue()) {
            final BlockPos playerPos = new BlockPos(player.getPositionVector());
            for (final EnumFacing facing : EnumFacing.values()) {
                if (facing != EnumFacing.DOWN) {
                    if (facing != EnumFacing.UP) {
                        final BlockPos holyPos = playerPos.down().offset(facing);
                        if (pos.equals((Object)holyPos)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    static {
        AutoGondal.target = null;
        AutoGondal.placedPos = new HashSet<BlockPos>();
        AutoGondal.brokenPos = new HashSet<BlockPos>();
    }

    private static class RAutoGondal implements Runnable
    {
        private static RAutoGondal instance;
        private AutoGondal AutoGondal;

        public static RAutoGondal getInstance(final AutoGondal AutoGondal) {
            if (RAutoGondal.instance == null) {
                RAutoGondal.instance = new RAutoGondal();
            }
            RAutoGondal.instance.AutoGondal = AutoGondal;
            return RAutoGondal.instance;
        }

        @Override
        public void run() {
            if (this.AutoGondal.threadMode.getValue() == ThreadMode.POOL) {
                if (this.AutoGondal.isOn()) {
                    this.AutoGondal.doAutoGondal();
                }
            }
            else if (this.AutoGondal.threadMode.getValue() == ThreadMode.WHILE) {
                while (this.AutoGondal.isOn() && this.AutoGondal.threadMode.getValue() == ThreadMode.WHILE) {
                    if (this.AutoGondal.shouldInterrupt.get()) {
                        this.AutoGondal.shouldInterrupt.set(false);
                        this.AutoGondal.syncroTimer.reset();
                        this.AutoGondal.thread.interrupt();
                        break;
                    }
                    this.AutoGondal.doAutoGondal();
                    try {
                        Thread.sleep(this.AutoGondal.threadDelay.getValue());
                    }
                    catch (InterruptedException e) {
                        this.AutoGondal.thread.interrupt();
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public enum Settings
    {
        PLACE,
        BREAK,
        RENDER,
        MISC,
        DEV;
    }

    public enum DamageSync
    {
        NONE,
        PLACE,
        BREAK;
    }

    public enum Rotate
    {
        OFF,
        PLACE,
        BREAK,
        ALL;
    }

    public enum Target
    {
        CLOSEST,
        UNSAFE,
        DAMAGE;
    }

    public enum Logic
    {
        BREAKPLACE,
        PLACEBREAK;
    }

    public enum Switch
    {
        ALWAYS,
        BREAKSLOT,
        CALC;
    }

    public enum Raytrace
    {
        NONE,
        PLACE,
        BREAK,
        FULL;
    }

    public enum AutoSwitch
    {
        NONE,
        TOGGLE,
        ALWAYS;
    }

    public enum ThreadMode
    {
        NONE,
        WHILE,
        POOL;
    }

    public enum AntiFriendPop
    {
        NONE,
        PLACE,
        BREAK,
        ALL;
    }
}