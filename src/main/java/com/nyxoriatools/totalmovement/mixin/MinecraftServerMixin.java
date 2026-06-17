package com.albiondungeons.totalmovement.mixin;

import java.util.function.BooleanSupplier;

import com.albiondungeons.totalmovement.AlbionTotalMovementMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin {
    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"))
    private void albionTotalMovement$afterServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        AlbionTotalMovementMod.tick((MinecraftServer) (Object) this);
    }
}
