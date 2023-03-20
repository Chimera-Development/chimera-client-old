package dev.chimera.amalthea.events;

import lombok.Getter;
import lombok.Setter;

public class AbstractEvent {
        @Getter @Setter
        private boolean cancelled = false;
}
