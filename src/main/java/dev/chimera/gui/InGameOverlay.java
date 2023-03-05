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
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBInternalformatQuery2;
import org.lwjgl.opengl.GL11C;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.glTexImage2D;

public class InGameOverlay {
    Identifier overlay = null;
    NativeImageBackedTexture texture = null;
    public static Panel SCREEN = new Panel();

    final static ExecutorService service = Executors.newCachedThreadPool();
    static NativeImage frame = null;
    static boolean frameAvailable = false;
    static boolean rendering = false;
    static final Object lock = new Object();
    // they can be
    static class ProcessTask implements Runnable {
        @Override
        public void run() {
            try {
                Size size = new Size(new Value(MinecraftClient.getInstance().getWindow().getWidth(), Value.ValueType.Pixel),
                        new Value(MinecraftClient.getInstance().getWindow().getHeight(), Value.ValueType.Pixel));
                synchronized (lock) {
                    if (!SCREEN.hasUpdated(size)) {
                        System.out.println("Skipping render!");
                        return;
                    }
                    rendering = true;
                }
                NativeImage nativeImage = null;
                BufferedImage image = null;
                synchronized (SCREEN) {
                    image = SCREEN.render(size);
                }

                try {
                    nativeImage = toNativeImage(image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                synchronized (lock) {
                    frame = nativeImage;
                    frameAvailable = true;
                    rendering = false;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Unexpected exception occurred in render thread!");
                synchronized (lock) {
                    rendering = false;
                }
            }
        }
    }

    public InGameOverlay(){
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            Window window = MinecraftClient.getInstance().getWindow();

            synchronized (lock)
            {
                if(frameAvailable) {
                    frameAvailable = false;
                    if (texture == null || texture.getImage().getWidth() != frame.getWidth() || texture.getImage().getHeight() != frame.getHeight()) {
                        texture = new NativeImageBackedTexture(frame);
                        overlay = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("overlay", texture);
                    }
                    else {
                        texture.setImage(frame);
                        texture.upload();
                    }
                }
                if(overlay == null)
                    return;
            }

            RenderSystem.setShaderTexture(0, overlay);

            DrawableHelper.drawTexture(matrices, 0,0, 0,0,
                    window.getScaledWidth(), window.getScaledHeight(),
                    window.getScaledWidth(), window.getScaledHeight());
        });

        ClientTickEvents.START_CLIENT_TICK.register((minecraftClient) -> {
            synchronized (lock){
                if(!rendering) {
                    service.submit(new ProcessTask());
                    //rendering = true;
                    //System.out.println("Requesting render!");
                }
                else {
                    System.out.println("RENDERING!");
                }
            }
        });
    }

    private static NativeImage toNativeImage(BufferedImage image) throws IOException {
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), false);
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                int rgb = image.getRGB(x,y);
                int a = (rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;


                int out = ((a & 0xFF) << 24) |
                        ((b & 0xFF) << 16) |
                        ((g & 0xFF) << 8)  |
                        ((r & 0xFF));

                nativeImage.setColor(x, y, out);
            }
        }
        return nativeImage;
    }

}
