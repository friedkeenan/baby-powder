package io.github.friedkeenan.baby_powder.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.friedkeenan.baby_powder.PotentiallySinisterFrameType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

@Mixin(AdvancementToast.class)
@Environment(EnvType.CLIENT)
public class AddSinisterSoundMixin {
    @Shadow
    @Final
    private Advancement advancement;

    @Inject(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/components/toasts/AdvancementToast;playedSound:Z",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        ),

        method = "render"
    )
    private void playSinisterSound(PoseStack pose_stack, ToastComponent component, long time, CallbackInfoReturnable<Toast.Visibility> info) {
        final var frame = (PotentiallySinisterFrameType) (Object) advancement.getDisplay().getFrame();

        if (frame.isSinister()) {
            component.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMBIENT_CAVE.value(), 1.0f, 1.0f));
        }
    }
}
