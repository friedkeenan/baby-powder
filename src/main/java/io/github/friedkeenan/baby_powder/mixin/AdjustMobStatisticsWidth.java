package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import io.github.friedkeenan.baby_powder.MobStatisticsListTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSelectionList;

@Mixin(AbstractSelectionList.class)
@Environment(EnvType.CLIENT)
public class AdjustMobStatisticsWidth {
    private static final int ADDED_WIDTH = 70;

    @ModifyReturnValue(at = @At("RETURN"), method = "getRowWidth")
    private int increaseRowWidth(int original) {
        if (this instanceof MobStatisticsListTag) {
            return original + ADDED_WIDTH;
        }

        return original;
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getScrollbarPosition")
    private int increaseScrollbarPosiiton(int original) {
        if (this instanceof MobStatisticsListTag) {
            return original + ADDED_WIDTH / 2;
        }

        return original;
    }
}
