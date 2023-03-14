package dev.chimera.amalthea;

public interface EventListenerIDs {
    String a = "chimera-client:a";

    String moduleInitializerTickEventStart = "chimera_client:moduleInitializerTickEventStart";
    String moduleInitializerTickEventEnd = "chimera_client:moduleInitializerTickEventEnd";
    String moduleInitializerKeyPress = "chimera_client:moduleInitializerKeyPress";
    String moduleInitializerKeyRelease = "chimera_client:moduleInitializerKeyRelease";

    String lwjglRendererTick = "chimera-client:lwjglRendererTick";
    String firstRenderer = "chimera-client:first-renderer";
    String lastRenderer = "chimera-client:last-renderer";

    String guiMouseButtonEvent = "chimera-client:guiMouseButtonEvent";
    String clickGuiOnKeyEvent = "chimera-client:clickGuiOnKeyEvent";
    String guiMouseMoveEvent = "chimera-client:guiMouseMoveEvent";
    String onRender = "chimera-client:onRenderListener";
}
