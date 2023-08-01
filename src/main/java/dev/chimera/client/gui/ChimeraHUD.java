package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.modules.AbstractModule;
import dev.chimera.client.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;

import java.util.List;

public class ChimeraHUD {


    private static TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public static void render(DrawContext drawContext) {
        float farthestInText = 0f;
        List<AbstractModule> activeModules = ModuleManager.getActiveModules();
        for (AbstractModule module :
                activeModules) {
            farthestInText = textRenderer.getWidth(module.MODULE_NAME) > farthestInText ?
                    textRenderer.getWidth(module.MODULE_NAME)
                    : farthestInText;
        }

        float hudBoxHeight = activeModules.size() * 10f + 20f;

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        //triangle one

        bufferBuilder.vertex(farthestInText + 20f, hudBoxHeight, 1f).color(1f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(farthestInText + 20f, 0, 1f).color(0f, 1f, 0f, .5f).next();
        bufferBuilder.vertex(0f, 0f, 1f).color(0f, 0f, 1f, .5f).next();
        //triangle two
        bufferBuilder.vertex(farthestInText + 20f, hudBoxHeight, 1f).color(1f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(0f, 0f, 1f).color(0f, 0f, 1f, .5f).next();
        bufferBuilder.vertex(0f, hudBoxHeight, 1f).color(1f, .5f, 1f, .5f).next();


        tessellator.draw();


        RenderSystem.disableBlend();

        int i = 0;
        for (AbstractModule module :
                activeModules) {
            i++;

            drawContext.drawText(MinecraftClient.getInstance().textRenderer, module.MODULE_NAME, 10, i * 10, 0xffffff, false);
        }

        RenderSystem.enableDepthTest();


    }


}
