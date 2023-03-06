package dev.chimera.gui.types;

// I don't think should be class, but i forgot what java has
public class Position implements Cloneable {
    public double x = 0;
    public double y = 0;

    public Position() {
    } // any proper way of empty constructors?

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Position clone() {
        try {
            Position clone = (Position) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Position p)
        {
            return (p.x == this.x && p.y == this.y);
        }
        return false;
    }
}