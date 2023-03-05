package dev.chimera.amalthea.events.player;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.entity.player.PlayerEntity;

public class JumpEvent extends AbstractEvent {

    private PlayerEntity player;
    public JumpEvent(PlayerEntity player){
        this.player = player;
    }

    public PlayerEntity getPlayer(){
        return player;
    }
}
