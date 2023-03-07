package dev.chimera.gui.events;

public class InputEvent {
    public boolean handled = false;
    public void handle()
    {
        handled = true;
    }
}
