package com.esoterik.client.manager;

import com.esoterik.client.event.events.Render2DEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.*;
import com.esoterik.client.features.modules.combat.*;
import com.esoterik.client.features.modules.misc.ChatModifier;
import com.esoterik.client.features.modules.misc.DonkeyNotifier;
import com.esoterik.client.features.modules.misc.RPC;
import com.esoterik.client.features.modules.misc.Spammer;
import com.esoterik.client.features.modules.movement.*;
import com.esoterik.client.features.modules.player.*;
import com.esoterik.client.features.modules.render.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager extends Feature {

    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    boolean hasRun = false;

    public void init() {
        modules.add(new AutoTrap());
        modules.add(new Offhand());
        modules.add(new Criticals());
        modules.add(new AutoArmor());
        modules.add(new Surround());
        modules.add(new HoleFiller());
        modules.add(new AutoGondal());
        modules.add(new ChatModifier());
        modules.add(new Spammer());
        modules.add(new DonkeyNotifier());
        modules.add(new Velocity());
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new Sprint());
        modules.add(new NoSlowDown());
        modules.add(new Speed());
        modules.add(new FakePlayer());
        modules.add(new Speedmine());
        modules.add(new Replenish());
        modules.add(new MCP());
        modules.add(new FastPlace());
        modules.add(new Burrow());
        modules.add(new NoRender());
        modules.add(new Fullbright());
        modules.add(new Nametags());
        modules.add(new CameraClip());
        modules.add(new ViewModel());
        modules.add(new ESP());
        modules.add(new HoleESP());
        modules.add(new CrystalChams());
        modules.add(new Gondal());
        modules.add(new Colors());
        modules.add(new Notifications());
        modules.add(new HUD());
        modules.add(new ClickGui());
        modules.add(new Managers());
        modules.add(new Components());
        modules.add(new RPC());
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public void enableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).enable();
        }
    }

    public void disableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module)module).isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }


    public void onLoad() {
        modules.stream().filter(Module::listening).forEach(((EventBus)MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof esohackGui) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    public static void onServerUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onServerUpdate);
    }
}

