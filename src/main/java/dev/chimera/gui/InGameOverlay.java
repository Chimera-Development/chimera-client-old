package dev.chimera.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.gui.components.Panel;
import dev.chimera.gui.types.Size;
import dev.chimera.gui.types.Value;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;

public class InGameOverlay {
    Identifier overlay = null;
    NativeImageBackedTexture texture = null;
    public Panel SCREEN = new Panel();

    public InGameOverlay(){
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            Window window = MinecraftClient.getInstance().getWindow();

            render();

            RenderSystem.setShaderTexture(0, overlay);

            DrawableHelper.drawTexture(matrices, 0,0, 0,0,
                    window.getScaledWidth(), window.getScaledHeight(),
                    window.getScaledWidth(), window.getScaledHeight());
        });
    }

    public void render()
    {
        Size size = new Size(new Value(MinecraftClient.getInstance().getWindow().getWidth(), Value.ValueType.Pixel),
                new Value(MinecraftClient.getInstance().getWindow().getHeight(), Value.ValueType.Pixel));

        if (!SCREEN.hasUpdated(size))
            return;

        BufferedImage image = SCREEN.render(size);
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), false);
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                nativeImage.setColor(x, y, image.getRGB(x,y));
            }
        }
        if(texture == null || texture.getImage().getWidth() != nativeImage.getWidth() || texture.getImage().getHeight() != nativeImage.getHeight())
        {
            texture = new NativeImageBackedTexture(nativeImage);
            overlay = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("overlay", texture);

            return;
        }
        texture.setImage(nativeImage);
        texture.upload();
    }
}
