package dev.chimera.nemean;

public interface Renderable {

    void render();

    default void registerRenderable(){
        GuiLayer.registerRenderable(this);
    }

}
