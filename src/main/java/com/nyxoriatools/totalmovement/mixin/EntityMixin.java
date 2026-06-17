package com.albiondungeons.totalmovement.mixin;

import java.util.Set;

import com.albiondungeons.totalmovement.AlbionTotalMovementMod;
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
    private void albionTotalMovement$afterRequestTeleport(double destX, double destY, double destZ, CallbackInfo ci) {
        albionTotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "requestTeleportAndDismount(DDD)V", at = @At("TAIL"))
    private void albionTotalMovement$afterRequestTeleportAndDismount(
            double destX,
            double destY,
            double destZ,
            CallbackInfo ci
    ) {
        albionTotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "requestTeleportOffset(DDD)V", at = @At("TAIL"))
    private void albionTotalMovement$afterRequestTeleportOffset(
            double offsetX,
            double offsetY,
            double offsetZ,
            CallbackInfo ci
    ) {
        albionTotalMovement$resetIfServerPlayer();
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At("RETURN"))
    private void albionTotalMovement$afterFlaggedTeleport(
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
            albionTotalMovement$resetIfServerPlayer();
        }
    }

    private void albionTotalMovement$resetIfServerPlayer() {
        if ((Object) this instanceof ServerPlayerEntity player) {
            AlbionTotalMovementMod.resetMovementSample(player);
        }
    }
}
