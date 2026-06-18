package com.keiserx01.totalmovement.mixin;

import net.minecraft.scoreboard.ScoreboardCriterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScoreboardCriterion.class)
public interface ScoreboardCriterionAccessor {
    @Invoker("create")
    static ScoreboardCriterion TotalMovement$create(String name) {
        throw new AssertionError();
    }
}
