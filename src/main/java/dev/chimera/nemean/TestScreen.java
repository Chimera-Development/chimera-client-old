package dev.chimera.nemean;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
public class TestScreen extends Screen {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowPtr;
    public static TestScreen INSTANCE;

    public TestScreen(Text title) {
        super(title);
        INSTANCE = this;
        ImGui.createContext();
        init();
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    public void init(){
        initWindow();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init();
    }

    private void initWindow(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            ChimeraClient.LOGGER.error("Unable to initialize GLFW");
            System.exit(-1);
        }

//        glslVersion = "#version "
        windowPtr = MinecraftClient.getInstance().getWindow().getHandle();
        if(windowPtr == NULL){
            ChimeraClient.LOGGER.error("window is null (idk how)");
            System.exit(-1);
        }
        glfwMakeContextCurrent(windowPtr);

        GL.createCapabilities();
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();
    }


    @EventListener(id = "test")
    public void renderTick(TickEvent.Start tick){
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

        GLFW.glfwSwapBuffers(windowPtr);
        org.lwjgl.glfw.GLFW.glfwPollEvents();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

        GLFW.glfwSwapBuffers(windowPtr);
        org.lwjgl.glfw.GLFW.glfwPollEvents();
    }
}
