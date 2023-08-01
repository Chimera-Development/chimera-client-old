package dev.chimera.client.gui.clickgui.components;

import dev.chimera.client.gui.AbstractComponent;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class ModuleButton extends AbstractComponent {

    private AbstractModule module;


    private TextRenderer textRenderer;

    public ModuleButton(float x, float y, float width, float height, AbstractModule module) {
//        this.children = children;

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.module = module;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hover = isWithinBounds(mouseX, mouseY);

        //highlight hover
        int color = hover ? -16777216 : 2080374784;

        if (textRenderer == null) {
            textRenderer = MinecraftClient.getInstance().textRenderer;
        }

        this.drawBackground(color);

        context.drawText(textRenderer, module.MODULE_NAME, (int) getX() + 1, (int) getY() + 2, 0xff007f, false);
    }

    @Override
    public void onMouseClick(double x, double y, int mouseButton) {
        super.onMouseClick(x, y, mouseButton);
        module.toggle();
    }
}
