package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.NearWorldSpawn;
import com.github.charlyb01.near_world_spawn.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyExpressionValue(method = "prepareStartRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getSpawnPos()Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos updateLoadedChunkPosition(BlockPos blockPos) {
        NearWorldSpawn.needChunkUpdate = true;
        return ModConfig.get().fixedLoadedChunks ? BlockPos.ORIGIN : blockPos;
    }
}
