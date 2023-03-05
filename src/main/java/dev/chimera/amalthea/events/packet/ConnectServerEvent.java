package dev.chimera.amalthea.events.packet;

import dev.chimera.amalthea.events.AbstractEvent;

import java.net.InetSocketAddress;

public class ConnectServerEvent extends AbstractEvent {
    private InetSocketAddress address;
    public ConnectServerEvent(InetSocketAddress address){
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}
