package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.stats.StatsCounter;

@Mixin(StatsScreen.class)
@Environment(EnvType.CLIENT)
public interface StatsScreenAccessor {
    @Accessor
    StatsCounter getStats();
}
