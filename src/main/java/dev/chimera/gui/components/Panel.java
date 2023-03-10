package dev.chimera.gui.components;

import dev.chimera.gui.Component;
import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;
import oshi.hardware.platform.mac.MacHardwareAbstractionLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Panel extends Component {
    public List<Component> children = new ArrayList<>();

    public Panel() {
        onLoad();
    }

    public void onLoad() {}

    @Override
    public Size getContentSize(Size maxSize) {
        double width = 1;
        double height = 1;
        for (Component child : children) {
            child.parent = this;
            Position pos = new Position(child.position.x,
                    child.position.y);

            Size cSize = new Size(child.size.width,
                    child.size.height);
            cSize = child.getContentSize(cSize);

            width = Math.max(width, pos.x+cSize.width);
            height = Math.max(height, pos.y+cSize.height);
        }
        width = Math.min(width, maxSize.width);
        height = Math.min(height, maxSize.height);
        return new Size(width, height);
    }

    @Override
    public BufferedImage render(Size maxSize) {
        BufferedImage output = new BufferedImage((int) maxSize.width, (int) maxSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        for (Component child : children) {
            child.parent = this;
            Position pos = new Position(child.position.x,
                    child.position.y);

            Size cSize = new Size(child.size.width,
                    child.size.height);
            cSize = child.getContentSize(cSize);

            BufferedImage component = child.render(cSize);
            g.drawImage(component, (int)pos.x, (int)pos.y, null);
        }

        return output;
    }

    @Override
    public void resize(Size newSize) {
        double xMultiplier = newSize.width/size.width;
        double yMultiplier = newSize.height/size.height;
        for (Component child : children) {
            if(child.anchor.TOP)
            {
                child.position.y *= yMultiplier;
            }
            if(child.anchor.LEFT)
            {
                child.position.x *= xMultiplier;
            }
            Size s = child.size.clone();
            if(child.anchor.BOTTOM)
            {
                s.height *= yMultiplier;
            }
            if(child.anchor.RIGHT)
            {
                s.width *= xMultiplier;
            }
            child.resize(s);
        }
        super.resize(newSize);
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
            Position pos = new Position(child.position.x,
                    child.position.y);

            Size cSize = new Size(child.size.width,
                    child.size.height);
            cSize = child.getContentSize(cSize);

            MouseButtonEvent e = new MouseButtonEvent();

            e.type = event.type;
            e.button = event.button;

            e.position = new Position(event.position.x-pos.x, event.position.y-pos.y);
            if(e.position.x < 0 || e.position.y < 0)
                e.inside = false;
            else if(e.position.x > cSize.width || e.position.y > cSize.height)
                e.inside = false;
            if(!event.inside)
                e.inside = false;

            child.onMouseButton(maxSize, e);
        }
    }

    @Override
    public void onMouseMove(Size maxSize, MouseMoveEvent event) {
        for (Component child : children) {
            Position pos = new Position(child.position.x,
                    child.position.y);

            Size cSize = new Size(child.size.width,
                    child.size.height);
            cSize = child.getContentSize(cSize);

            MouseMoveEvent e = new MouseMoveEvent();

            e.relative = event.relative.clone();
            e.position = new Position(event.position.x-pos.x, event.position.y-pos.y);
            if(e.position.x < 0 || e.position.y < 0)
                e.inside = false;
            else if(e.position.x > cSize.width || e.position.y > cSize.height)
                e.inside = false;
            if(!event.inside)
                e.inside = false;

            child.onMouseMove(maxSize, e);
        }
    }
}
