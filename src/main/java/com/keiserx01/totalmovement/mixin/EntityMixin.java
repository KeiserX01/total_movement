package com.keiserx01.totalmovement.mixin;

import java.util.Set;

import com.keiserx01.totalmovement.TotalMovementMod;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class EntityMixin {
    @Inject(method = "requestTeleport(DDD)V", at = @At("TAIL"))
    private void TotalMovement$afterRequestTeleport(double destX, double destY, double destZ, CallbackInfo ci) {
        TotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "requestTeleportAndDismount(DDD)V", at = @At("TAIL"))
    private void TotalMovement$afterRequestTeleportAndDismount(
            double destX,
            double destY,
            double destZ,
            CallbackInfo ci
    ) {
        TotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "requestTeleportOffset(DDD)V", at = @At("TAIL"))
    private void TotalMovement$afterRequestTeleportOffset(
            double offsetX,
            double offsetY,
            double offsetZ,
            CallbackInfo ci
    ) {
        TotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At("RETURN"))
    private void TotalMovement$afterFlaggedTeleport(
            ServerWorld world,
            double x,
            double y,
            double z,
            Set<PositionFlag> movementFlags,
            float yaw,
            float pitch,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValueZ()) {
            TotalMovement$resetIfServerPlayer();
        }
    }

    private void TotalMovement$resetIfServerPlayer() {
        if ((Object) this instanceof ServerPlayerEntity player) {
            TotalMovementMod.resetMovementSample(player);
        }
    }
}
