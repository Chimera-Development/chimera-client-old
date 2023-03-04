package dev.chimera.gui.events;

import dev.chimera.gui.types.Position;

public class MouseUpdateEvent extends InputEvent {
    boolean mouseDown = false;
    Position mousePosition = new Position();
}
