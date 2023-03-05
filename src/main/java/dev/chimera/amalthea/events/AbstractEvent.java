package dev.chimera.amalthea.events;

public class AbstractEvent {
        public boolean cancelled = false;
        public boolean isCancelled() {
                return cancelled;
        }

        public void setCancelled(boolean cancelled) {
                this.cancelled = cancelled;
        }

}
