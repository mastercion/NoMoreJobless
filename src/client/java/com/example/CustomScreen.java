package com.example;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.client.gui.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;

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

        // Inside your CustomScreen class' init method where you define button1
        button1 = ButtonWidget.builder(Text.translatable("button.customscreen.buttonone"), button -> {
                    // Action for Button 1
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.world != null) {
                        for (Entity entity : client.world.getEntities()) {
                            if (entity instanceof ArmorStandEntity) {
                                ArmorStandEntity armorStand = (ArmorStandEntity) entity;
                                if (armorStand.hasCustomName()) {
                                    Text customName = armorStand.getCustomName();
                                    if (customName != null && customName.getString().startsWith("SavedPos_")) {
                                        client.player.sendMessage(Text.literal(customName.getString()), false);
                                    }
                                }
                            }
                        }
                    }
                })
                .position(this.width / 2 - 205, 20) // Adjust position as needed
                .dimensions(200, 20, 200, 20) // Adjust dimensions as needed
                .tooltip(Tooltip.of(Text.literal("Prints all named armor stands starting with 'savedpos_'")))
                .build();

        // Inside your CustomScreen class' init method where you define button2
        button2 = ButtonWidget.builder(Text.translatable("button.customscreen.buttontwo"), button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.world != null) {
                        List<Entity> entitiesToRemove = new ArrayList<>();
                        for (Entity entity : client.world.getEntities()) {
                            if (entity instanceof ArmorStandEntity) {
                                ArmorStandEntity armorStand = (ArmorStandEntity) entity;
                                if (armorStand.hasCustomName()) {
                                    Text customName = armorStand.getCustomName();
                                    if (customName != null && customName.getString().startsWith("SavedPos_")) {
                                        entitiesToRemove.add(armorStand);
                                    }
                                }
                            }
                        }
                        entitiesToRemove.forEach(entity -> {
                            // On the client side, this won't work to actually remove entities in a normal game
                            // For demonstration purposes only
                            client.world.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
                        });
                    }
                })
                .position(this.width / 2 + 5, startY) // Adjust position as needed
                .dimensions(200, 40, 200, 20) // Adjust dimensions as needed
                .tooltip(Tooltip.of(Text.literal("Deletes all armor stands starting with 'SavedPos_'")))
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


