package com.example;

import com.example.CustomScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ModClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("tutorial"); // Replace "modid" with your actual mod ID
	private static KeyBinding openCustomScreenKeyBinding;

	@Override
	public void onInitializeClient() {
		// Initialize and register the keybinding
		openCustomScreenKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.modid.open_custom_screen", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard
				GLFW.GLFW_KEY_B, // The keycode of the 'B' key
				"category.modid.custom" // The translation key of the keybinding's category
		));

		// Register a client tick event to check the keybinding state
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openCustomScreenKeyBinding.wasPressed()) {
				if (MinecraftClient.getInstance().currentScreen == null) { // Ensure no other screen is displayed
					MinecraftClient.getInstance().setScreen(new CustomScreen(Text.literal("Custom UI")));
					LOGGER.info("Opening Custom UI with B"); // Updated log message
				}
			}
		});
	}
}
