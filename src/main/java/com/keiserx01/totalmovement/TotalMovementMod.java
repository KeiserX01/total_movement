package com.keiserx01.totalmovement;

import com.keiserx01.totalmovement.mixin.ScoreboardCriterionAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class TotalMovementMod implements ModInitializer {
    public static final String MOD_ID = "total_movement";
    public static final String CRITERION_NAME = "total_movement";
    public static final ScoreboardCriterion TOTAL_MOVEMENT =
            ScoreboardCriterionAccessor.TotalMovement$create(CRITERION_NAME);

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
