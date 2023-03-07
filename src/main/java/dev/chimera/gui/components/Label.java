package dev.chimera.gui.components;

import dev.chimera.gui.Component;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Label extends Component {
    public String content = "Label";
    public Color color = new Color(255,255,255);
    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width, (int) maxSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.setPaint(color);
        // lower left corner
        g.drawString(content, 0, 16);
        return output;
    }

    private String lastContent = content;
    private Color lastColor = color;

    @Override
    public boolean hasUpdated(Size maxSize) {
        // rgb = int, single
        if(!lastContent.equals(content) || lastColor.getRGB() != color.getRGB())
        {
            lastContent = content;
            lastColor = color;
            return true;
        }
        return super.hasUpdated(maxSize);
    }
}
