package dev.chimera.gui;

import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;
import net.minecraft.client.MinecraftClient;

import java.awt.image.BufferedImage;

public abstract class Component {
    public Position position = new Position(new Value("0px"), new Value("0px"));
    public Size size = new Size(new Value("100%"), new Value("100%"));
    public BufferedImage render(Size maxSize)
    {
        return null;
    }

    public static Size getMaxSize()
    {
        Size size = new Size(new Value(MinecraftClient.getInstance().getWindow().getWidth(), Value.ValueType.Pixel),
                new Value(MinecraftClient.getInstance().getWindow().getHeight(), Value.ValueType.Pixel));
        return size;
    }

    public Size getSize(Size maxSize)
    {
        return maxSize;
    }

    private Position lastPosition = null;
    private Size lastSize = null;
    private Size lastMaxSize = null;
    public boolean hasUpdated(Size maxSize)
    {
        return true;
        /*
        if(size != lastSize || position != lastPosition || maxSize.width.value != lastMaxSize.width.value || lastMaxSize.height.value != maxSize.height.value)
        {
            lastSize = size;
            lastPosition = position;
            lastMaxSize = maxSize;
            return true;
        }
        return false;*/
    }


    //Event system would be useful here
    public void onMouseButton(Size maxSize, MouseButtonEvent event) {}

    public void onMouseMove(Size maxSize, MouseMoveEvent event) {}
}
