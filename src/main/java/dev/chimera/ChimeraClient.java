package dev.chimera;

import com.mojang.blaze3d.systems.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventBus;
import dev.chimera.amalthea.events.EventSystemTest;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.ExampleModule;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.modules.combat.KillAuraModule;
//import dev.chimera.modules.common.ClickGUIModule;
import dev.chimera.modules.common.FarmAuraModule;
import dev.chimera.modules.player.FlightModule;
import dev.chimera.modules.player.NoFallModule;
//import dev.chimera.nemean.GuiLayer;
//import dev.chimera.nemean.elements.Gui;
import dev.chimera.nemean.ScreenRenderingAttempt;
import dev.chimera.nemean.components.Trollface;
import dev.chimera.sisyphus.AddonInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger("chimera-client");
    public static final EventBus EVENT_BUS = new EventBus();

    public static int test = 0;

    @Override
    public void onInitializeClient() {

        Trollface trollface = new Trollface();
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            MinecraftClient.getInstance().getProfiler().push("ChimeraGUI");

            trollface.render(matrixStack);

            MinecraftClient.getInstance().getProfiler().pop();
        });

    }
}