package dev.chimera.amalthea.events.input;

import dev.chimera.amalthea.events.AbstractEvent;
import lombok.Getter;

public class KeyboardEvent extends AbstractEvent {
    public static class Press extends KeyboardEvent {
        @Getter
        private final int key;

        public Press(int key) {
            this.key = key;
        }
    }
    public static class Release extends KeyboardEvent {
        @Getter
        private final int key;

        public Release(int key) {
            this.key = key;
        }
    }
}
