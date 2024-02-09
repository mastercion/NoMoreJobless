package com.example;

import net.minecraft.client.util.math.MatrixStack;

public interface CustomScreenMod {
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta);
}
