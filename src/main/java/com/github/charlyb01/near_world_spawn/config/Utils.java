package com.github.charlyb01.near_world_spawn.config;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Utils {
    private Utils() {

    }

    public static int setVanillaType(CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.VANILLA;
        return printConfig(context);
    }

    public static int setBoxType(CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.BOX;
        return printConfig(context);
    }

    public static int setCircleType(CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.CIRCLE;
        return printConfig(context);
    }

    public static int setOffset(boolean offset, CommandContext<ServerCommandSource> context) {
        ModConfig.get().offset = offset;
        return printConfig(context);
    }

    public static int setOffsetBoxType(boolean offset, CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.BOX;
        ModConfig.get().offset = offset;
        return printConfig(context);
    }

    public static int setOffsetCircleType(boolean offset, CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.CIRCLE;
        ModConfig.get().offset = offset;
        return printConfig(context);
    }

    public static int setExpand(int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().expand = expand;
        return printConfig(context);
    }

    public static int setExpandOffsetBoxType(boolean offset, int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.BOX;
        ModConfig.get().offset = offset;
        ModConfig.get().expand = expand;
        return printConfig(context);
    }

    public static int setExpandOffsetCircleType(boolean offset, int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().type = SpawnType.CIRCLE;
        ModConfig.get().offset = offset;
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
                    if (ModConfig.get().type.equals(SpawnType.VANILLA)) {
                        text = Text.literal("Worldspawn set to %s"
                                .formatted(ModConfig.get().type.name));
                    } else {
                        String weighted = ModConfig.get().offset ? "a weighted" : "an absolute";
                        text = Text.literal("Worldspawn set to %s widened by %s blocks with %s center"
                                .formatted(ModConfig.get().type.name, ModConfig.get().expand, weighted));
                    }
                    return text;
                },
                true);

        return save ? 1 : 0;
    }
}
