package dev.chimera.client.gui.clickgui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.chimera.client.gui.AbstractComponent;
import dev.chimera.client.modules.AbstractModule;
import dev.chimera.client.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;

import java.util.List;

public class ButtonGroup extends AbstractComponent {
    private List<AbstractModule> children = ModuleManager.getAllModules();

    private List<ModuleButton> moduleButtons;

    public float marginY = 2f;
    public float marginX = 2f;

    public ButtonGroup(float x, float y, float width, float height, List<ModuleButton> moduleButtons) {
//        this.children = children;

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.moduleButtons = moduleButtons;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //-1 is argb white
        this.drawBackground(-1);

        float yCursor = getY() + marginY;

        for (ModuleButton button : moduleButtons) {
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
        for (ModuleButton moduleButton : moduleButtons) {
            if (moduleButton.isWithinBounds(x, y)) {
                moduleButton.onMouseClick(x, y, mouseButton);
            }
        }
    }

    private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private void renderTile(DrawContext context, float x, float y, float width, float height, AbstractModule module) {
        if (textRenderer == null) {
            textRenderer = MinecraftClient.getInstance().textRenderer;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        //triangle one

        bufferBuilder.vertex(x + width, y + height, 1f).color(0f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(x + width, y, 1f).color(0f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(x, y, 1f).color(0f, 0f, 0f, .5f).next();

        //triangle two
        bufferBuilder.vertex(x + width, y + height, 1f).color(0f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(x, y, 1f).color(0f, 0f, 0f, .5f).next();
        bufferBuilder.vertex(x, y + height, 1f).color(0f, 0f, 0f, .5f).next();

        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();

        context.drawText(textRenderer, module.MODULE_NAME, (int) x + 1, (int) y + 2, 0xff007f, false);
    }

}
