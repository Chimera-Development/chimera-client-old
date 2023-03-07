package dev.chimera.gui.components;

import dev.chimera.gui.Component;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rectangle extends Component {
    public Color color = new Color(0,0,0);

    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width, (int) maxSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.setPaint(color);
        g.fillRect(0,0,output.getWidth(),output.getHeight());
        return output;
    }

    private Color lastColor = new Color(0,0,0);
    @Override
    public boolean hasUpdated(Size maxSize) {
        if(!lastColor.equals(color))
        {
            lastColor = color;
            return true;
        }
        return super.hasUpdated(maxSize);
    }
}
