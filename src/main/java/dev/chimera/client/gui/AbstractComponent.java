package dev.chimera.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;

public abstract class AbstractComponent {

    private float x;
    private float y;

    private float width;
    private float height;

    protected AbstractComponent() {
    }

    public void onMouseClick(double x, double y, int mouseButton) {
        if (!isWithinBounds((int) x, (int) y)) return;

    }

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public boolean isWithinBounds(double mouseX, double mouseY) {
        return mouseX > this.getX()
                && mouseY > this.getY()
                && mouseX < this.getX() + this.getWidth()
                && mouseY < this.getY() + this.getHeight();
    }

    public void drawBackground(int argbColor) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableDepthTest();

        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        //triangle one
        bufferBuilder.vertex(getX1(), getY1(), 1f).color(argbColor).next();
        bufferBuilder.vertex(getX1(), getY(), 1f).color(argbColor).next();
        bufferBuilder.vertex(getX(), getY(), 1f).color(argbColor).next();

        //triangle two
        bufferBuilder.vertex(getX1(), getY1(), 1f).color(argbColor).next();
        bufferBuilder.vertex(getX(), getY(), 1f).color(argbColor).next();
        bufferBuilder.vertex(getX(), getY1(), 1f).color(argbColor).next();

        tessellator.draw();

        RenderSystem.enableDepthTest();
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX1() {
        return width + x;
    }

    public float getY1() {
        return height + y;
    }

    public void setX1(float x1) {
        this.width = x1 - x;
    }

    public void setY1(float y1) {
        this.height = y1 - y;
    }

}
