package dev.chimera.nemean;

import imgui.ImGui;

public class ImGuiLayer {
    private boolean showText = false;
    public void imgui(){
        ImGui.begin("cool window");

        if(ImGui.button("I am a button")){
            showText = true;
        }
        if (showText){
            ImGui.text("You clicked a button");
            ImGui.sameLine();
            if(ImGui.button("Stop showing text")){
                showText = false;
            }
        }
        ImGui.end();
    }
}
