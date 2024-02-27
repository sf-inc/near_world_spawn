package com.github.charlyb01.near_world_spawn.mixin;

import com.github.charlyb01.near_world_spawn.config.ModConfig;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "setSpawnPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;removeTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"), cancellable = true)
    private void dontUpdateLoadedChunks(BlockPos pos, float angle, CallbackInfo ci) {
        if (ModConfig.get().fixedLoadedChunks) {
            ci.cancel();
        }
    }
}
