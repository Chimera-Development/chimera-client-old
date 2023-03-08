package dev.chimera.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.KeyEvents;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.gui.Component;
import dev.chimera.gui.InteractiveScreen;
import dev.chimera.gui.components.Button;
import dev.chimera.gui.components.GuiWindow;
import dev.chimera.gui.components.Label;
import dev.chimera.gui.components.Picture;
import dev.chimera.gui.types.Anchor;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleInitializer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.chimera.gui.InGameOverlay.SCREEN;

public class ClickGUIModule extends Module {
    List<GuiWindow> windows = new ArrayList<>();
    public ClickGUIModule()
    {
        super("ClickGUI", GLFW.GLFW_KEY_RIGHT_SHIFT, false);
    }

    @Override
    public void init() {

    }

    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("Opening gui!");
        MinecraftClient.getInstance().setScreen(new InteractiveScreen());

        GuiWindow window = new GuiWindow();
        window.size.height = 1000;
        window.anchor = Anchor.all();

        /*Picture p = new Picture();
        try {
            p.texture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("trollface.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.anchor = Anchor.all();
        p.size = new Size(0.75*window.contentPanel.size.width, 0.75*window.contentPanel.size.height);
        window.contentPanel.children.add(p);*/
        int y = 0;
        for (Module m : ModuleInitializer.MODULE_NAMES.values())
        {
            System.out.println(m);
            if (m.getModuleName() == null)
                continue; // This shouldn't happen but caused a bug. TODO: Look into fixing mysterious bug
            System.out.println("Module passed!");
            Button l = new Button();
            l.text = m.getModuleName();
            l.enabled = m.getModuleEnabled();
            if (l.text.equals(this.getModuleName()))
                l.enabled = true;
            System.out.println(window.contentPanel);
            l.size = new Size(window.contentPanel.size.width, 16);
            l.position = new Position(0, y);
            l.anchor.LEFT = true;
            l.anchor.RIGHT = true;
            y += 16;
            window.contentPanel.children.add(l);
        }

        System.out.println("Done!");
        windows.add(window);
        SCREEN.children.add(window);
        System.out.println(SCREEN.children.size());
    }

    @Override
    public void onDisable() {
        ChimeraClient.LOGGER.info("Closing gui!");
        for (GuiWindow window : windows)
        {
            SCREEN.children.remove(window);
        }
        if (MinecraftClient.getInstance().currentScreen != null)
            MinecraftClient.getInstance().currentScreen.close();
    }



    @Override
    public void onTickStart(TickEvent.Start event) {
        try {
            if (windows.size() == 0)
                return;
            GuiWindow window = windows.get(0);
            for (Component c : window.contentPanel.children) {
                if (c instanceof Button b) {
                    Module mod = ModuleInitializer.MODULE_NAMES.get(b.text);
                    if (mod.getModuleEnabled() != b.enabled) {
                        mod.toggle();
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }
}
