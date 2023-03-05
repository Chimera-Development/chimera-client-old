package dev.chimera.gui.components;

import com.ibm.icu.util.BuddhistCalendar;
import dev.chimera.gui.Component;
import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Panel extends Component {
    public List<Component> children = new ArrayList<>();

    public Panel()
    {
        onLoad();
    }

    public void onLoad() {}

    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width.value, (int) maxSize.height.value, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        for (Component child : children) {
            Position pos = new Position(child.position.x.toPixel(maxSize.width.value),
                    child.position.y.toPixel(maxSize.height.value));

            Size cSize = new Size(child.size.width.toPixel(maxSize.width.value),
                    child.size.height.toPixel(maxSize.height.value));
            cSize = child.getSize(cSize);

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

    @Override
    public void onMouseButton(Size maxSize, MouseButtonEvent event) {
        for (Component child : children) {
            Position pos = new Position(child.position.x.toPixel(maxSize.width.value),
                    child.position.y.toPixel(maxSize.height.value));

            Size cSize = new Size(child.size.width.toPixel(maxSize.width.value),
                    child.size.height.toPixel(maxSize.height.value));
            cSize = child.getSize(cSize);

            MouseButtonEvent e = new MouseButtonEvent();
            e.type = event.type;
            e.button = event.button;
            e.inside = true;

            e.position = new Position(new Value(event.position.x.value-pos.x.value, Value.ValueType.Pixel),
                    new Value(event.position.y.value-pos.y.value, Value.ValueType.Pixel));
            if(e.position.x.value < 0 || e.position.y.value < 0)
                e.inside = false;
            else if(e.position.x.value > cSize.width.value || e.position.y.value > cSize.height.value)
                e.inside = false;

            child.onMouseButton(maxSize, e);
        }
    }

    @Override
    public void onMouseMove(Size maxSize, MouseMoveEvent event) {
        for (Component child : children) {
            Position pos = new Position(child.position.x.toPixel(maxSize.width.value),
                    child.position.y.toPixel(maxSize.height.value));

            Size cSize = new Size(child.size.width.toPixel(maxSize.width.value),
                    child.size.height.toPixel(maxSize.height.value));
            cSize = child.getSize(cSize);

            MouseMoveEvent e = new MouseMoveEvent();
            e.inside = true;

            e.position = new Position(new Value(event.position.x.value-pos.x.value, Value.ValueType.Pixel),
                    new Value(event.position.y.value-pos.y.value, Value.ValueType.Pixel));

            if(e.position.x.value < 0 || e.position.y.value < 0)
                e.inside = false;
            else if(e.position.x.value > cSize.width.value || e.position.y.value > cSize.height.value)
                e.inside = false;

            child.onMouseMove(maxSize, e);
        }
    }
}
