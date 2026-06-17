package com.albiondungeons.totalmovement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

final class MovementTracker {
    private static final double SCORE_UNITS_PER_BLOCK = 10.0D;

    /*
     * Ignore large position discontinuities that did not pass through the
     * teleport hooks, such as ender pearl or third-party teleport updates.
     */
    private static final double MAX_TRACKED_BLOCKS_PER_TICK = 16.0D;

    private final ScoreboardCriterion criterion;
    private final Map<UUID, Sample> samples = new HashMap<>();

    MovementTracker(ScoreboardCriterion criterion) {
        this.criterion = criterion;
    }

    void tick(MinecraftServer server) {
        if (server.getCurrentPlayerCount() == 0) {
            samples.clear();
            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            update(player);
        }

        pruneOfflinePlayers(server);
    }

    void reset(ServerPlayerEntity player) {
        samples.put(player.getUuid(), Sample.at(player.getPos()));
    }

    void forget(ServerPlayerEntity player) {
        samples.remove(player.getUuid());
    }

    private void update(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        Vec3d currentPos = player.getPos();
        Sample sample = samples.get(uuid);

        if (sample == null) {
            samples.put(uuid, Sample.at(currentPos));
            return;
        }

        double deltaX = currentPos.x - sample.x;
        double deltaY = currentPos.y - sample.y;
        double deltaZ = currentPos.z - sample.z;
        double distanceSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

        sample.setPosition(currentPos);

        if (distanceSquared <= 0.0D) {
            return;
        }

        if (distanceSquared > MAX_TRACKED_BLOCKS_PER_TICK) {
            sample.pendingScoreUnits = 0.0D;
            return;
        }

        sample.pendingScoreUnits += Math.sqrt(distanceSquared) * SCORE_UNITS_PER_BLOCK;
        int wholeScoreUnits = (int) sample.pendingScoreUnits;

        if (wholeScoreUnits <= 0) {
            return;
        }

        sample.pendingScoreUnits -= wholeScoreUnits;
        player.getScoreboard().forEachScore(criterion, player, score -> increment(score, wholeScoreUnits));
    }

    private static void increment(ScoreAccess score, int amount) {
        score.incrementScore(amount);
    }

    private void pruneOfflinePlayers(MinecraftServer server) {
        if (samples.size() <= server.getCurrentPlayerCount()) {
            return;
        }

        Iterator<UUID> iterator = samples.keySet().iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            if (server.getPlayerManager().getPlayer(uuid) == null) {
                iterator.remove();
            }
        }
    }

    private static final class Sample {
        private double x;
        private double y;
        private double z;
        private double pendingScoreUnits;

        private static Sample at(Vec3d pos) {
            Sample sample = new Sample();
            sample.setPosition(pos);
            return sample;
        }

        private void setPosition(Vec3d pos) {
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
        }
    }
}
