package dev.chimera.client.modules.modules;

import dev.chimera.client.modules.AbstractModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static dev.chimera.client.ChimeraClient.MOD_ID;

public class Flight extends AbstractModule {
    private static KeyBinding keyBinding;



    public Flight() {
        super(new Identifier(MOD_ID, "flight"), "Flight");

        keyBinding = registerKeybind(GLFW.GLFW_KEY_G, "flight.dev.chimera.client");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                toggle();
            }

            if (client.player != null) {
                client.player.getAbilities().flying = isEnabled();

                if (client.player.age % 40 >= 0 && client.player.age % 40 <= 2)
                    client.player.networkHandler
                            .sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                                    client.player.getX(),
                                    client.player.getY(),
                                    client.player.getZ(),
                                    client.player.isOnGround()));

            }
        });
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = false;
    }


}
