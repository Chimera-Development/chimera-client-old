package dev.chimera.gui.events;

import dev.chimera.gui.types.Position;

public class MouseMoveEvent extends InputEvent {
    public Position position = new Position();
    public Position relative = new Position();
    public boolean inside = true;
}
