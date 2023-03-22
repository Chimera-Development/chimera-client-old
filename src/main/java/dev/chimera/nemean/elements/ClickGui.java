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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import dev.chimera.nemean.ImGui;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    {
        BufferedImage decoder = null;
        try {
            decoder = ImageIO.read(getClass().getResourceAsStream("/trollface.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer buf = ByteBuffer.allocateDirect(3*decoder.getWidth()*decoder.getHeight());
        int byteCount = 0;
        for (int y = 0; y < decoder.getHeight(); y++)
        {
            for (int x = 0; x < decoder.getWidth(); x++)
            {
                int clr = decoder.getRGB(x, y);
                int r =   (clr & 0x00ff0000) >> 16;
                int g = (clr & 0x0000ff00) >> 8;
                int b =   clr & 0x000000ff;
                buf.put(byteCount, (byte) r);
                buf.put(byteCount+1, (byte) g);
                buf.put(byteCount+2, (byte) b);
                byteCount += 3;
            }
        }
        buf.flip();

        int texture=GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, decoder.getWidth(), decoder.getHeight(), 0,
                GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buf);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        burrId = texture;
        burrW = decoder.getWidth();
        burrH = decoder.getHeight();
    }

    public boolean burrito()
    {
        ImVec2 start = ImGui.getCursorPos();
        ImVec2 size = new ImVec2(100,100);

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
            /*ImVec2 workSize = ImGui.getIO().getDisplaySize();
            if(workSize.x <= 0 || workSize.y <= 0)
            {
                return;
            }

            ImGui.setNextWindowPos(0, 0);
            ImGui.setNextWindowSize(workSize.x, workSize.y);

            // full screen nonresizable window

            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f);

            ImGui.begin("FillWindow", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoResize);
            ImGui.text("a");
            ImGui.end();
            ImGui.popStyleVar(2);*/

            // do you want click gui inside of window?
            // no not really

            if(burrito())
            {
                ImGui.text("Clicked burrito!");
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
        });
    }
}
