package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.friedkeenan.baby_powder.BabyPowderStats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class TrackBabyKillsMixin {
    private Player asPlayer() {
        return (Player) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "killedEntity")
    private void awardBabiesKilled(ServerLevel level, LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity.isBaby()) {
            final var player = this.asPlayer();
            player.awardStat(BabyPowderStats.BABIES_KILLED_TOTAL);
            player.awardStat(BabyPowderStats.BABIES_KILLED.get(entity.getType()));

            if (entity instanceof Monster) {
                player.awardStat(BabyPowderStats.BABIES_KILLED_MONSTER);
            } else if (entity instanceof AgeableMob) {
                /* Theoretically there could be a baby mob that is neither a monster nor ageable. */

                player.awardStat(BabyPowderStats.BABIES_KILLED_INNOCENT);
            }
        }
    }
}
