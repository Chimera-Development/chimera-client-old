package dev.chimera.gui.components;

import com.ibm.icu.util.BuddhistCalendar;
import dev.chimera.gui.Component;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Panel extends Component {
    public List<Component> children = new ArrayList<>();

    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width.value, (int) maxSize.height.value, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        for (Component child : children) {
            //System.out.printf("Rendering %s at %s with size %s", child, child.position, child.size);

            Position pos = new Position(child.position.x.toPixel(maxSize.width.value),
                    child.position.y.toPixel(maxSize.height.value));

            Size cSize = new Size(child.size.width.toPixel(maxSize.width.value),
                    child.size.height.toPixel(maxSize.height.value));
            BufferedImage component = child.render(cSize);
            g.drawImage(component, (int)pos.x.value, (int)pos.y.value, null);
        }

        return output;
    }

    @Override
    public boolean hasUpdated(Size maxSize) {
        for (Component child : children)
        {
            if(child.hasUpdated(maxSize))
                return true;
        }
        return super.hasUpdated(maxSize);
    }
}
