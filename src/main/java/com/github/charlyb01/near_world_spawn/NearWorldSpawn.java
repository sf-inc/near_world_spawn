package com.github.charlyb01.near_world_spawn;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.github.charlyb01.near_world_spawn.config.PlayerInfluence;
import com.github.charlyb01.near_world_spawn.config.SpawnType;
import com.github.charlyb01.near_world_spawn.config.Utils;
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
            dispatcher.register(CommandManager.literal("worldspawn")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printTypeInfluenceAndExpand)
                    .then(CommandManager.literal("vanilla")
                            .executes(context -> Utils.setSpawnType(SpawnType.VANILLA, context)))
                    .then(CommandManager.literal("box")
                            .then(CommandManager.literal("none")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.BOX, PlayerInfluence.NONE, IntegerArgumentType.getInteger(context, "expand"), context))))
                            .then(CommandManager.literal("area_offset")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.BOX, PlayerInfluence.AREA_OFFSET, IntegerArgumentType.getInteger(context, "expand"), context))))
                            .then(CommandManager.literal("area_shrink")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.BOX, PlayerInfluence.AREA_SHRINK, IntegerArgumentType.getInteger(context, "expand"), context)))))
                    .then(CommandManager.literal("circle")
                            .then(CommandManager.literal("none")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.CIRCLE, PlayerInfluence.NONE, IntegerArgumentType.getInteger(context, "expand"), context))))
                            .then(CommandManager.literal("area_offset")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.CIRCLE, PlayerInfluence.AREA_OFFSET, IntegerArgumentType.getInteger(context, "expand"), context))))
                            .then(CommandManager.literal("area_shrink")
                                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                            .executes(context -> Utils.setTypeInfluenceAndExpand(SpawnType.CIRCLE, PlayerInfluence.AREA_SHRINK, IntegerArgumentType.getInteger(context, "expand"), context)))))
            );
            dispatcher.register(CommandManager.literal("worldspawn-type")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printSpawnType)
                    .then(CommandManager.literal("vanilla")
                            .executes(context -> Utils.setSpawnType(SpawnType.VANILLA, context)))
                    .then(CommandManager.literal("box")
                            .executes(context -> Utils.setSpawnType(SpawnType.BOX, context)))
                    .then(CommandManager.literal("circle")
                            .executes(context -> Utils.setSpawnType(SpawnType.CIRCLE, context)))
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
            dispatcher.register(CommandManager.literal("worldspawn-expand")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(Utils::printExpand)
                    .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                            .executes(context -> Utils.setExpand(IntegerArgumentType.getInteger(context, "expand"), context)))
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
