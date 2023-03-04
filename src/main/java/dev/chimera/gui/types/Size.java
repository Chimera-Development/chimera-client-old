package dev.chimera.gui.types;

public class Size {
    public Value width = new Value("0px");
    public Value height = new Value("0px");

    public Size() {}
    public Size(Value width, Value height)
    {
        this.width = width;
        this.height = height;
    }
}
