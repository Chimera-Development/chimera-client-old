package dev.chimera.gui;

import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static dev.chimera.gui.InGameOverlay.SCREEN;


public class InteractiveScreen extends Screen {
    protected InteractiveScreen(Text title) {
        super(title);
    }

    public InteractiveScreen() {
        this(Text.empty());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Window w = MinecraftClient.getInstance().getWindow();
        float screenMouseX = (float)((mouseX/width)*(double)w.getWidth());
        float screenMouseY = (float)((mouseY/height)*(double)w.getHeight());

        MouseButtonEvent event = new MouseButtonEvent();
        event.position = new Position(new Value(screenMouseX, Value.ValueType.Pixel), new Value(screenMouseY, Value.ValueType.Pixel));
        event.button = button;
        event.type = MouseButtonEvent.Type.MouseDown;

        SCREEN.onMouseButton(Component.getMaxSize(), event);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Window w = MinecraftClient.getInstance().getWindow();
        float screenMouseX = (float)((mouseX/width)*(double)w.getWidth());
        float screenMouseY = (float)((mouseY/height)*(double)w.getHeight());

        MouseButtonEvent event = new MouseButtonEvent();
        event.position = new Position(new Value(screenMouseX, Value.ValueType.Pixel), new Value(screenMouseY, Value.ValueType.Pixel));
        event.button = button;
        event.type = MouseButtonEvent.Type.MouseUp;

        SCREEN.onMouseButton(Component.getMaxSize(), event);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        Window w = MinecraftClient.getInstance().getWindow();
        float screenMouseX = (float)((mouseX/width)*(double)w.getWidth());
        float screenMouseY = (float)((mouseY/height)*(double)w.getHeight());

        MouseMoveEvent event = new MouseMoveEvent();
        event.position = new Position(new Value(screenMouseX, Value.ValueType.Pixel), new Value(screenMouseY, Value.ValueType.Pixel));

        SCREEN.onMouseMove(Component.getMaxSize(), event);

        super.mouseMoved(mouseX, mouseY);
    }
}
