package dev.chimera.client.modules.modules;

import dev.chimera.client.gui.clickgui.ClickGui;
import dev.chimera.client.modules.AbstractModule;
import dev.chimera.client.modules.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ClickGUIModule extends AbstractModule {
    private static KeyBinding keyBinding;

    private float farthestInText = 0f;

    public ClickGUIModule(Identifier moduleId, String moduleName) {
        super(moduleId, moduleName);

        keyBinding = registerKeybind(GLFW.GLFW_KEY_RIGHT_SHIFT, "clickgui.dev.chimera.client");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

                List<AbstractModule> modules = ModuleManager.getAllModules();
                modules.stream().forEach((module) -> {
                    farthestInText = textRenderer.getWidth(module.MODULE_NAME) > farthestInText ?
                            textRenderer.getWidth(module.MODULE_NAME)
                            : farthestInText;
                });

                toggle();
            }
        });


    }

    ClickGui clickGui;

    @Override
    public void onEnable() {
        super.onEnable();

        clickGui = new ClickGui(50, 50, farthestInText + 4f + 1f, ModuleManager.getAllModules().size() * 14f + 4f);
        MinecraftClient.getInstance().setScreen(clickGui);
    }


    @Override
    public void onDisable() {
        super.onDisable();
        if (MinecraftClient.getInstance().currentScreen instanceof ClickGui) {
            MinecraftClient.getInstance().setScreen(null);
        }
    }
}
