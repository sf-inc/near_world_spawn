package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.NearWorldSpawn;
import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow public abstract ServerChunkManager getChunkManager();
    @Shadow @NotNull public abstract MinecraftServer getServer();

    @WrapOperation(method = "setSpawnPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;removeTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"))
    private <T> void updateRemovedChunkPos(ServerChunkManager instance, ChunkTicketType<T> ticketType, ChunkPos pos,
                                           int radius, T argument, Operation<Void> original) {
        if (ModConfig.get().fixedLoadedChunks && !NearWorldSpawn.needChunkUpdate) return;

        ChunkPos oldPos = !ModConfig.get().fixedLoadedChunks && NearWorldSpawn.needChunkUpdate
                ? new ChunkPos(BlockPos.ORIGIN) : pos;
        original.call(instance, ticketType, oldPos, radius, argument);
    }

    @WrapOperation(method = "setSpawnPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;addTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"))
    private <T> void updateAddedChunkPos(ServerChunkManager instance, ChunkTicketType<T> ticketType, ChunkPos pos,
                                           int radius, T argument, Operation<Void> original) {
        if (ModConfig.get().fixedLoadedChunks && !NearWorldSpawn.needChunkUpdate) return;

        ChunkPos newPos = ModConfig.get().fixedLoadedChunks ? new ChunkPos(BlockPos.ORIGIN) : pos;
        original.call(instance, ticketType, newPos, radius, argument);
        NearWorldSpawn.needChunkUpdate = false;
    }
}
