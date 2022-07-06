package io.github.friedkeenan.baby_powder.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.friedkeenan.baby_powder.BabyPowderItem;
import io.github.friedkeenan.baby_powder.InnocentBabyKiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(LivingEntity.class)
public class DropBabyPowderMixin {
    private static final float BABY_POWDER_CHANCE = 1.0f / 32.0f;

    private LivingEntity asLivingEntity() {
        return (LivingEntity) (Object) this;
    }

    @Inject(at = @At("RETURN"), method = "shouldDropLoot", cancellable = true)
    private void allowBabyDrops(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "dropFromLootTable", cancellable = true)
    private void stopBabyLoot(DamageSource dmg_source, boolean hurt_by_player, CallbackInfo info) {
        if (this.asLivingEntity().isBaby()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "dropCustomDeathLoot")
    private void dropBabyPowder(DamageSource dmg_source, int looting, boolean hurt_by_player, CallbackInfo info) {
        final var entity = this.asLivingEntity();

        if (!(entity instanceof AgeableMob)) {
            return;
        }

        final var mob = (AgeableMob) entity;

        if (hurt_by_player && mob.isBaby()) {
            var should_drop = mob.getRandom().nextFloat() < BABY_POWDER_CHANCE;

            @Nullable final var killer = dmg_source.getEntity();
            if (killer != null && killer instanceof Player) {
                final var baby_killer = (InnocentBabyKiller) killer;

                if (!baby_killer.getKilledInnocentBaby()) {
                    should_drop = true;
                    baby_killer.setKilledInnocentBaby(true);
                }
            }

            if (should_drop) {
                final var item = BabyPowderItem.ForMob(mob);

                entity.spawnAtLocation(item);
            }
        }
    }
}
