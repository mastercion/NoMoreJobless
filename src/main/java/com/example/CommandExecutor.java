package com.example;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandExecutor {

    private static DataPackContents server;

    /**
     * Executes a command on the server.
     *
     * @param source  The command source executing the command.
     * @param command The command string to execute.
     * @return The result of the command execution.
     */
    public static int executeCommand(ServerCommandSource source, String command) {
        CommandManager commandManager = server.getCommandManager();

        // Parse the command
        ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(command, source);

        // Execute the parsed command
        try {
            return commandManager.getDispatcher().execute(parseResults);
        } catch (CommandSyntaxException e) {
            source.sendError(Text.literal("Command syntax error: " + e.getMessage()));
            return 0;
        }
    }
}
