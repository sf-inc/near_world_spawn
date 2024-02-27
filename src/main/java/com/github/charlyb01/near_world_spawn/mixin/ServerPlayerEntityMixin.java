package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.github.charlyb01.near_world_spawn.config.SpawnType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow public abstract @Nullable BlockPos getSpawnPointPosition();

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "moveToSpawn", at = @At("HEAD"))
    private void updateWorldSpawn(ServerWorld world, CallbackInfo ci) {
        if (this.getSpawnPointPosition() != null) return;
        if (ModConfig.get().type.equals(SpawnType.VANILLA)) return;

        ServerPlayerEntity thisPlayer = (ServerPlayerEntity)(Object) this;
        ServerWorld serverWorld = this.getServerWorld().getServer().getOverworld();

        Integer minX = null, minZ = null;
        Integer maxX = null, maxZ = null;
        int weightedCenterX = 0, weightedCenterZ = 0;
        int nbPlayers = 0;

        for (ServerPlayerEntity player : serverWorld.getPlayers(LivingEntity::isAlive)) {
            if (player == thisPlayer) continue;

            weightedCenterX += player.getBlockX();
            weightedCenterZ += player.getBlockZ();
            ++nbPlayers;

            if (minX == null) {
                minX = maxX = player.getBlockX();
                minZ = maxZ = player.getBlockZ();
            } else {
                minX = Math.min(minX, player.getBlockX());
                minZ = Math.min(minZ, player.getBlockZ());
                maxX = Math.max(maxX, player.getBlockX());
                maxZ = Math.max(maxZ, player.getBlockZ());
            }
        }

        if (minX == null) return;

        weightedCenterX /= nbPlayers;
        weightedCenterZ /= nbPlayers;
        BlockPos center = new BlockPos((minX + maxX) / 2, 0, (minZ + maxZ) / 2);

        if (ModConfig.get().offset) {
            int offsetX = weightedCenterX - center.getX();
            int offsetZ = weightedCenterZ - center.getZ();
            minX += offsetX;
            maxX += offsetX;
            minZ += offsetZ;
            maxZ += offsetZ;
            center = new BlockPos(weightedCenterX, 0, weightedCenterZ);
        }

        if (ModConfig.get().expand > 0) {
            minX -= ModConfig.get().expand;
            maxX += ModConfig.get().expand;
            minZ -= ModConfig.get().expand;
            maxZ += ModConfig.get().expand;
        }

        int count = Math.min((maxX - minX) * (maxZ - minZ), 1000);
        int radius = Math.max(maxX - minX, maxZ - minZ) / 2;
        boolean isCircle = ModConfig.get().type.equals(SpawnType.CIRCLE);

        for (BlockPos blockPos : BlockPos.iterateRandomly(this.getServerWorld().getRandom(), count,
                minX, 0, minZ, maxX, 0, maxZ)) {
            if (isCircle && !blockPos.isWithinDistance(center, radius)) continue;

            BlockPos blockPos2 = SpawnLocating.findOverworldSpawn(world, blockPos.getX(), blockPos.getZ());
            if (blockPos2 == null) continue;

            serverWorld.setSpawnPos(blockPos2, 0.f);
            break;
        }
    }
}
