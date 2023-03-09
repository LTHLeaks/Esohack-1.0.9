package com.esoterik.client.features.gui;

import com.esoterik.client.esohack;
import com.esoterik.client.features.gui.components.Component;
import com.esoterik.client.features.gui.components.items.Item;
import com.esoterik.client.features.gui.components.items.buttons.ModuleButton;
import com.esoterik.client.features.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class esohackGui extends GuiScreen {

    private static esohackGui phobosGui;
    private final ArrayList<Component> components = new ArrayList();
    private static esohackGui INSTANCE;

    public esohackGui() {
        this.setInstance();
        this.load();
    }

    public static esohackGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new esohackGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static esohackGui getClickGui() {
        return esohackGui.getInstance();
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : esohack.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 90, 4, true){

                @Override
                public void setupItems() {
                    esohack.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton((Module)module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort((item1, item2) -> item1.getName().compareTo(item2.getName())));
    }

    public void updateModule(Module module) {
        block0: for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
                continue block0;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    static {
        INSTANCE = new esohackGui();
    }
}

