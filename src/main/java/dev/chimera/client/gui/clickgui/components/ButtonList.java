package dev.chimera.client.gui.clickgui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.gui.AbstractComponent;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;

import java.util.List;

public class ButtonList extends AbstractComponent {

    private List<ModuleButton> buttons;

    public float marginY = 2f;
    public float marginX = 2f;

    public ButtonList(float x, float y, float width, float height, List<ModuleButton> buttons) {
//        this.children = children;

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.buttons = buttons;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //-1 is argb white
        this.drawBackground(-1);

        float yCursor = getY() + marginY;

        for (ModuleButton button : buttons) {
            button.setX(getX() + marginX);
            button.setY(yCursor);
            button.setWidth(getWidth() - marginX * 2);
            button.render(context, mouseX, mouseY, delta);

            yCursor += button.getHeight() + marginY;
        }
    }

    @Override
    public void onMouseClick(double x, double y, int mouseButton) {
        super.onMouseClick(x, y, mouseButton);
        for (ModuleButton moduleButton : buttons) {
            if (moduleButton.isWithinBounds(x, y)) {
                moduleButton.onMouseClick(x, y, mouseButton);
            }
        }
    }
}
