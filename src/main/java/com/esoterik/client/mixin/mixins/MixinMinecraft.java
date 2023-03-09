package com.esoterik.client.mixin.mixins;

import com.esoterik.client.esohack;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Inject(method={"Lnet/minecraft/client/Minecraft;getLimitFramerate()I"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLimitFramerateHook(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (Managers.getInstance().unfocusedCpu.getValue().booleanValue() && !Display.isActive()) {
                callbackInfoReturnable.setReturnValue(Managers.getInstance().cpuFPS.getValue());
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    @Redirect(method={"runGameLoop"}, at=@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;sync(I)V"))
    public void syncHook(int maxFps) {
        if (Managers.getInstance().betterFrames.getValue().booleanValue()) {
            Display.sync((int)Managers.getInstance().betterFPS.getValue());
        } else {
            Display.sync((int)maxFps);
        }
    }

    @Redirect(method={"run"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Redirect(method={"runTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
        NoRender.getInstance().doVoidFogParticles(x, y, z);
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    public void shutdownHook(CallbackInfo info) {
        this.unload();
    }

    private void unload() {
        System.out.println("Shutting down: saving configuration");
        esohack.onUnload();
        System.out.println("Configuration saved.");
    }
}

