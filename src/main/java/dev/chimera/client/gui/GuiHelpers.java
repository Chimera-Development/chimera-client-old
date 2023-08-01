package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;

public class GuiHelpers {

    public void drawColoredQuad(float x, float x1, float y, float y1, float z, int argbColor){
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        //triangle one
        bufferBuilder.vertex(x1, y1, 1f).color(1f, 1f, 1f, 1f).next();
        bufferBuilder.vertex(x1, y, 1f).color(1f, 1f, 1f, 1f).next();
        bufferBuilder.vertex(x, y, 1f).color(1f, 1f, 1f, 1f).next();

        //triangle two
        bufferBuilder.vertex(x1, y1, 1f).color(1f, 1f, 1f, 1f).next();
        bufferBuilder.vertex(x, y, 1f).color(1f, 1f, 1f, 1f).next();
        bufferBuilder.vertex(x, y1, 1f).color(1f, 1f, 1f, 1f).next();

        tessellator.draw();
    }



}
