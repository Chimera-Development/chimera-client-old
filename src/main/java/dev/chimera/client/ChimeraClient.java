package dev.chimera.client;

import dev.chimera.client.addons.AddonManager;
import dev.chimera.client.gui.ChimeraHUD;
import dev.chimera.client.modules.modules.AntiFlyKick;
import dev.chimera.client.modules.modules.ClickGUIModule;
import dev.chimera.client.modules.modules.Flight;
import dev.chimera.client.modules.modules.NoFall;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("chimera-client");
    public static final String MOD_ID = "chimera-client";

    @Override
    public void onInitializeClient() {

        AddonManager.initAddons();

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            ChimeraHUD.render(drawContext);
        });

        new Flight();
        new NoFall();
        new AntiFlyKick();

        new ClickGUIModule(Identifier.of(MOD_ID, "clickgui"), "ClickGUI");
    }
}
