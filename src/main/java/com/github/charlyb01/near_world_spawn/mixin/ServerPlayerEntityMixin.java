package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.config.AreaShape;
import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.github.charlyb01.near_world_spawn.config.PlayerInfluence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow public abstract @Nullable BlockPos getSpawnPointPosition();

    @Unique
    private static final int MAX_ITERATION = 1000;

    @Inject(method = "moveToSpawn", at = @At("HEAD"))
    private void updateWorldSpawn(ServerWorld world, CallbackInfo ci) {
        if (this.getSpawnPointPosition() != null) return;
        if (!ModConfig.get().changingArea) return;

        ServerPlayerEntity thisPlayer = (ServerPlayerEntity)(Object) this;
        ServerWorld serverWorld = world.getServer().getOverworld();

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

        int centerX = (minX + maxX) / 2;
        int centerZ = (minZ + maxZ) / 2;
        int offsetX = weightedCenterX - centerX;
        int offsetZ = weightedCenterZ - centerZ;

        if (ModConfig.get().playerInfluence.equals(PlayerInfluence.AREA_OFFSET)) {
            minX += offsetX;
            maxX += offsetX;
            minZ += offsetZ;
            maxZ += offsetZ;

            centerX = weightedCenterX;
            centerZ = weightedCenterZ;
        } else if (ModConfig.get().playerInfluence.equals(PlayerInfluence.AREA_SHRINK)) {
            if (offsetX > 0) minX += 2 * offsetX;
            else maxX += 2 * offsetX;
            if (offsetZ > 0) minZ += 2 * offsetZ;
            else maxZ += 2 * offsetZ;

            centerX = weightedCenterX;
            centerZ = weightedCenterZ;
        }

        if (ModConfig.get().expand > 0) {
            minX -= ModConfig.get().expand;
            maxX += ModConfig.get().expand;
            minZ -= ModConfig.get().expand;
            maxZ += ModConfig.get().expand;
        }

        if (ModConfig.get().areaShape.equals(AreaShape.BOX)) {
            this.setSpawnPosBox(minX, minZ, maxX, maxZ, serverWorld);
        } else {
            this.setSpawnPosCircle(minX, minZ, maxX, maxZ, centerX, centerZ, serverWorld);
        }
    }

    @Unique
    private void setSpawnPosBox(int minX, int minZ, int maxX, int maxZ, ServerWorld serverWorld) {
        for (BlockPos blockPos : BlockPos.iterateRandomly(serverWorld.getRandom(), MAX_ITERATION,
                minX, 0, minZ, maxX, 0, maxZ)) {
            BlockPos spawnPos = SpawnLocating.findOverworldSpawn(serverWorld, blockPos.getX(), blockPos.getZ());
            if (spawnPos == null) continue;

            serverWorld.setSpawnPos(spawnPos, 0.f);
            break;
        }
    }

    @Unique
    private void setSpawnPosCircle(int minX, int minZ, int maxX, int maxZ, int centerX, int centerZ,
                                   ServerWorld serverWorld) {
        double radius = Math.max(maxX - minX, maxZ - minZ) / 2.0;

        for (int i = 0; i < MAX_ITERATION; ++i) {
            double r = radius * Math.sqrt(serverWorld.getRandom().nextDouble());
            double theta = 2.0 * Math.PI * serverWorld.getRandom().nextDouble();
            int x = (int) (r * Math.cos(theta));
            int z = (int) (r * Math.sin(theta));

            BlockPos spawnPos = SpawnLocating.findOverworldSpawn(serverWorld, centerX + x, centerZ + z);
            if (spawnPos == null) continue;

            serverWorld.setSpawnPos(spawnPos, 0.f);
            break;
        }
    }
}
