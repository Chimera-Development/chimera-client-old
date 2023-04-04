package dev.chimera.nemean.elements;

import dev.chimera.ChimeraClient;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import dev.chimera.modules.ModuleInitializer;
import dev.chimera.nemean.GuiLayer;
import dev.chimera.nemean.Renderable;
import imgui.ImFont;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import dev.chimera.nemean.ImGui;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class ClickGui extends Screen implements Renderable {

    public static ClickGui INSTANCE;
    public boolean isActive = false;
    public ClickGui() {
        super(Text.of("ClickGUI"));
        INSTANCE = this;
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
        this.registerRenderable();
    }

    @Override
    public void close() {
        isActive = false;
        Module module = Objects.requireNonNull(ModuleInitializer.findModule("ClickGUI"));
        module.toggle();
        super.close();
    }

    boolean clickedBurrito = false;
    int burrId = -999;
    int burrW = 0;
    int burrH = 0;
    public void loadImage()
            //maybe it is fun challenge, also turns out image loading is slight pain so might abstract (bcs image loading is opengl level)
    {
        BufferedImage decoderOrg = null;
        try {
            decoderOrg = ImageIO.read(getClass().getResourceAsStream("/trollface.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage image = new BufferedImage(decoderOrg.getWidth(), decoderOrg.getHeight(), BufferedImage.TYPE_INT_ARGB);

        image.createGraphics().drawImage(decoderOrg, 0, 0, image.getWidth(), image.getHeight(), null);

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(4*image.getWidth()*image.getHeight());

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:
        int textureID = glGenTextures(); //Generate texture ID
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        //Setup texture scaling filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11. GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        //Send texel data to OpenGL
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        burrId = textureID;
        burrW = image.getWidth();
        burrH = image.getHeight();
    }

    public boolean burrito()
    {
        ImVec2 start = ImGui.getCursorPos();

        if (burrId == -999)
            loadImage();

        ImGui.image(burrId, burrW, burrH);
        //ImGui.text("Test");

        ImVec2 end = ImGui.getCursorPos();
        ImGui.sameLine();
        end.x = ImGui.getCursorPosX();

        ChimeraClient.LOGGER.info(start.x + " " + start.y);
        ChimeraClient.LOGGER.info(end.x + " " + end.y);

        ImGui.setCursorPos(start.x, start.y);
        if(ImGui.invisibleButton("burclick", end.x-start.x, end.y-start.y))
            clickedBurrito = !clickedBurrito;
        return clickedBurrito;
    }

    @Override
    public void render() {
        if (!this.isActive)
            return;

        HashMap<ModuleCategory, ArrayList<Module>> categorized = new HashMap<>();

        ModuleInitializer.getAllModules().forEach((module) -> {
            if(!categorized.containsKey(module.getModuleCategory()))
                categorized.put(module.getModuleCategory(), new ArrayList<>());
            categorized.get(module.getModuleCategory()).add(module);
        });

        // Sort modules alphabetically
        for(ArrayList<Module> modulesInCategory : categorized.values())
            modulesInCategory.sort(Comparator.comparing(Module::getModuleName));

        ImGui.frame(() -> {

            //ImVec2 workSize = ImGui.getIO().getDisplaySize();
            ImGuiViewport viewport = ImGui.getMainViewport();
            /*if(workSize.x <= 0 || workSize.y <= 0)
            {
                return;
            }*/

            //ImGui.setNextWindowPos(viewport.getWorkPosX(), viewport.getWorkPosY());
            //ImGui.setNextWindowSize(viewport.getWorkSizeX(), viewport.getWorkSizeY());

            // full screen nonresizable window

            //ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f);

            /*ImGui.begin("FillWindow", new ImBoolean(true), ImGuiWindowFlags.NoFocusOnAppearing); // ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoResize
            ImGui.text("a");
            ImGui.end();
            */
            // do you want click gui inside of window?
            // no not really

            //if(burrito())
            {
              //  ImGui.text("Clicked burrito!");
            }
            for (Map.Entry<ModuleCategory, ArrayList<Module>> entry : categorized.entrySet()) {
                ImGui.window(entry.getKey().getName(), () -> {
                    for (Module module : entry.getValue()) {
                        if (ImGui.checkbox(module.getModuleName(), module.getModuleEnabled())) {
                            module.toggle();
                        }
                    }
                });
            }
            //ImGui.popStyleVar();
        });
    }
}
