package dev.chimera.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventBus;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static dev.chimera.ChimeraClient.mc;

public class BouncingLogo extends Module {
    public BouncingLogo() {
        super(ModuleCategory.RENDER, "Bouncing Logo", GLFW.GLFW_KEY_UNKNOWN);
    }
    int xVelocity = 10;
    int yVelocity = 10;
    int window_height = mc.getWindow().getHeight();
    int window_width = mc.getWindow().getWidth();
    int x = window_width/2;
    int y = window_height/2;
    int width = 0;
    int height = 0;

    private static final Identifier texture = new Identifier("chimera-client", "icon.png");
    @Override
    public void init() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTickStart(TickEvent.Start event) {

    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }
    @EventListener(id = EventListenerIDs.lwjglRendererTick+" lol", runAfter = EventListenerIDs.firstRenderer, runBefore = EventListenerIDs.lastRenderer)
    public void onGUIRender(GuiRenderEvent event){
        MatrixStack matrixStack = event.getMatrices();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND);
        RenderSystem.setShaderTexture(0, texture);
        int window_height = mc.getWindow().getHeight();
        int window_width = mc.getWindow().getWidth();
        if(x >= window_width - 72 || x < 0){
            xVelocity = xVelocity * -1;
        }
        x = x - xVelocity;

        if(y >= window_height - 18 || y < 0){
            yVelocity = yVelocity * -1;
        }
        y = y + yVelocity;
        DrawableHelper.drawTexture(matrixStack, window_width, y, 0, 0, 72, 18, 72, 18);
    }
}
