package dev.chimera.client.gui.clickgui;

import dev.chimera.client.gui.clickgui.components.ButtonList;
import dev.chimera.client.gui.clickgui.components.ModuleButton;
import dev.chimera.client.modules.AbstractModule;
import dev.chimera.client.modules.ModuleManager;
import dev.chimera.client.modules.modules.ClickGUIModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {


    ButtonList listView;
    Profiler profiler = MinecraftClient.getInstance().getProfiler();

    public ClickGui(float x, float y, float width, float height) {
        super(Text.literal("ClickGui"));

        List<ModuleButton> buttonsList = new ArrayList<>();

        float buttonGroupHeight = 0.0f;
        for (AbstractModule module : ModuleManager.getAllModules()) {
            buttonsList.add(new ModuleButton(-1, -1, -1, 12f, module));
            buttonGroupHeight += 12f;
        }

        //the 2f is marginY, I'll need to refactor all of this to have proper dependency injection
        buttonGroupHeight += (buttonsList.size() + 1) * 2f;

        listView = new ButtonList(x, y, width, buttonGroupHeight, buttonsList);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        profiler.push("ClickGUI");

        listView.render(context, mouseX, mouseY, delta);

        profiler.pop();
    }

    private boolean firstRelease = true;

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {

        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT && !firstRelease) {
            ((ClickGUIModule) ModuleManager.getModule(ClickGUIModule.class)).disable();
        }

        firstRelease = false;

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        listView.onMouseClick(mouseX, mouseY, button);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        ((ClickGUIModule) ModuleManager.getModule(ClickGUIModule.class)).disable();
        super.close();
    }
}
