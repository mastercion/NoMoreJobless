package com.example;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakEventListener {
    private static final Map<String, Integer> playerBlockCounts = new HashMap<>();
    private static final Map<String, Integer> playerBlockCoalCounts = new HashMap<>();

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> onBlockBreak(world, (ServerPlayerEntity) player, pos, state, entity));
    }

    private static void onBlockBreak(World world, ServerPlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        // Log to console for debugging
        System.out.println("onBlockBreak called (BlockState)");

        // Get the block from the state
        Block block = state.getBlock();

        if (block == Blocks.STONE) {
            handleStoneBlockBreak(player);
        }

        if (block == Blocks.COAL_ORE) {
            handleORECOALBlockBreak(player);
        }
    }

    private static void onBlockBreak(World world, ServerPlayerEntity player, BlockPos pos, Block block, Entity entity) {
        // Log to console for debugging
        System.out.println("onBlockBreak called (Block)");

        if (block == Blocks.STONE) {
            handleStoneBlockBreak(player);
        }
        if (block == Blocks.COAL_ORE) {
            handleORECOALBlockBreak(player);
        }
    }

    private static void handleORECOALBlockBreak(ServerPlayerEntity player) {
        // Get the player's name
        String playerName = player.getName().getString();

        // Increment the player's block count
        playerBlockCoalCounts.put(playerName, playerBlockCoalCounts.getOrDefault(playerName, 0) + 1);

        // Log to console for debugging
        System.out.println(playerName + " mined a stone block! Total Coal Ore blocks mined: " + playerBlockCoalCounts.get(playerName));

        // Send a message to the player
        player.sendMessage(Text.of("You mined a stone block! Total Coal Ore blocks mined: " + playerBlockCoalCounts.get(playerName)), false);
    }

    private static void handleStoneBlockBreak(ServerPlayerEntity player) {
        // Get the player's name
        String playerName = player.getName().getString();

        // Increment the player's block count
        playerBlockCounts.put(playerName, playerBlockCounts.getOrDefault(playerName, 0) + 1);

        // Log to console for debugging
        System.out.println(playerName + " mined a stone block! Total stone blocks mined: " + playerBlockCounts.get(playerName));

        // Send a message to the player
        player.sendMessage(Text.of("You mined a stone block! Total stone blocks mined: " + playerBlockCounts.get(playerName)), false);
    }
}
