package com.albiondungeons.totalmovement;

import com.albiondungeons.totalmovement.mixin.ScoreboardCriterionAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class AlbionTotalMovementMod implements ModInitializer {
    public static final String MOD_ID = "albion_total_movement";
    public static final String CRITERION_NAME = "total_movement";
    public static final ScoreboardCriterion TOTAL_MOVEMENT =
            ScoreboardCriterionAccessor.albionTotalMovement$create(CRITERION_NAME);

    private static final MovementTracker MOVEMENT_TRACKER = new MovementTracker(TOTAL_MOVEMENT);

    @Override
    public void onInitialize() {
        // Loading this class registers TOTAL_MOVEMENT as a vanilla scoreboard criterion.
    }

    public static void tick(MinecraftServer server) {
        MOVEMENT_TRACKER.tick(server);
    }

    public static void resetMovementSample(ServerPlayerEntity player) {
        MOVEMENT_TRACKER.reset(player);
    }
}
