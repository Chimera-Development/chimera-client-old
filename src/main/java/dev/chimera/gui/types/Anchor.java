package dev.chimera.gui.types;

public class Anchor {
    public boolean TOP = false;
    public boolean LEFT = false;
    public boolean BOTTOM = false;
    public boolean RIGHT = false;

    public static Anchor all()
    {
        Anchor a = new Anchor();
        a.TOP = true;
        a.LEFT = true;
        a.BOTTOM = true;
        a.RIGHT = true;
        return a;
    }
}
