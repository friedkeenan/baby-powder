package io.github.friedkeenan.baby_powder.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.friedkeenan.baby_powder.BabyPowderItem;
import io.github.friedkeenan.baby_powder.BabyPowderStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class DropBabyPowderMixin {
    private static final float BABY_POWDER_CHANCE = 1.0f / 32.0f;

    @Shadow
    private int lastHurtByPlayerTime;

    private LivingEntity asLivingEntity() {
        return (LivingEntity) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "dropAllDeathLoot")
    private void dropBabyPowder(DamageSource dmg_source, CallbackInfo info) {
        final var entity = this.asLivingEntity();

        if (!(entity instanceof AgeableMob)) {
            return;
        }

        final var mob = (AgeableMob) entity;

        final var hurt_by_player = this.lastHurtByPlayerTime > 0;
        if (hurt_by_player && mob.isBaby()) {
            var should_drop = mob.getRandom().nextFloat() < BABY_POWDER_CHANCE;

            @Nullable final var killer = dmg_source.getEntity();
            if (killer != null && killer instanceof ServerPlayer) {
                final var player = (ServerPlayer) killer;

                /* If this is the first innocent baby the player has killed. */
                if (player.getStats().getValue(Stats.CUSTOM.get(BabyPowderStats.BABIES_KILLED_INNOCENT)) <= 1) {
                    should_drop = true;
                }
            }

            if (should_drop) {
                final var item = BabyPowderItem.ForMob(mob);

                entity.spawnAtLocation(item);
            }
        }
    }
}
