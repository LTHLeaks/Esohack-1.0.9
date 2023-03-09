package com.esoterik.client.features.modules.render;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraft.entity.passive.EntityBat;
import net.minecraftforge.client.event.RenderLivingEvent;
import java.util.Iterator;
import java.util.HashMap;
import net.minecraft.world.BossInfo;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.BossInfoClient;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.world.GameType;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.entity.item.EntityItem;

public class NoRender extends Module {

    public Setting<Boolean> fire;
    public Setting<Boolean> portal;
    public Setting<Boolean> pumpkin;
    public Setting<Boolean> totemPops;
    public Setting<Boolean> items;
    public Setting<Boolean> nausea;
    public Setting<Boolean> hurtcam;
    public Setting<Fog> fog;
    public Setting<Boolean> noWeather;
    public Setting<Boss> boss;
    public Setting<Float> scale;
    public Setting<Boolean> bats;
    public Setting<NoArmor> noArmor;
    public Setting<Skylight> skylight;
    public Setting<Boolean> barriers;
    public Setting<Boolean> blocks;
    private static NoRender INSTANCE;

    public NoRender() {
        super("NoRender", "Allows you to stop rendering stuff", Category.RENDER, true, false, false);
        this.fire = (Setting<Boolean>)this.register(new Setting("Fire", false, "Removes the portal overlay."));
        this.portal = (Setting<Boolean>)this.register(new Setting("Portal", false, "Removes the portal overlay."));
        this.pumpkin = (Setting<Boolean>)this.register(new Setting("Pumpkin", false, "Removes the pumpkin overlay."));
        this.totemPops = (Setting<Boolean>)this.register(new Setting("TotemPop", false, "Removes the Totem overlay."));
        this.items = (Setting<Boolean>)this.register(new Setting("Items", false, "Removes items on the ground."));
        this.nausea = (Setting<Boolean>)this.register(new Setting("Nausea", false, "Removes Portal Nausea."));
        this.hurtcam = (Setting<Boolean>)this.register(new Setting("HurtCam", false, "Removes shaking after taking damage."));
        this.fog = (Setting<Fog>)this.register(new Setting("Fog", Fog.NONE, "Removes Fog."));
        this.noWeather = (Setting<Boolean>)this.register(new Setting("Weather", false, "AntiWeather"));
        this.boss = (Setting<Boss>)this.register(new Setting("BossBars", Boss.NONE, "Modifies the bossbars."));
        this.scale = (Setting<Float>)this.register(new Setting("Scale", 0.0f, 0.5f, 1.0f, v -> this.boss.getValue() == Boss.MINIMIZE || this.boss.getValue() != Boss.STACK, "Scale of the bars."));
        this.bats = (Setting<Boolean>)this.register(new Setting("Bats", false, "Removes bats."));
        this.noArmor = (Setting<NoArmor>)this.register(new Setting("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
        this.skylight = (Setting<Skylight>)this.register(new Setting("Skylight", Skylight.NONE));
        this.barriers = (Setting<Boolean>)this.register(new Setting("Barriers", false, "Barriers"));
        this.blocks = (Setting<Boolean>)this.register(new Setting("Blocks", false, "Blocks"));
        this.setInstance();
    }

    private void setInstance() {
        NoRender.INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.items.getValue()) {
            NoRender.mc.world.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::setDead);
        }
        if (this.noWeather.getValue() && NoRender.mc.world.isRaining()) {
            NoRender.mc.world.setRainStrength(0.0f);
        }
    }

    public void doVoidFogParticles(final int posX, final int posY, final int posZ) {
        final int i = 32;
        final Random random = new Random();
        final ItemStack itemstack = NoRender.mc.player.getHeldItemMainhand();
        final boolean flag = !this.barriers.getValue() || (NoRender.mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER));
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(final int x, final int y, final int z, final int offset, final Random random, final boolean holdingBarrier, final BlockPos.MutableBlockPos pos) {
        final int i = x + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        final int j = y + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        final int k = z + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        pos.setPos(i, j, k);
        final IBlockState iblockstate = NoRender.mc.world.getBlockState((BlockPos)pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, (World)NoRender.mc.world, (BlockPos)pos, random);
        if (!holdingBarrier && iblockstate.getBlock() == Blocks.BARRIER) {
            NoRender.mc.world.spawnParticle(EnumParticleTypes.BARRIER, (double)(i + 0.5f), (double)(j + 0.5f), (double)(k + 0.5f), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderLiving(final RenderLivingEvent.Pre<?> event) {
        if (this.bats.getValue() && event.getEntity() instanceof EntityBat) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaySound(final PlaySoundAtEntityEvent event) {
        if ((this.bats.getValue() && event.getSound().equals(SoundEvents.ENTITY_BAT_AMBIENT)) || event.getSound().equals(SoundEvents.ENTITY_BAT_DEATH) || event.getSound().equals(SoundEvents.ENTITY_BAT_HURT) || event.getSound().equals(SoundEvents.ENTITY_BAT_LOOP) || event.getSound().equals(SoundEvents.ENTITY_BAT_TAKEOFF)) {
            event.setVolume(0.0f);
            event.setPitch(0.0f);
            event.setCanceled(true);
        }
    }

    public static NoRender getInstance() {
        if (NoRender.INSTANCE == null) {
            NoRender.INSTANCE = new NoRender();
        }
        return NoRender.INSTANCE;
    }

    static {
        NoRender.INSTANCE = new NoRender();
    }

    public enum Skylight
    {
        NONE,
        WORLD,
        ENTITY,
        ALL;
    }

    public enum Fog
    {
        NONE,
        AIR,
        NOFOG;
    }

    public enum Boss
    {
        NONE,
        REMOVE,
        STACK,
        MINIMIZE;
    }

    public enum NoArmor
    {
        NONE,
        ALL,
        HELMET;
    }

    public static class Pair<T, S>
    {
        private T key;
        private S value;

        public Pair(final T key, final S value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return this.key;
        }

        public S getValue() {
            return this.value;
        }

        public void setKey(final T key) {
            this.key = key;
        }

        public void setValue(final S value) {
            this.value = value;
        }
    }
}