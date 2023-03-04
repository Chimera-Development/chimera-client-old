package dev.chimera.gui.types;

public class Value {
    public enum ValueType {
        Pixel,
        Percentage
    }

    public ValueType type = ValueType.Pixel;
    public float value = 0;

    public void setValue(String value)
    {
        // Can't use switch cases here, since you cant use endswith
        if (value.endsWith("px"))
        {
            this.value = Float.valueOf(value.substring(0,value.length()-2));
            this.type = ValueType.Pixel;
        }
        else if (value.endsWith("%"))
        {
            this.value = Float.valueOf(value.substring(0,value.length()-1))/100;
            this.type = ValueType.Percentage;
        }
        else {
            throw new RuntimeException("Invalid value given " + value);
        }
    }

    public Value(String value)
    {
        setValue(value);
    }

    public Value(float value, ValueType type)
    {
        this.value = value;
        this.type = type;
    }

    public Value toPixel(float maxValue)
    {
        if (type == ValueType.Pixel)
        {
            return this;
        }
        else if (type == ValueType.Percentage) {
            return new Value(value*maxValue, ValueType.Pixel);
        }
        return null;
    }
}
