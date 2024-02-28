package com.github.charlyb01.near_world_spawn.config;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Utils {
    private Utils() {

    }

    public static int setSpawnType(SpawnType type, CommandContext<ServerCommandSource> context) {
        ModConfig.get().spawnType = type;
        return printConfig(context);
    }

    public static int setPlayerInfluence(PlayerInfluence influence, CommandContext<ServerCommandSource> context) {
        ModConfig.get().playerInfluence = influence;
        return printConfig(context);
    }

    public static int setTypeAndInfluence(SpawnType type, PlayerInfluence influence, CommandContext<ServerCommandSource> context) {
        ModConfig.get().spawnType = type;
        ModConfig.get().playerInfluence = influence;
        return printConfig(context);
    }

    public static int setExpand(int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().expand = expand;
        return printConfig(context);
    }

    public static int setTypeInfluenceAndExpand(SpawnType type, PlayerInfluence influence, int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().spawnType = type;
        ModConfig.get().playerInfluence = influence;
        ModConfig.get().expand = expand;
        return printConfig(context);
    }

    public static int printConfig(CommandContext<ServerCommandSource> context) {
        return printConfig(context, true);
    }

    public static int printConfig(CommandContext<ServerCommandSource> context, boolean save) {
        if (save) {
            ModConfig.save();
        }

        context.getSource().sendFeedback(
                () -> {
                    Text text;
                    if (ModConfig.get().spawnType.equals(SpawnType.VANILLA)) {
                        text = Text.literal("Worldspawn set to %s"
                                .formatted(ModConfig.get().spawnType.name));
                    } else {
                        text = Text.literal("Worldspawn set to %s widened by %s blocks with a player influence set to %s"
                                .formatted(ModConfig.get().spawnType.name, ModConfig.get().expand, ModConfig.get().playerInfluence));
                    }
                    return text;
                },
                true);

        return save ? 1 : 0;
    }
}
