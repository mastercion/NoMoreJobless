package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public interface CustomScreenInf {
    void renderBackground(DrawContext drawContext);

    void render(DrawContext drawContext, MatrixStack matrices, int mouseX, int mouseY, float delta);
}
