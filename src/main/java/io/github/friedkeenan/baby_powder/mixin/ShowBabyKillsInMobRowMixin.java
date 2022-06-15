package io.github.friedkeenan.baby_powder.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.friedkeenan.baby_powder.BabyPowderStats;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.EntityType;

@Mixin(targets = {"net/minecraft/client/gui/screens/achievement/StatsScreen$MobsStatisticsList$MobRow"})
@Environment(EnvType.CLIENT)
public class ShowBabyKillsInMobRowMixin {
    @Shadow
    @Final
    private Component mobName;

    private MobsStatisticsListAccessor list;

    private @Nullable Component baby_kills = null;
    private @Nullable Component babies_killed_by = null;

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
    private void createBabyKillComponents(@Coerce MobsStatisticsListAccessor mobs_statistics_list, EntityType<?> entity, CallbackInfo info) {
        this.list = mobs_statistics_list;

        final var stats = this.getStats();

        final var num_baby_kills = stats.getValue(BabyPowderStats.BABIES_KILLED.get(entity));
        if (num_baby_kills > 0) {
            this.baby_kills = Component.translatable("stat_type.baby_powder.baby_kills", num_baby_kills, this.mobName);
        }

        final var num_babies_killed_by = stats.getValue(BabyPowderStats.BABIES_KILLED_BY.get(entity));
        if (num_babies_killed_by > 0) {
            this.babies_killed_by =Component.translatable("stat_type.baby_powder.babies_killed_by", this.mobName, num_babies_killed_by);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void renderBabyKills(PoseStack pose_stack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo info) {
        final var font = this.getFont();
        var height_offset = j + 1 + font.lineHeight * 3;

        if (this.baby_kills != null) {
            GuiComponent.drawString(pose_stack, font, this.baby_kills, k + 2 + 10, height_offset, 0x909090);
            height_offset += font.lineHeight;
        }

        if (this.babies_killed_by != null) {
            GuiComponent.drawString(pose_stack, font, this.babies_killed_by, k + 2 + 10, height_offset, 0x909090);
        }
    }

    private static MutableComponent JoinLinesForNarration(Component... components) {
        final var list = new ArrayList<>(Arrays.asList(components));
        list.removeIf(x -> x == null);

        return ComponentUtils.formatList(list, CommonComponents.NARRATION_SEPARATOR, Function.identity());
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/CommonComponents;joinForNarration(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"
        ),

        method = "getNarration"
    )
    private MutableComponent addBabyKillNarration(Component kills, Component killed_by) {
        return JoinLinesForNarration(kills, killed_by, this.baby_kills, this.babies_killed_by);
    }
}
