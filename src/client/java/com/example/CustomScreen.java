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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomScreen extends Screen implements CustomScreenInf {
    private ButtonWidget button1;
    private ButtonWidget button2;
    private ButtonWidget button3;

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
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.world != null && client.player != null) {
                        client.world.getEntities().forEach(entity -> {
                            if (entity instanceof ArmorStandEntity) {
                                ArmorStandEntity armorStand = (ArmorStandEntity) entity;
                                if (armorStand.hasCustomName() && armorStand.getCustomName().getString().startsWith("SavedPos_")) {
                                    // Fetching the ArmorStand's position
                                    BlockPos pos = armorStand.getBlockPos();
                                    String command = "/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
                                    String message = String.format("Click to teleport to %s at X:%d Y:%d Z:%d",
                                            armorStand.getCustomName().getString(),
                                            pos.getX(), pos.getY(), pos.getZ());

                                    // Create a text component with click event for teleportation
                                    Text teleportText = Text.literal(message).styled(style ->
                                            style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Teleport to ArmorStand"))));

                                    // Sending the clickable message to the player
                                    client.player.sendMessage(teleportText, false);
                                }
                            }
                        });
                    }
                })
                .position(this.width / 2 - 205, 20)
                .dimensions(200, 20,200,20)
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
    public void renderBackground(DrawContext drawContext) {
        assert this.client != null;
        if (this.client.world != null) {
            drawContext.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.renderBackgroundTexture(drawContext);
        }
    }


    public void renderBackgroundTexture(DrawContext context) {
        context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        boolean i = true;
        context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void render(DrawContext drawContext, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}


