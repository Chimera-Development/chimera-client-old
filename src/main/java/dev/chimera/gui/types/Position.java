package dev.chimera.gui.types;

// I don't think should be class, but i forgot what java has
public class Position {
    public Value x = new Value("0px");
    public Value y = new Value("0px");

    public Position() {} // any proper way of empty constructors?

    public Position(Value x, Value y)
    {
        this.x = x;
        this.y = y;
    }
}
