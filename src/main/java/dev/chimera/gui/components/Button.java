package dev.chimera.gui.components;

import dev.chimera.gui.Component;
import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.types.Anchor;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button extends Panel {
    public String text = "Test Button";
    public boolean enabled = false;
    Rectangle backgroundRect = null;
    Label label = null;

    public Button()
    {
        System.out.println("onLoad called");
        System.out.println(this);
        backgroundRect = new Rectangle();
        backgroundRect.color = new Color(0,0,0,0);
        backgroundRect.size = this.size.clone();
        backgroundRect.anchor = Anchor.all();

        label = new Label();
        label.size = this.size.clone();
        label.anchor = Anchor.all();
        this.children.add(backgroundRect);
        this.children.add(label);
    }

    @Override
    public void onMouseButton(Size maxSize, MouseButtonEvent event) {
        if(event.inside && event.type == MouseButtonEvent.Type.MouseDown)
        {
            enabled = !enabled;
        }
        super.onMouseButton(maxSize, event);
    }

    @Override
    public BufferedImage render(Size maxSize) {
        if(enabled)
        {
            backgroundRect.color = new Color(128,128,128);
        }
        else {
            backgroundRect.color = new Color(0,0,0,0);
        }
        if(text.equals("Label"))
        {
            throw new RuntimeException("This should never happen!");
        }
        label.content = text;
        return super.render(maxSize);
    }
}
