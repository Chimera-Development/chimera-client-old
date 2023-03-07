package dev.chimera.gui.events;

import dev.chimera.gui.types.Position;

public class MouseButtonEvent extends InputEvent {
    public int button = 0;
    public Position position = new Position();
    public boolean inside = false;

    public enum Type {
        MouseDown,
        MouseUp
    }

    public Type type = Type.MouseDown;
}
