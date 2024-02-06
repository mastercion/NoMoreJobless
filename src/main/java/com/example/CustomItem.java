package com.example;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CustomItem extends Item {


    public CustomItem(Settings settings) {
        super(settings);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public void removeOldArmorStand(ServerWorld world, PlayerEntity player) {
        String customName = "SavedPos_" + player.getName().getString();
        List<ArmorStandEntity> armorStands = (List<ArmorStandEntity>) world.getEntitiesByType(EntityType.ARMOR_STAND,
                e -> e.getCustomName() != null && e.getCustomName().getString().equals(customName));

        for (ArmorStandEntity armorStand : armorStands) {
            armorStand.remove(Entity.RemovalReason.DISCARDED); // Use the appropriate RemovalReason for your context
        }
    }


    public void spawnInvisibleArmorStand(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
            armorStand.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            armorStand.setInvisible(true);
            armorStand.setNoGravity(true);
            armorStand.setInvulnerable(true);
            armorStand.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
            String customName = "SavedPos_" + player.getName().getString();
            armorStand.setCustomName(Text.literal(customName));
            armorStand.setCustomNameVisible(false);
            if (world instanceof ServerWorld) {
                ((ServerWorld) world).spawnEntity(armorStand);
            }
        }
    }





    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        BlockPos pos = playerEntity.getBlockPos();

        if (!world.isClient && hand == Hand.MAIN_HAND && playerEntity.isSneaking()) {
            ServerWorld serverWorld = (ServerWorld) world;
            removeOldArmorStand(serverWorld, playerEntity);
            setSavedCoordinates(pos);
            spawnInvisibleArmorStand(world, pos, playerEntity);
        }



            if (hand == Hand.MAIN_HAND && playerEntity.isSneaking()) {
                setSavedCoordinates(pos);
                LOGGER.info("Saved Pos:" + pos);
                playerEntity.playSound(SoundEvents.ITEM_CROP_PLANT, 1.0F, 1.0F);
                spawnInvisibleArmorStand(world, pos, playerEntity);
                //spawnInvisibleArmorStand(serverWorld, player.getBlockPos(), player);
                playerEntity.playSound(SoundEvents.ITEM_CROP_PLANT, 1.0F, 1.0F); // Play saving position sound

                if (!world.isClient) { // Ensure we are on the server side
                    playerEntity.sendMessage(Text.literal("Position gespeichert A: " + pos.toShortString()).formatted(Formatting.GOLD), false);
                    ServerWorld serverWorld = (ServerWorld) world;
                }

                return TypedActionResult.success(stack);
            } else {
                BlockPos savedPos = getSavedCoordinates();
                if (savedPos != null) {
                    LOGGER.info("Teleporting to:" + savedPos);
                    // Perform the teleportation first
                    playerEntity.teleport(savedPos.getX() + 0.5, savedPos.getY(), savedPos.getZ() + 0.5);

                    if (!world.isClient) { // Ensure we are on the server side
                        ServerWorld serverWorld = (ServerWorld) world;
                        spawnTeleportParticles(savedPos, serverWorld);
                        String message = "Du hast dein 1x Teleport benutzt. Das Item ist nun verschwunden";
                        playerEntity.sendMessage(Text.literal(message).formatted(Formatting.RED), false);

                        // Play the sound after teleporting to ensure it's heard at the new location
                        serverWorld.playSound(null, savedPos.getX() + 0.5, savedPos.getY(), savedPos.getZ() + 0.5, SoundEvents.ITEM_TRIDENT_RETURN, playerEntity.getSoundCategory(), 1.0F, 1.0F);
                    }

                    if (!world.isClient) { // Ensure we are on the server side
                        // Teleportation logic here

                        // Remove the item after teleportation
                        if (!playerEntity.getAbilities().creativeMode) { // Check if the player is not in creative mode
                            stack.decrement(1); // Decrease the item count by 1, effectively removing it if it's the last one
                        }
                    }


                    return TypedActionResult.success(stack);
                }
            }


        return TypedActionResult.pass(stack);
    }

    public void spawnInvisibleArmorStand(ServerWorld world, BlockPos pos, PlayerEntity player) {
        ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
        armorStand.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        armorStand.setInvisible(true);
        armorStand.setNoGravity(true);
        armorStand.setInvulnerable(true);
        armorStand.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));

        // Set custom name with player's name
        String customName = "TeleportSave" + player.getName().getString();
        armorStand.setCustomName(Text.literal(customName));

        // Prepare custom NBT data
        NbtCompound customTag = new NbtCompound();
        customTag.putBoolean("CustomOutline", true); // Example custom tag
        armorStand.readNbt(customTag); // This method does not exist for setting custom data directly. Use a workaround.

        world.spawnEntity(armorStand);
    }


    private void spawnTeleportParticles(BlockPos pos, ServerWorld world) {
        for (int i = 0; i < 20; i++) {
            double offsetX = world.random.nextDouble() - 0.5;
            double offsetY = world.random.nextDouble() * 2.0;
            double offsetZ = world.random.nextDouble() - 0.5;
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5 + offsetX,
                    pos.getY() + 0.5 + offsetY,
                    pos.getZ() + 0.5 + offsetZ,
                    1,
                    0, 0, 0,
                    0.0);
        }
    }


    private static BlockPos savedCoordinates = null;

    private ServerPlayerEntity player;

    private void myplayer() {
        if (player != null) {
            // Maybe use in shop expensive but one time teleport
            player.sendMessage(Text.of("Teleporting..."), false);
        }
    }


    private static void clearSavedCoordinates() {
        savedCoordinates = null; // Clear the saved coordinates
    }

    private static BlockPos setSavedCoordinates(BlockPos pos) {
        savedCoordinates = pos; // Set the saved coordinates
        return savedCoordinates; // Return the saved coordinates
    }

    private static BlockPos getSavedCoordinates() {
        return savedCoordinates; // Retrieve the saved coordinates
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        // default white text
        tooltip.add(Text.translatable("Shift + Right Click to save Position").formatted(Formatting.GRAY));

        // formatted red text
        tooltip.add(Text.translatable("Right Click to Teleport (1x)").formatted(Formatting.GOLD));
    }
}
