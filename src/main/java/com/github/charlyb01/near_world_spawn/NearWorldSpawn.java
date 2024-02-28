package com.github.charlyb01.near_world_spawn;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.github.charlyb01.near_world_spawn.config.PlayerInfluence;
import com.github.charlyb01.near_world_spawn.config.AreaShape;
import com.github.charlyb01.near_world_spawn.config.Utils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public class NearWorldSpawn implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("worldspawn-modded")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printChangingArea)
                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                            .executes(context -> Utils.setChangingArea(BoolArgumentType.getBool(context, "enabled"), context)))
            );
            dispatcher.register(CommandManager.literal("worldspawn")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printShapeInfluenceAndExpand)
                    .then(CommandManager.literal("box")
                            .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                    .then(CommandManager.literal("none")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.BOX, PlayerInfluence.NONE, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_offset")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.BOX, PlayerInfluence.AREA_OFFSET, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_shrink")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.BOX, PlayerInfluence.AREA_SHRINK, IntegerArgumentType.getInteger(context, "expand"), context)))))
                    .then(CommandManager.literal("inner_circle")
                            .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                    .then(CommandManager.literal("none")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.INNER_CIRCLE, PlayerInfluence.NONE, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_offset")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.INNER_CIRCLE, PlayerInfluence.AREA_OFFSET, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_shrink")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.INNER_CIRCLE, PlayerInfluence.AREA_SHRINK, IntegerArgumentType.getInteger(context, "expand"), context)))))
                    .then(CommandManager.literal("outer_circle")
                            .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                    .then(CommandManager.literal("none")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.OUTER_CIRCLE, PlayerInfluence.NONE, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_offset")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.OUTER_CIRCLE, PlayerInfluence.AREA_OFFSET, IntegerArgumentType.getInteger(context, "expand"), context)))
                                    .then(CommandManager.literal("area_shrink")
                                            .executes(context -> Utils.setShapeInfluenceAndExpand(AreaShape.OUTER_CIRCLE, PlayerInfluence.AREA_SHRINK, IntegerArgumentType.getInteger(context, "expand"), context)))))
            );
            dispatcher.register(CommandManager.literal("worldspawn-areashape")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printAreaShape)
                    .then(CommandManager.literal("box")
                            .executes(context -> Utils.setAreaShape(AreaShape.BOX, context)))
                    .then(CommandManager.literal("inner_circle")
                            .executes(context -> Utils.setAreaShape(AreaShape.INNER_CIRCLE, context)))
                    .then(CommandManager.literal("outer_circle")
                            .executes(context -> Utils.setAreaShape(AreaShape.OUTER_CIRCLE, context)))
            );
            dispatcher.register(CommandManager.literal("worldspawn-expand")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printExpand)
                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                            .executes(context -> Utils.setExpand(IntegerArgumentType.getInteger(context, "expand"), context)))
            );
            dispatcher.register(CommandManager.literal("worldspawn-playerinfluence")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printPlayerInfluence)
                    .then(CommandManager.literal("none")
                            .executes(context -> Utils.setPlayerInfluence(PlayerInfluence.NONE, context)))
                    .then(CommandManager.literal("area_offset")
                            .executes(context -> Utils.setPlayerInfluence(PlayerInfluence.AREA_OFFSET, context)))
                    .then(CommandManager.literal("area_shrink")
                            .executes(context -> Utils.setPlayerInfluence(PlayerInfluence.AREA_SHRINK, context)))
            );
            dispatcher.register(CommandManager.literal("spawnchunks")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printFixedLoadedChunks)
                    .then(CommandManager.literal("fixed")
                            .executes(context -> Utils.setFixedLoadedChunks(true, context)))
                    .then(CommandManager.literal("worldspawn")
                            .executes(context -> Utils.setFixedLoadedChunks(false, context)))
            );
        });
    }
}
