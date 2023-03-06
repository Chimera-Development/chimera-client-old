package dev.chimera.gui;

import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Anchor;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import net.minecraft.client.MinecraftClient;

import java.awt.image.BufferedImage;

public abstract class Component {
    public Position position = new Position(0, 0);
    public Size size = new Size(100, 100);
    public Anchor anchor = new Anchor();

    public BufferedImage render(Size maxSize)
    {
        return null;
    }

    public static Size getMaxSize()
    {
        return new Size(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
    }

    public Size getContentSize(Size maxSize)
    {
        return maxSize;
    }

    public void resize(Size newSize)
    {
        // Most components do not need this event, unless they contain another component
        this.size = newSize.clone();
        return;
    }

    private Position lastPosition = null;
    private Size lastSize = null;
    private Size lastMaxSize = null;
    public boolean hasUpdated(Size maxSize)
    {
        if(!size.equals(lastSize) || !position.equals(lastPosition) || !maxSize.equals(lastMaxSize))
        {
            lastSize = size.clone();
            lastPosition = position.clone();
            lastMaxSize = maxSize.clone();
            System.out.println("Component updated, remove this message please!");
            return true;
        }
        return false;
    }


    //Event system would be useful here
    public void onMouseButton(Size maxSize, MouseButtonEvent event) {}

    public void onMouseMove(Size maxSize, MouseMoveEvent event) {}
}
