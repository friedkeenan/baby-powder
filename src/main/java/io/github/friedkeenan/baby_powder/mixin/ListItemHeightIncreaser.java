package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import io.github.friedkeenan.baby_powder.ItemHeightIncreaser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSelectionList;

@Mixin(AbstractSelectionList.class)
@Environment(EnvType.CLIENT)
public class ListItemHeightIncreaser implements ItemHeightIncreaser {
    @Mutable
    @Final
    @Shadow
    private int itemHeight;

    public void increaseItemheight(int additional_height) {
        this.itemHeight += additional_height;
    }
}
