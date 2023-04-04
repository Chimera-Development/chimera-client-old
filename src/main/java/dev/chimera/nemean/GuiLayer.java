package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.GuiRenderEvent;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class GuiLayer {
    private static final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();

    private static long windowPtr;

    public static ImFont titleFont;
    public static ImFont contentFont;

    private static ArrayList<Renderable> renderStack = new ArrayList<>();

    public GuiLayer() {
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    public static void config(long windowPtr) {
        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init();

        ranAlready = true;
    }

    public static void onGlfwInit(long windowPtr) {
        initializeImGui();
        implGlfw.init(windowPtr, true);
        implGl3.init();
        GuiLayer.windowPtr = windowPtr;
    }

    private static void initializeImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);
        io.setConfigWindowsMoveFromTitleBarOnly(true);

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        //fontAtlas.addFontDefault();
        // Im trying to make imgui look nicer to make enderschesi happy
        InputStream is = GuiLayer.class.getResourceAsStream("/OpenSans-Medium.ttf");
        try {
            byte[] bytes = is.readAllBytes();
            contentFont = fontAtlas.addFontFromMemoryTTF(bytes, 20);

            titleFont = fontAtlas.addFontFromMemoryTTF(bytes, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        fontConfig.setPixelSnapH(true);

        fontConfig.destroy();


        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();

            style.setWindowMenuButtonPosition(-1);
            style.setColor(ImGuiCol.WindowBg,
                    0, 0, 0, 100);
            // Ill check
            style.setColor(ImGuiCol.TitleBgActive,
                    0, 0, 0, 100 );
            style.setColor(ImGuiCol.TitleBg,
                    0, 0, 0, 100);

            style.setColor(ImGuiCol.Header,
                    100, 100, 100, 120);

            style.setColor(ImGuiCol.Border, 0);
            style.setColor(ImGuiCol.BorderShadow, 0);

            style.setColor(ImGuiCol.FrameBg,100, 100, 100, 120);

            style.setColor(ImGuiCol.ResizeGrip , 0);
            style.setWindowRounding(5f);
            style.setFrameRounding(5f);
        }
    }

    private static boolean ranAlready = false;

    public static void registerRenderable(Renderable e) {
        renderStack.add(e);
    }

    @EventListener(id = EventListenerIDs.onRender)
    public void onRender(GuiRenderEvent event) {
        ImGui.getIO().setDisplaySize(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
        MinecraftClient.getInstance().getProfiler().push("ChimeraHUD");
        implGlfw.newFrame();
        ImGui.frame(() -> {

            for (Renderable renderable : renderStack) {

                renderable.render();

            }

            MinecraftClient.getInstance().getProfiler().pop();
            // I know this does it, its just this code I accidentally left endframe (well render in this case)
        });
        drawFrame(windowPtr);
    }

    private static void drawFrame(long windowPtr) {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        implGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        //glfwSwapBuffers(windowPtr);
        //glfwPollEvents();
    }
}
