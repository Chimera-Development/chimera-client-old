package dev.chimera.amalthea.events.movement;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class PlayerMoveEvent extends AbstractEvent {
    private MovementType type;
    private Vec3d vec;

    public PlayerMoveEvent(MovementType type, Vec3d vec) {
        this.type = type;
        this.vec = vec;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Vec3d getVec() {
        return vec;
    }

    public void setVec(Vec3d vec) {
        this.vec = vec;
    }
}
