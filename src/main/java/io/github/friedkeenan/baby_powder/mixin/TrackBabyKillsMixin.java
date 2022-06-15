package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.friedkeenan.baby_powder.BabyPowderStats;
import io.github.friedkeenan.baby_powder.InnocentBabyKiller;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class TrackBabyKillsMixin implements InnocentBabyKiller {
    private static final String TAG_KILLED_INNOCENT_BABY = "KilledInnocentBaby";

    private boolean killed_innocent_baby = false;

    public boolean getKilledInnocentBaby() {
        return this.killed_innocent_baby;
    }

    public void setKilledInnocentBaby(boolean killed_innocent_baby) {
        this.killed_innocent_baby = killed_innocent_baby;
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addKilledInnocentBaby(CompoundTag data, CallbackInfo info) {
        data.putBoolean(TAG_KILLED_INNOCENT_BABY, this.killed_innocent_baby);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readKilledInnocentBaby(CompoundTag data, CallbackInfo info) {
        this.killed_innocent_baby = data.getBoolean(TAG_KILLED_INNOCENT_BABY);
    }

    private Player asPlayer() {
        return (Player) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "wasKilled")
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
