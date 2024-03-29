package io.github.friedkeenan.baby_powder.mixin;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.friedkeenan.baby_powder.BabyPowderStats;
import io.github.friedkeenan.baby_powder.StatsScreenGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.EntityType;

@Mixin(targets = {"net/minecraft/client/gui/screens/achievement/StatsScreen$MobsStatisticsList$MobRow"})
@Environment(EnvType.CLIENT)
public class ShowBabyKillsInMobRowMixin {
    @Shadow
    @Final
    private Component mobName;

    private StatsScreenGetter list;

    private @Nullable Component baby_kills = null;
    private @Nullable Component babies_killed_by = null;

    private boolean has_baby_kills     = false;
    private boolean was_killed_by_baby = false;

    private StatsScreenAccessor getScreen() {
        return (StatsScreenAccessor) this.list.getStatsScreen();
    }

    private StatsCounter getStats() {
        return this.getScreen().getStats();
    }

    private Font getFont() {
        return ((ScreenAccessor) this.getScreen()).getFont();
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void createBabyKillComponents(@Coerce StatsScreenGetter mobs_statistics_list, EntityType<?> entity, CallbackInfo info) {
        this.list = mobs_statistics_list;

        final var stats = this.getStats();

        final var num_baby_kills = stats.getValue(BabyPowderStats.BABIES_KILLED.get(entity));
        if (num_baby_kills > 0) {
            this.has_baby_kills = true;
            this.baby_kills = Component.translatable("stat_type.baby_powder.babies_killed", num_baby_kills, this.mobName);
        } else {
            this.baby_kills = Component.translatable("stat_type.baby_powder.babies_killed.none", this.mobName);
        }

        final var num_babies_killed_by = stats.getValue(BabyPowderStats.BABIES_KILLED_BY.get(entity));
        if (num_babies_killed_by > 0) {
            this.was_killed_by_baby = true;
            this.babies_killed_by = Component.translatable("stat_type.baby_powder.babies_killed_by", this.mobName, num_babies_killed_by);
        } else {
            this.babies_killed_by = Component.translatable("stat_type.baby_powder.babies_killed_by.none", this.mobName);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void renderBabyKills(GuiGraphics graphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo info) {
        final var font = this.getFont();
        var height_offset = j + 1 + font.lineHeight * 3;

        graphics.drawString(font, this.baby_kills, k + 2 + 10, height_offset, this.has_baby_kills ? 0x909090 : 0x606060);

        height_offset += font.lineHeight;

        graphics.drawString(font, this.babies_killed_by, k + 2 + 10, height_offset, this.was_killed_by_baby ? 0x909090 : 0x606060);
    }

    @ModifyArg(
        at = @At(
            value  = "INVOKE",
            target = "Lnet/minecraft/network/chat/CommonComponents;joinForNarration([Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"
        ),

        index  = 0,
        method = "getNarration"
    )
    private Component[] addBabyKillNarration(Component components[]) {
        var result = Arrays.copyOf(components, components.length + 2);

        result[components.length + 0] = this.baby_kills;
        result[components.length + 1] = this.babies_killed_by;

        return result;
    }
}
