package dev.chimera;

import dev.chimera.amalthea.EventBus;
import dev.chimera.amalthea.events.EventSystemTest;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.gui.components.ShreckntButton;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.gui.InGameOverlay;
import dev.chimera.gui.components.Label;
import dev.chimera.gui.components.Panel;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChimeraClient implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("chimera-client");
	public static final EventBus EVENT_BUS = new EventBus();

	public static final InGameOverlay OVERLAY = new InGameOverlay();

	public static int test = 0;
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Chimera sussers!");

		//GUI test
		ClientTickEvents.START_CLIENT_TICK.register((minecraftClient) -> {
			// Im gonna have to make a onrender event instead of using start client tick
			OVERLAY.SCREEN.children.clear();
			for(int i = 0; i < 100; i+=16) {
				Label label = new Label();
				float diff = (float)Math.abs(i-test);
				int grey = Math.min(255,(int)( (diff/50F)*255F ));
				label.color = new Color(grey, grey, grey);
				label.content = "Example GUI";
				label.size.width.setValue("100%");
				label.position.y.value = i;
				OVERLAY.SCREEN.children.add(label);
			}
			test+=1;
			if(test > 100)
				test = 0;

			//System.out.println(test);

		});

		//Events test
		EventSystemTest test = new EventSystemTest();

		EVENT_BUS.post("string test");
		EVENT_BUS.post("sussy", "Works!!");

		new ModuleInitializer().initializeModules();

		ClientTickEvents.START_CLIENT_TICK.register((startTick) -> {
			EVENT_BUS.post("start", new TickEvent());
		});

		ClientTickEvents.END_CLIENT_TICK.register((endTick) -> {
			EVENT_BUS.post("end", new TickEvent());
		});
	}

	public Event<?> event = ScreenEvents.AFTER_INIT;
}