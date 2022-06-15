package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
@Environment(EnvType.CLIENT)
public interface ScreenAccessor {
    @Accessor
    Font getFont();
}
