package dev.chimera.amalthea.events.misc;

import dev.chimera.amalthea.events.AbstractEvent;

public class KeyEvents extends AbstractEvent {
    public static class Press {
        public int key;

        public Press(int key) {
            this.key = key;
        }
    }
    public static class Release {
        public int key;

        public Release(int key) {
            this.key = key;
        }
    }
}
