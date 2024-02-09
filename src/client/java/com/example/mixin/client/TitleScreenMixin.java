package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void injectCustomButton(int y, int spacingY, CallbackInfo info) {
        int l = this.height / 4 + 48;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        String playerName = "Player"; // Default name in case session is null

        if (minecraftClient.getSession() != null) {
            playerName = minecraftClient.getSession().getUsername(); // Correct way to get the session's username
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal(playerName), (button) -> {
            this.client.setScreen(new SelectWorldScreen(this));
        }).dimensions(this.width / 2 + 128, l + 72 + 12, 80, 20).build());
    }


}