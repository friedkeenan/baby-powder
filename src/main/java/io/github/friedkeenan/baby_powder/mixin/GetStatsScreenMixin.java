package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.friedkeenan.baby_powder.StatsScreenGetter;
import net.minecraft.client.gui.screens.achievement.StatsScreen;

@Mixin(targets = {"net/minecraft/client/gui/screens/achievement/StatsScreen$MobsStatisticsList"})
public class GetStatsScreenMixin implements StatsScreenGetter {
    @Shadow
    @Final
    private StatsScreen field_18763;

    /* Synthetic field from being a nested class. */
    public StatsScreen getStatsScreen() {
        return this.field_18763;
    }
}
