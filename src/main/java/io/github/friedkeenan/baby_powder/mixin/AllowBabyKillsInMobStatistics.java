package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.friedkeenan.baby_powder.ItemHeightIncreaser;
import io.github.friedkeenan.baby_powder.StatsScreenGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.achievement.StatsScreen;

/*
    This should just be an accessor mixin, but sadly
    when it is written that way it raises a compile error
    *only* when compiling. It runs fine when running through vscode.
*/
@Mixin(targets = {"net/minecraft/client/gui/screens/achievement/StatsScreen$MobsStatisticsList"})
@Environment(EnvType.CLIENT)
public class AllowBabyKillsInMobStatistics implements StatsScreenGetter {
    @Shadow
    @Final
    private StatsScreen field_18763;

    /* Synthetic field from being a nested class. */
    public StatsScreen getStatsScreen() {
        return this.field_18763;
    }

    private Font getFont() {
        return ((ScreenAccessor) this.getStatsScreen()).getFont();
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void constructor(StatsScreen stats_screen, Minecraft minecraft, CallbackInfo info) {
        /* Add enough vertical space for two more lines. */
        ((ItemHeightIncreaser) this).increaseItemheight(this.getFont().lineHeight * 2);

        /*
            NOTE: Currently text can run off the right edge
            of the screen and thus not be highlighted correctly.

            It would be nice to fix this, but I think the simple,
            non-invasive solutions would end up looking weird.
        */
    }
}
