package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.NearWorldSpawn;
import com.github.charlyb01.near_world_spawn.config.ModConfig;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow public abstract ServerChunkManager getChunkManager();
    @Shadow @NotNull public abstract MinecraftServer getServer();

    @Inject(method = "setSpawnPos", at = @At("HEAD"))
    private void updateRightChunks(BlockPos pos, float angle, CallbackInfo ci) {
        ChunkPos oldPos = ModConfig.get().fixedLoadedChunks
                ? new ChunkPos(this.getSpawnPos())
                : new ChunkPos(BlockPos.ORIGIN);
        ChunkPos newPos= ModConfig.get().fixedLoadedChunks
                ? new ChunkPos(BlockPos.ORIGIN)
                : new ChunkPos(pos);

        this.getChunkManager().removeTicket(ChunkTicketType.START, oldPos, 11, Unit.INSTANCE);
        this.getChunkManager().addTicket(ChunkTicketType.START, newPos, 11, Unit.INSTANCE);
        this.getServer().getPlayerManager().sendToAll(new PlayerSpawnPositionS2CPacket(pos, angle));
    }

    @Inject(method = "setSpawnPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;removeTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"), cancellable = true)
    private void avoidUnnecessaryChunksUpdate(BlockPos pos, float angle, CallbackInfo ci) {
        if (!NearWorldSpawn.needChunkUpdate) {
            if (ModConfig.get().fixedLoadedChunks) {
                ci.cancel();
            }
            return;
        }

        NearWorldSpawn.needChunkUpdate = false;
        ci.cancel();
    }
}
