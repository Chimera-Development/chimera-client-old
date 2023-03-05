package dev.chimera;

import dev.chimera.amalthea.EventBus;
import dev.chimera.amalthea.events.EventSystemTest;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.ModuleInitializer;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

public class ChimeraClient implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("chimera-client");
	public static final EventBus EVENT_BUS = new EventBus();
	public static MinecraftClient mc;
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Chimera sussers!");
		EventSystemTest test = new EventSystemTest();
		mc = MinecraftClient.getInstance();
		initializeRPC();
		try {
			EVENT_BUS.postEvent("Works!!");
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		try{
			EVENT_BUS.postEventToListener("idPush", EventListeners.a);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		try {
			EVENT_BUS.postEvent("Works!!");
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		new ModuleInitializer().initializeModules();

		ClientTickEvents.START_CLIENT_TICK.register((startTick) -> {
			try {
				EVENT_BUS.postEvent(new TickEvent.Start());
			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register((endTick) -> {
			try {
				EVENT_BUS.postEvent(new TickEvent.End());
			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});

	}
	public void initializeRPC(){
		if (!DiscordIPC.start(1081668565633093662L, () -> System.out.println("Logged in account: " + DiscordIPC.getUser().username))) {
			System.out.println("Failed to start Discord IPC");
			return;
		}

		RichPresence presence = new RichPresence();
		presence.setDetails("Playing in " + Utils.getWorldName());
		presence.setState("yes");
		presence.setLargeImage("chimera-logo", "Chimera Client");
		presence.setSmallImage("chimera-logo", "heheheha");
		presence.setStart(Instant.now().getEpochSecond());
		DiscordIPC.setActivity(presence);
	}
	public Event<?> event = ScreenEvents.AFTER_INIT;
}