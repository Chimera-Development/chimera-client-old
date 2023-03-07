package dev.chimera.gui.components;

import dev.chimera.gui.Component;
import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Picture extends Component {
    public BufferedImage texture;

    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width, (int) maxSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.drawImage(texture, 0, 0, output.getWidth(), output.getHeight(), null);
        return output;
    }

    @Override
    public void onMouseMove(Size maxSize, MouseMoveEvent event) {
        /*
        if(event.inside)
        {
            this.size.width -= 5;
            this.size.height -= 5;
        }
        else {
            this.size.width += 5;
            this.size.height += 5;
        }*/
        super.onMouseMove(maxSize, event);
    }

    private BufferedImage lastImage = null;
    @Override
    public boolean hasUpdated(Size maxSize) {
        if(lastImage != texture)
        {
            lastImage = texture;
            return true;
        }
        return super.hasUpdated(maxSize);
    }
}
