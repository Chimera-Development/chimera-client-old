package dev.chimera.gui;

import dev.chimera.ChimeraClient;
import dev.chimera.gui.events.MouseUpdateEvent;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;
import org.spongepowered.asm.util.PrettyPrinter;

import java.awt.image.BufferedImage;

public abstract class Component {
    public Position position = new Position(new Value("0px"), new Value("0px"));
    public Size size = new Size(new Value("100%"), new Value("100%"));
    public BufferedImage render(Size maxSize)
    {
        return null;
    }

    private Position lastPosition = null;
    private Size lastSize = null;
    private Size lastMaxSize = null;
    public boolean hasUpdated(Size maxSize)
    {
        if(size != lastSize || position != lastPosition || maxSize.width.value != lastMaxSize.width.value || lastMaxSize.height.value != maxSize.height.value)
        {
            lastSize = size;
            lastPosition = position;
            lastMaxSize = maxSize;
            return true;
        }
        return false;
    }

    //Event system would be useful here
    public void onMouseUpdate(MouseUpdateEvent mouseUpdate){}
}
