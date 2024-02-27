package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyVariable(method = "prepareStartRegion", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;getSpawnPos()Lnet/minecraft/util/math/BlockPos;"), ordinal = 0)
    private BlockPos updateLoadedChunkPosition(BlockPos blockPos) {
        return ModConfig.get().fixedLoadedChunks ? BlockPos.ORIGIN : blockPos;
    }
}
