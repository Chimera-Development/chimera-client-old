package dev.chimera.amalthea;

public interface EventListenerIDs {
    String a = "chimera-client:a";

    String moduleInitializerTickEventStart = "chimera_client:moduleInitializerTickEventStart";
    String moduleInitializerTickEventEnd = "chimera_client:moduleInitializerTickEventEnd";
    String moduleInitializerKeyPress = "chimera_client:moduleInitializerKeyPress";
    String moduleInitializerKeyRelease = "chimera_client:moduleInitializerKeyRelease";

    String lwjglRendererTick = "chimera-client:lwjglRendererTick";
    String guiMouseButtonEvent = "chimera-client:guiMouseButtonEvent";
    String clickGuiOnKeyEvent = "chimera-client:clickGuiOnKeyEvent";
    String guiMouseMoveEvent = "chimera-client:guiMouseMoveEvent";
}
