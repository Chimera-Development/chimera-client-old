package dev.chimera.gui.types;

public class Size implements Cloneable {
    public double width = 0;
    public double height = 0;

    public Size() {}
    public Size(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public Size clone() {
        try {
            Size clone = (Size) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Size s)
        {
            return (s.width == this.width && s.height == this.height);
        }
        return false;
    }
}
