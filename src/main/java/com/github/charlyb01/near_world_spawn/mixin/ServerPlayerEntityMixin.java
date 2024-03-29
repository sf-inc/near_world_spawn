package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.github.charlyb01.near_world_spawn.config.PlayerInfluence;
import com.github.charlyb01.near_world_spawn.config.AreaShape;
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
        if (!ModConfig.get().changingArea) return;

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

        int offsetX = weightedCenterX - center.getX();
        int offsetZ = weightedCenterZ - center.getZ();
        if (ModConfig.get().playerInfluence.equals(PlayerInfluence.AREA_OFFSET)) {
            minX += offsetX;
            maxX += offsetX;
            minZ += offsetZ;
            maxZ += offsetZ;
            center = new BlockPos(weightedCenterX, 0, weightedCenterZ);
        } else if (ModConfig.get().playerInfluence.equals(PlayerInfluence.AREA_SHRINK)) {
            if (offsetX > 0) minX += 2 * offsetX;
            else maxX += 2 * offsetX;
            if (offsetZ > 0) minZ += 2 * offsetZ;
            else maxZ += 2 * offsetZ;
            center = new BlockPos(weightedCenterX, 0, weightedCenterZ);
        }

        if (ModConfig.get().expand > 0) {
            minX -= ModConfig.get().expand;
            maxX += ModConfig.get().expand;
            minZ -= ModConfig.get().expand;
            maxZ += ModConfig.get().expand;
        }

        int lengthX = maxX - minX;
        int lengthZ = maxZ - minZ;
        boolean isCircleType = ModConfig.get().areaShape.isCircle();

        if (isCircleType) {
            int preferredLength = ModConfig.get().areaShape.equals(AreaShape.INNER_CIRCLE)
                    ? Math.min(lengthX, lengthZ)
                    : Math.max(lengthX, lengthZ);
            if (lengthX == preferredLength) {
                lengthZ = preferredLength;
                minZ = center.getZ() - preferredLength / 2;
                maxZ = center.getZ() + preferredLength / 2;
            } else {
                lengthX = preferredLength;
                minX = center.getX() - preferredLength / 2;
                maxX = center.getX() + preferredLength / 2;
            }
        }

        int radius = lengthX / 2;
        int count = Math.min(lengthX * lengthZ, 1000);

        for (BlockPos blockPos : BlockPos.iterateRandomly(this.getServerWorld().getRandom(), count,
                minX, 0, minZ, maxX, 0, maxZ)) {
            if (isCircleType && !blockPos.isWithinDistance(center, radius)) continue;

            BlockPos blockPos2 = SpawnLocating.findOverworldSpawn(world, blockPos.getX(), blockPos.getZ());
            if (blockPos2 == null) continue;

            serverWorld.setSpawnPos(blockPos2, 0.f);
            break;
        }
    }
}
