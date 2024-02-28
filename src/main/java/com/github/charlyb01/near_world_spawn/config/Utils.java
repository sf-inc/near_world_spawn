package com.github.charlyb01.near_world_spawn.config;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Utils {
    private Utils() {

    }

    public static int setSpawnType(SpawnType type, CommandContext<ServerCommandSource> context) {
        ModConfig.get().spawnType = type;
        ModConfig.save();
        return printSpawnType(context) + 1;
    }

    public static int setPlayerInfluence(PlayerInfluence influence, CommandContext<ServerCommandSource> context) {
        ModConfig.get().playerInfluence = influence;
        ModConfig.save();
        return printPlayerInfluence(context) + 1;
    }

    public static int setExpand(int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().expand = expand;
        ModConfig.save();
        return printExpand(context) + 1;
    }

    public static int setTypeInfluenceAndExpand(SpawnType type, PlayerInfluence influence, int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().spawnType = type;
        ModConfig.get().playerInfluence = influence;
        ModConfig.get().expand = expand;
        ModConfig.save();
        return printTypeInfluenceAndExpand(context) + 1;
    }

    public static int printSpawnType(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn type set to %s"
                        .formatted(ModConfig.get().spawnType.name)),
                true);

        return 0;
    }

    public static int printPlayerInfluence(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn player influence set to %s"
                        .formatted(ModConfig.get().playerInfluence.name)),
                true);

        return 0;
    }

    public static int printExpand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn expand set to %s"
                        .formatted(ModConfig.get().expand)),
                true);

        return 0;
    }

    public static int printTypeInfluenceAndExpand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(
                () -> {
                    Text text;
                    if (ModConfig.get().spawnType.equals(SpawnType.VANILLA)) {
                        text = Text.literal("Worldspawn type set to %s"
                                .formatted(ModConfig.get().spawnType.name));
                    } else {
                        text = Text.literal("Worldspawn type set to %s widened by %s blocks with a player influence set to %s"
                                .formatted(ModConfig.get().spawnType.name, ModConfig.get().expand, ModConfig.get().playerInfluence.name));
                    }
                    return text;
                },
                true);

        return 0;
    }
}
