package dev.chimera.nemean;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;

public class ScreenRenderingAttempt extends DrawableHelper {

    public void render() {

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        //triangle one
        bufferBuilder.vertex(200f, 0f, 1f).color(0f, 1f, 0f, .5f).next();
        bufferBuilder.vertex(0f, 0f, 1f).color(0f, 0f, 1f, .5f).next();
        bufferBuilder.vertex(200f, 200f, 1f).color(1f, 0f, 0f, .5f).next();
        //triangle two
        bufferBuilder.vertex(200f, 200f, 1f).color(1f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(0f, 0f, 1f).color(0f, 0f, 1f, .5f).next();
        bufferBuilder.vertex(0f, 200f, 1f).color(1f, .5f, 1f, .5f).next();

        tessellator.draw();


        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();

    }
}
