package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AnimatedTexturedButtonWidget extends ButtonWidget {
    protected final Identifier texture;
    protected final int u;
    protected final int v;
    protected final int hoveredVOffset;
    protected final int textureWidth;
    protected final int textureHeight;
    protected final float maxAnimationOffset;
    float animationOffset;
    public double startFadeIn;

    public AnimatedTexturedButtonWidget(int x, int y, int width, int height, int u, int v, Identifier texture, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, height, 0, -1, texture, 256, 256, pressAction);
    }

    public AnimatedTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int maxAnimationOffset, Identifier texture, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, maxAnimationOffset, -1, texture, 256, 256, pressAction);
    }

    public AnimatedTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int maxAnimationOffset, double startFadeIn, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, maxAnimationOffset, startFadeIn, texture, textureWidth, textureHeight, pressAction, ScreenTexts.EMPTY);
    }

    public AnimatedTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int maxAnimationOffset, double startFadeIn, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, Text message) {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.maxAnimationOffset = maxAnimationOffset;
        this.startFadeIn = startFadeIn;
        this.texture = texture;
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta, double fade) {
        float calcFade = (float)Math.max(Math.min(fade - startFadeIn, 1), 0);

        boolean hover = mouseX > this.getX()
                && mouseY > this.getY()
                && mouseX < this.getX() + this.getWidth()
                && mouseY < this.getY() + this.getHeight();
        animationOffset += (hover ? 1 : -1) * delta;
        if (animationOffset < 0) animationOffset = 0;
        if (animationOffset > maxAnimationOffset) animationOffset = maxAnimationOffset;

        float easedAnimationOffset = (1 - (float)Math.pow(1 - animationOffset / maxAnimationOffset, 5)) * maxAnimationOffset;

        RenderSystem.enableBlend();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        RenderSystem.enableDepthTest();

        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, calcFade);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        RenderSystem.setShaderTexture(0, texture);

        float width = this.width + easedAnimationOffset;
        float height = this.height + easedAnimationOffset;

        float textureWidth = this.textureWidth + easedAnimationOffset;
        float textureHeight = this.textureHeight + easedAnimationOffset;
        float u = this.u;
        float v = this.v;

        float u0, u1, v0, v1;

        u0 = u / textureWidth;
        u1 = (u + width) / textureWidth;
        v0 = v / textureHeight;
        v1 = (v + height) / textureHeight;


        float x0 = this.getX() - (easedAnimationOffset / 2);
        float y0 = this.getY() - (easedAnimationOffset / 2);

        float x1 = x0 + width;
        float y1 = y0 + height;

        int z = 1;

        bufferBuilder.vertex(x0, y1, (float) z).texture(u0, v1).next();
        bufferBuilder.vertex(x1, y1, (float) z).texture(u1, v1).next();
        bufferBuilder.vertex(x1, y0, (float) z).texture(u1, v0).next();
        bufferBuilder.vertex(x0, y0, (float) z).texture(u0, v0).next();

        tessellator.draw();

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }
}
