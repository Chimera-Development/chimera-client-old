package dev.chimera.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.chimera.ChimeraClient.mc;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin extends Screen {
    protected InventoryScreenMixin(Text title) {
        super(title);
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
    @Inject(method = "drawBackground",at=@At("TAIL"))
    public void renderLogo(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci){
        render(matrices);
    }

    private void render(MatrixStack matrixStack){
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND);
        RenderSystem.setShaderTexture(0, texture);
        int window_height = mc.getWindow().getHeight();
        int window_width = mc.getWindow().getWidth();
//        if(x >= window_width - 72 || x < 0){
//            xVelocity = xVelocity * -1;
//        }
//        x = x - xVelocity;

        if(y >= window_height - 18 || y < 0){
            yVelocity = yVelocity * -1;
        }
        y = y + yVelocity;
        DrawableHelper.drawTexture(matrixStack, window_width/2, y, 0, 0, 72, 18, 72, 18);
    }
}
