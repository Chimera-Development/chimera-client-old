package dev.chimera.gui.components;

import dev.chimera.gui.events.MouseButtonEvent;
import dev.chimera.gui.events.MouseMoveEvent;
import dev.chimera.gui.types.Anchor;
import dev.chimera.gui.types.Position;
import dev.chimera.gui.types.Size;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GuiWindow extends Panel {
    public Panel contentPanel = new Panel();
    Rectangle backgroundRect = null;
    boolean movingWindow = false;

    public GuiWindow(){
        System.out.println("GuiWindow> onLoad called!");
        Rectangle titleRect = new Rectangle();
        titleRect.size = new Size(this.size.width, 16);
        titleRect.anchor.LEFT = true;
        titleRect.anchor.RIGHT = true;

        Label titleLabel = new Label();
        titleLabel.size = new Size(this.size.width, 16);
        titleLabel.content = "Hello World!";
        titleRect.anchor.LEFT = true;
        titleRect.anchor.RIGHT = true;

        System.out.println(this);
        System.out.println(contentPanel);
        //contentPanel = new Panel();
        contentPanel.position = new Position(0,16);
        contentPanel.size = new Size(this.size.width, this.size.height-16);
        contentPanel.anchor = Anchor.all();
        contentPanel.anchor.TOP = false;

        backgroundRect = new Rectangle();
        backgroundRect.color = new Color(0,0,0,128);
        backgroundRect.position = new Position(0,16);
        backgroundRect.size = new Size(this.contentPanel.size.width, this.contentPanel.size.height);
        backgroundRect.anchor = Anchor.all();
        backgroundRect.anchor.TOP = false;
        backgroundRect.anchor.BOTTOM = false;

        this.children.add(titleRect);
        this.children.add(titleLabel);

        this.children.add(backgroundRect);
        this.children.add(contentPanel);
        System.out.println("GuiWindow> onLoad finished!");
    }


    // For some reason, this is a bit broken
    @Override
    public void onLoad() {
    }

    @Override
    public BufferedImage render(Size maxSize) {
        Size newSize = backgroundRect.size.clone();
        newSize.height = contentPanel.getContentSize(maxSize).height;
        backgroundRect.resize(newSize);
        return super.render(maxSize);
    }

    @Override
    public void onMouseButton(Size maxSize, MouseButtonEvent event) {
        if(event.inside && event.position.y <= 16)
        {
            movingWindow = (event.type == MouseButtonEvent.Type.MouseDown);
            System.out.println(movingWindow);
        }
        else if (event.type == MouseButtonEvent.Type.MouseUp) {
            movingWindow = false;
        }
        super.onMouseButton(maxSize, event);
    }

    @Override
    public void onMouseMove(Size maxSize, MouseMoveEvent event) {
        if(movingWindow)
        {
            this.position.x += event.relative.x;
            this.position.y += event.relative.y;
            System.out.println("Should have moved, maybe");
            System.out.println(event.relative.y);
        }
        super.onMouseMove(maxSize, event);
    }
}
