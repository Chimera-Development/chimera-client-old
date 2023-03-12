package dev.chimera.Utils;

import dev.chimera.mixins.MinecraftServerAccessor;

import java.io.File;

import static dev.chimera.ChimeraClient.mc;

public class Utils {
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
}
