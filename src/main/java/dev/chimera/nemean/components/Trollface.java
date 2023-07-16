package dev.chimera.nemean.components;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.ChimeraClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Trollface extends DrawableHelper {

    private int coordX = 10;
    private int coordY = 10;

    private boolean movingDown = false;
    private boolean movingRight = false;

    public void render(MatrixStack matrices) {
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        RenderSystem.disableDepthTest();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        Identifier identifier = new Identifier("chimera-client:trollface.png");

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        RenderSystem.setShaderTexture(0, identifier);

        checkedMove();

        drawTexture(matrices, coordX, coordY, 0, 0, 256, 256);
        tessellator.draw();

        RenderSystem.enableDepthTest();
    }

    private boolean isInBoundsY(int y) {
        int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (y > 0 && y < height) {
            return true;
        }
        return false;
    }

    private boolean isInBoundsX(int x) {
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        if (x > 0 && x < width) {
            return true;
        }
        return false;
    }
    void checkedMove() {
        if (movingDown) {
            if (isInBoundsY(this.coordY + 256 + 1)) {
                this.coordY++;
            }else{
                movingDown = !movingDown;
            }
        }
        if (!movingDown) {
            if (isInBoundsY(this.coordY - 1)) {
                this.coordY--;
            }else{
                movingDown = !movingDown;
            }
        }

        if (movingRight) {
            if (isInBoundsX(this.coordX + 256 + 1)) {
                this.coordX++;
            }else{
                movingRight = !movingRight;
            }
        }
        if (!movingRight) {
            if (isInBoundsX(this.coordX - 1)) {
                this.coordX--;
            }else{
                movingRight = !movingRight;
            }
        }
    }

//    public static void update() {
//        if (enabled) {
//            Tessellator tessellator = RenderSystem.renderThreadTesselator();
//            RenderSystem.disableDepthTest();
//
//            BufferBuilder bufferBuilder = tessellator.getBuffer();
//            Identifier identifier = new Identifier("chimera-client:trollface.png");
//
//            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//            RenderSystem.setShaderColor(1, 1, 1, 1);
//
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//            RenderSystem.setShaderTexture(0, identifier);
//
//            int width = 256;
//            int height = 256;
//
//            int textureWidth = 256;
//            int textureHeight = 256;
//            int u = 256;
//            int v = 256;
//
//            int u0, u1, v0, v1;
////        MinecraftClient.getInstance().getTextureManager().getTexture(identifier).bindTexture();
//
//            u0 = u / textureWidth;
//            u1 = (u + width) / textureWidth;
//            v0 = v / textureHeight;
//            v1 = (v + height) / textureHeight;
//
//
//            int x0 = 0;
//            int y0 = 0;
//
//            int x1 = x0 + width;
//            int y1 = y0 + height;
//
//            int z = 1;
//
//            bufferBuilder.vertex((float) x0, (float) y1, (float) z).texture(u0, v1).next();
//            bufferBuilder.vertex((float) x1, (float) y1, (float) z).texture(u1, v1).next();
//            bufferBuilder.vertex((float) x1, (float) y0, (float) z).texture(u1, v0).next();
//            bufferBuilder.vertex((float) x0, (float) y0, (float) z).texture(u0, v0).next();
//            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
//
//            tessellator.draw();
//
//            RenderSystem.enableDepthTest();
//        }
//    }


    /**
     * Draws a textured rectangle from a region in a 256x256 texture.
     *
     * <p>The Z coordinate of the rectangle is {@link #zOffset}.
     *
     * <p>The width and height of the region are the same as
     * the dimensions of the rectangle.
     *
     * @param height   the height
     * @param v        the top-most coordinate of the texture region
     * @param width    the width
     * @param y        the Y coordinate of the rectangle
     * @param u        the left-most coordinate of the texture region
     * @param matrices the matrix stack used for rendering
     * @param x        the X coordinate of the rectangle
     */
//    public void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
//        drawTexture(matrices, x, y, 1, (float) u, (float) v, width, height, 256, 256);
//    }
//
//    /**
//     * Draws a textured rectangle from a region in a texture.
//     *
//     * <p>The width and height of the region are the same as
//     * the dimensions of the rectangle.
//     *
//     * @param u             the left-most coordinate of the texture region
//     * @param z             the Z coordinate of the rectangle
//     * @param y             the Y coordinate of the rectangle
//     * @param x             the X coordinate of the rectangle
//     * @param matrices      the matrix stack used for rendering
//     * @param textureHeight the height of the entire texture
//     * @param textureWidth  the width of the entire texture
//     * @param height        the height of the rectangle
//     * @param width         the width of the rectangle
//     * @param v             the top-most coordinate of the texture region
//     */
//    public static void drawTexture(MatrixStack matrices, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
//        drawTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
//    }
//
//    //regionWidth is @param width
//    //textureWidth is @param textureWidth
//    private static void drawTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
//        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0F) / (float) textureWidth, (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) regionHeight) / (float) textureHeight);
//    }
//    //x0 is @param x
//    //x1 is @param x + @param width
//    //y0 is @param y
//    //y1 is @param y + @param height
//    //z is @param z
//
//    private static void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
//        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//        bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).texture(u0, v1).next();
//        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).texture(u1, v1).next();
//        bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).texture(u1, v0).next();
//        bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).texture(u0, v0).next();
//        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
//    }


}
