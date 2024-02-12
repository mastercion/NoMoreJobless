package com.example;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class DeleteArmorStandsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deleteSavedPosArmorStands")
                .requires(source -> source.hasPermissionLevel(2)) // Requires OP level 2
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    Box worldBox = new Box(-30000000, 0, -30000000, 30000000, 256, 30000000);
                    long count = source.getWorld().getEntitiesByClass(ArmorStandEntity.class, worldBox,
                            armorStand -> armorStand.getCustomName() != null && armorStand.getCustomName().getString().startsWith("SavedPos_")
                    ).stream().peek(armorStand -> armorStand.remove(Entity.RemovalReason.DISCARDED)).count(); // Count removed armor stands

                    if (count > 0) {
                        source.sendFeedback(() -> Text.literal("All 'SavedPos_' ArmorStands have been deleted. Total: " + count), true);
                        // After attempting to remove the armor stands
                        source.getWorld().getEntitiesByClass(ArmorStandEntity.class, worldBox,
                                armorStand -> armorStand.getCustomName() != null && armorStand.getCustomName().getString().startsWith("SavedPos_")
                        ).forEach(armorStand -> {
                            System.out.println("Removing armor stand: " + armorStand.getCustomName().getString());
                            armorStand.remove(Entity.RemovalReason.DISCARDED);
                        });
                    } else {
                        source.sendFeedback(() -> Text.literal("No 'SavedPos_' ArmorStands found to delete."), true);
                    }
                    return (int) count; // Return the number of armor stands deleted as the command execution result
                }));
    }
}
