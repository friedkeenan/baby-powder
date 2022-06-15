package io.github.friedkeenan.baby_powder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.friedkeenan.baby_powder.BabyPowderableMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;

@Mixin(AgeableMob.class)
public class StopAgingMixin implements BabyPowderableMob {
    private static final String TAG_BABY_POWDERED = "BabyPowdered";

    private boolean baby_powdered = false;

    public boolean getBabyPowdered() {
        return this.baby_powdered;
    }

    public void setBabyPowdered(boolean baby_powdered) {
        this.baby_powdered = baby_powdered;
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void addBabyPowdered(CompoundTag data, CallbackInfo info) {
        data.putBoolean(TAG_BABY_POWDERED, this.baby_powdered);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readBabyPowdered(CompoundTag data, CallbackInfo info) {
        this.baby_powdered = data.getBoolean(TAG_BABY_POWDERED);
    }

    @Inject(at = @At("HEAD"), method = "setAge", cancellable = true)
    private void stopAging(int age, CallbackInfo info) {
        if (this.baby_powdered && age >= 0) {
            info.cancel();
        }
    }
}
