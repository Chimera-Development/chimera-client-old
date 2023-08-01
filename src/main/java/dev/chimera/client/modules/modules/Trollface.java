package dev.chimera.client.modules.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import static dev.chimera.client.ChimeraClient.MOD_ID;

public class Trollface extends AbstractModule {

    public Trollface() {
        super(new Identifier(MOD_ID, "trollface"), "TrollFace");

        this.enable();
    }

    private int coordX = 10;
    private int coordY = 10;

    private boolean movingDown = false;
    private boolean movingRight = false;

    public void render(DrawContext drawContext) {
        RenderSystem.disableDepthTest();

        Identifier identifier = new Identifier(MOD_ID, "trollface.png");

        checkedMove();
        drawContext.drawTexture(identifier, coordX, coordY, 0, 0, 256, 256);

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
            } else {
                movingDown = !movingDown;
            }
        }
        if (!movingDown) {
            if (isInBoundsY(this.coordY - 1)) {
                this.coordY--;
            } else {
                movingDown = !movingDown;
            }
        }

        if (movingRight) {
            if (isInBoundsX(this.coordX + 256 + 1)) {
                this.coordX++;
            } else {
                movingRight = !movingRight;
            }
        }
        if (!movingRight) {
            if (isInBoundsX(this.coordX - 1)) {
                this.coordX--;
            } else {
                movingRight = !movingRight;
            }
        }
    }

}
