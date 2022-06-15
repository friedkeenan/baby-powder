package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.achievement.StatsScreen;

@Mixin(targets = {"net/minecraft/client/gui/screens/achievement/StatsScreen$MobsStatisticsList"})
public interface MobsStatisticsListAccessor {
    /* Synthetic field from being a nested class. */
    @Accessor("field_18763")
    StatsScreen getStatsScreen();
}
