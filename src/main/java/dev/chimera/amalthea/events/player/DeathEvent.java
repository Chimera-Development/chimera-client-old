package dev.chimera.amalthea.events.player;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.entity.damage.DamageSource;

public class DeathEvent extends AbstractEvent {
    private DamageSource damageSource;

    public DeathEvent(DamageSource damageSource){
        this.damageSource = damageSource;
    }

    public DamageSource getDamageSource(){
        return damageSource;
    }
}
