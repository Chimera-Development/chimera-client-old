package dev.chimera.amalthea.events.misc;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.client.util.math.MatrixStack;

public class GuiRenderEvent extends AbstractEvent {
    private MatrixStack matrices;
    private float floatthing;

    public GuiRenderEvent(){}

    public void setMatrices(MatrixStack matrices) {
        this.matrices = matrices;
    }
    public void setFloatthing(float floatthing){
        this.floatthing = floatthing;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public float getFloatthing() {
        return floatthing;
    }
}
