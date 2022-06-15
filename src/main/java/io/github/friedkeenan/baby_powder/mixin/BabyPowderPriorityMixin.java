package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.friedkeenan.baby_powder.BabyPowderItem;
import io.github.friedkeenan.baby_powder.BabyPowderMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

@Mixin(Mob.class)
public class BabyPowderPriorityMixin {
    private Mob asMob() {
        return (Mob) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "checkAndHandleImportantInteractions", cancellable = true)
    private void handleBabyPowder(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        final var item = player.getItemInHand(hand);

        if (!item.is(BabyPowderMod.BABY_POWDER)) {
            return;
        }

        final var mob = this.asMob();

        if (!(mob instanceof AgeableMob)) {
            return;
        }

        info.setReturnValue(BabyPowderItem.ApplyBabyPowder((AgeableMob) mob, item));
    }
}
