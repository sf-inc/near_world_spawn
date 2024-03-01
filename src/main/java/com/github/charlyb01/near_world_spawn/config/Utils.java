package com.github.charlyb01.near_world_spawn.config;

import com.github.charlyb01.near_world_spawn.NearWorldSpawn;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Utils {
    private Utils() {

    }

    public static int setChangingArea(boolean changing, CommandContext<ServerCommandSource> context) {
        ModConfig.get().changingArea = changing;
        ModConfig.save();
        return printChangingArea(context) + 1;
    }

    public static int setAreaShape(AreaShape shape, CommandContext<ServerCommandSource> context) {
        ModConfig.get().areaShape = shape;
        ModConfig.save();
        return printAreaShape(context) + 1;
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

    public static int setShapeInfluenceAndExpand(AreaShape shape, PlayerInfluence influence, int expand, CommandContext<ServerCommandSource> context) {
        ModConfig.get().areaShape = shape;
        ModConfig.get().playerInfluence = influence;
        ModConfig.get().expand = expand;
        ModConfig.save();
        return printShapeInfluenceAndExpand(context) + 1;
    }

    public static int setFixedLoadedChunks(boolean fixed, CommandContext<ServerCommandSource> context) {
        if (ModConfig.get().fixedLoadedChunks != fixed) {
            ModConfig.get().fixedLoadedChunks = fixed;
            ModConfig.save();
            NearWorldSpawn.needChunkUpdate = !NearWorldSpawn.needChunkUpdate;
        }
        return printFixedLoadedChunks(context) + 1;
    }

    public static int printChangingArea(CommandContext<ServerCommandSource> context) {
        String changing = ModConfig.get().changingArea
                ? "changing area around players"
                : "not changing (vanilla)";
        context.getSource().sendFeedback(
                () -> Text.literal("World spawn is %s"
                        .formatted(changing)),
                true);

        return 0;
    }

    public static int printAreaShape(CommandContext<ServerCommandSource> context) {
        if (!ModConfig.get().changingArea) {
            return printChangingArea(context);
        }

        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn area shape set to %s"
                        .formatted(ModConfig.get().areaShape.name)),
                true);

        return 0;
    }

    public static int printPlayerInfluence(CommandContext<ServerCommandSource> context) {
        if (!ModConfig.get().changingArea) {
            return printChangingArea(context);
        }

        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn player influence set to %s"
                        .formatted(ModConfig.get().playerInfluence.name)),
                true);

        return 0;
    }

    public static int printExpand(CommandContext<ServerCommandSource> context) {
        if (!ModConfig.get().changingArea) {
            return printChangingArea(context);
        }

        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn expand set to %s"
                        .formatted(ModConfig.get().expand)),
                true);

        return 0;
    }

    public static int printShapeInfluenceAndExpand(CommandContext<ServerCommandSource> context) {
        if (!ModConfig.get().changingArea) {
            return printChangingArea(context);
        }

        context.getSource().sendFeedback(
                () -> Text.literal("Worldspawn area shape set to %s widened by %s blocks with a player influence set to %s"
                        .formatted(ModConfig.get().areaShape.name, ModConfig.get().expand, ModConfig.get().playerInfluence.name)),
                true);

        return 0;
    }

    public static int printFixedLoadedChunks(CommandContext<ServerCommandSource> context) {
        String fixed = ModConfig.get().fixedLoadedChunks ? "fixed (0,0,0)" : "world spawn";
        context.getSource().sendFeedback(
                () -> Text.literal("Spawn chunks set to %s"
                        .formatted(fixed)),
                true);

        return 0;
    }
}
