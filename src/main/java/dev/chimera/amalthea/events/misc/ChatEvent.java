package dev.chimera.amalthea.events.misc;

import dev.chimera.amalthea.events.AbstractEvent;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

public class ChatEvent extends AbstractEvent {


    public static class Send extends ChatEvent{
        private ChatMessageC2SPacket packet;
        public Send(){
            this.packet = packet;
        }

        public ChatMessageC2SPacket getPacket() {
            return packet;
        }

        public void setPacket(ChatMessageC2SPacket packet) {
            this.packet = packet;
        }

        public String getMessage(){
            return packet.chatMessage();
        }

    }
    public static class Receive extends ChatEvent{
        private ChatMessageS2CPacket packet;
        public Receive(){
            this.packet = packet;
        }

        public void setPacket(ChatMessageS2CPacket packet) {
            this.packet = packet;
        }

        public ChatMessageS2CPacket getPacket() {
            return packet;
        }
        public String getMessage(){
            return packet.body().content();
        }
    }
}
