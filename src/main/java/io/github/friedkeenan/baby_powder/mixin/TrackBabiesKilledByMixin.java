package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.friedkeenan.baby_powder.BabyPowderStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

@Mixin(ServerPlayer.class)
public class TrackBabiesKilledByMixin {
    private ServerPlayer asServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "die")
    private void trackBabiesKilled(DamageSource dmg_source, CallbackInfo info) {
        final var player = this.asServerPlayer();
        final var killer = player.getKillCredit();

        player.awardStat(BabyPowderStats.BABIES_KILLED_BY.get(killer.getType()));
    }
}
