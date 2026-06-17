package com.albiondungeons.totalmovement.mixin;

import com.albiondungeons.totalmovement.AlbionTotalMovementMod;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin {
    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", at = @At("TAIL"))
    private void albionTotalMovement$afterSimpleTeleport(
            ServerWorld world,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            CallbackInfo ci
    ) {
        AlbionTotalMovementMod.resetMovementSample((ServerPlayerEntity) (Object) this);
    }
}
