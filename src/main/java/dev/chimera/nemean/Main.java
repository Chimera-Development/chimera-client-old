package dev.chimera.nemean;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class Main extends Application {

    public long WINDOW_PTR;

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
        WINDOW_PTR = this.getHandle();
    }

    @Override
    public void process() {
        ImGui.text("Hello, World!");
    }

    public static void main(String[] args) {
        launch(new Main());
    }
}
