package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJobsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final CustomItem CUSTOM_ITEM = new CustomItem(new FabricItemSettings().maxCount(16));


	private static final String CATEGORY = "examplemod.category";

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(CUSTOM_ITEM))
			.displayName(Text.translatable("itemGroup.tutorial.test_group"))
			.entries((context, entries) -> {
				entries.add(CUSTOM_ITEM);
			})
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registries.ITEM, new Identifier("tutorial", "custom_item"), CUSTOM_ITEM);
		Registry.register(Registries.ITEM_GROUP, new Identifier("tutorial", "test_group"), ITEM_GROUP);
		BlockBreakEventListener.registerEvents();

		CommandRegistrationCallback.EVENT.register((dispatcher, environment, registrationEnvironment) -> {
			// The third parameter can be named according to what it represents, often it's a boolean indicating if it's a dedicated server.
			DeleteArmorStandsCommand.register(dispatcher);
		});


		LOGGER.info("Hello Fabric world!");
	}
}