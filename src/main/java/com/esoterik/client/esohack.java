package com.esoterik.client;

import com.esoterik.client.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="esohack", name="esohack", version="1.0.5")
public class esohack {
    public static final String MODID = "esohack";
    public static final String MODNAME = "esohack";
    public static final String MODVER = "1.0.5";
    public static final Logger LOGGER = LogManager.getLogger((String)"esohack");
    private static String name = "esohack";
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static TimerManager timerManager;
    public static PacketManager packetManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    private static boolean unloaded;
    @Mod.Instance
    public static esohack INSTANCE;

    public static String getName() {
        return name;
    }

    public static void setName(String newName) {
        name = newName;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle((String)"esohack - v.1.0.5");
        esohack.load();
    }

    public static void load() {
        LOGGER.info("\n\nLoading esohack 1.0.5");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        totemPopManager = new TotemPopManager();
        timerManager = new TimerManager();
        packetManager = new PacketManager();
        serverManager = new ServerManager();
        colorManager = new ColorManager();
        textManager = new TextManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        commandManager = new CommandManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        fileManager = new FileManager();
        friendManager = new FriendManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        holeManager = new HoleManager();
        notificationManager = new NotificationManager();
        safetyManager = new SafetyManager();
        LOGGER.info("Initialized Managers");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        totemPopManager.init();
        timerManager.init();
        LOGGER.info("esohack initialized!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading esohack 1.0.5");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        esohack.onUnload();
        eventManager = null;
        holeManager = null;
        timerManager = null;
        moduleManager = null;
        totemPopManager = null;
        serverManager = null;
        colorManager = null;
        textManager = null;
        speedManager = null;
        rotationManager = null;
        positionManager = null;
        commandManager = null;
        configManager = null;
        fileManager = null;
        friendManager = null;
        potionManager = null;
        inventoryManager = null;
        notificationManager = null;
        safetyManager = null;
        LOGGER.info("esohack unloaded!\n");
    }

    public static void reload() {
        esohack.unload(false);
        esohack.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(esohack.configManager.config.replaceFirst("esohack/", ""));
            moduleManager.onUnloadPost();
            timerManager.unload();
            unloaded = true;
        }
    }

    static {
        unloaded = false;
    }
}