package dev.chimera.Utils;

import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.packet.PacketEvent;
import dev.chimera.mixins.MinecraftServerAccessor;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static dev.chimera.ChimeraClient.EVENT_BUS;
import static dev.chimera.ChimeraClient.mc;

public class Utils {
    public Utils(){
        EVENT_BUS.registerListenersInClass(this);
    }
    private long prevTime = 0;
    private static double tps = 20;
    private long lastPacket = 0;
    public static List<Double> tpsHistory = new ArrayList<Double>();
    public static String getWorldName() {
        // Singleplayer
        if (mc.isInSingleplayer()) {
            if (mc.world == null) return "";

            File folder = ((MinecraftServerAccessor) mc.getServer()).getSession().getWorldDirectory(mc.world.getRegistryKey()).toFile();
            if (folder.toPath().relativize(mc.runDirectory.toPath()).getNameCount() != 2) {
                folder = folder.getParentFile();
            }
            return folder.getName();
        }

        // Multiplayer
        if (mc.getCurrentServerEntry() != null) {
            return mc.isConnectedToRealms() ? "realms" : mc.getCurrentServerEntry().address;
        }

        return "";
    }
    public static double getTPS(){
        return tps;
    }
    @EventListener(id="getTps")
    public void readPacket(PacketEvent.Receive event) {
        lastPacket = System.currentTimeMillis();
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            long time = System.currentTimeMillis();
            long timeOffset = Math.abs(1000 - (time - prevTime)) + 1000;
            tps = Math.round(MathHelper.clamp(20 / (timeOffset / 1000d), 0, 20) * 100d) / 100d;
            prevTime = time;
            tpsHistory.add(tps);
        }
    }

}
