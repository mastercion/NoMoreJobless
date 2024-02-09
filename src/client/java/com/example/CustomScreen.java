package com.example;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.client.gui.tooltip.Tooltip;

@Environment(EnvType.CLIENT)
public class CustomScreen extends Screen implements CustomScreenInf {
    private ButtonWidget button1;
    private ButtonWidget button2;

    protected CustomScreen(MutableText customUi) {
        super(Text.literal("Custom UI"));
    }

    @Override
    protected void init() {
        super.init();
        // Calculate button positions dynamically based on screen width
        int startY = this.height / 4 + 48; // Example starting Y position

        button1 = ButtonWidget.builder(Text.literal("Button 1"), button -> {
                    // Action for Button 1
                    System.out.println("Button 1 clicked!");
                })
                .position(this.width / 2 - 205, startY) // Position for Button 1
                .dimensions(200, 20, 20, 20) // Size of Button 1
                .tooltip(Tooltip.of(Text.literal("Tooltip for Button 1"))) // Tooltip for Button 1
                .build();

        button2 = ButtonWidget.builder(Text.literal("Button 2"), button -> {
                    // Action for Button 2
                    System.out.println("Button 2 clicked!");
                })
                .position(this.width / 2 + 5, startY) // Position for Button 2
                .dimensions(200, 20, 20, 20) // Size of Button 2
                .tooltip(Tooltip.of(Text.literal("Tooltip for Button 2"))) // Tooltip for Button 2
                .build();

        // Add buttons to the screen
        addDrawableChild(button1);
        addDrawableChild(button2);
    }

    @Override
    public void render(DrawContext drawContext, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext); // Render the background of the screen
        super.render(drawContext, mouseX, mouseY, delta); // Render buttons and other components
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF); // Draw the screen title
    }

    private void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text title, int i, int i1, int i2) {
    }
}


