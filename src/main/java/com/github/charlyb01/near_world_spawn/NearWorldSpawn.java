package com.github.charlyb01.near_world_spawn;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal("worldspawn")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> Utils.printConfig(context, false))
                        .then(CommandManager.argument("offset", BoolArgumentType.bool())
                                .executes(context -> Utils.setOffset(BoolArgumentType.getBool(context, "offset"), context)))
                        .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                .executes(context -> Utils.setExpand(IntegerArgumentType.getInteger(context, "expand"), context)))
                        .then(CommandManager.literal("vanilla")
                                .executes(Utils::setVanillaType))
                        .then(CommandManager.literal("box")
                                .executes(Utils::setBoxType)
                                .then(CommandManager.argument("offset", BoolArgumentType.bool())
                                        .executes(context -> Utils.setOffsetBoxType(BoolArgumentType.getBool(context, "offset"), context))
                                        .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                                .executes(context -> Utils.setExpandOffsetBoxType(BoolArgumentType.getBool(context, "offset"), IntegerArgumentType.getInteger(context, "expand"), context)))))
                        .then(CommandManager.literal("circle")
                                .executes(Utils::setCircleType)
                                .then(CommandManager.argument("offset", BoolArgumentType.bool())
                                        .executes(context -> Utils.setOffsetCircleType(BoolArgumentType.getBool(context, "offset"), context))
                                        .then(CommandManager.argument("expand", IntegerArgumentType.integer(0))
                                                .executes(context -> Utils.setExpandOffsetCircleType(BoolArgumentType.getBool(context, "offset"), IntegerArgumentType.getInteger(context, "expand"), context)))))
                ));
    }
}
