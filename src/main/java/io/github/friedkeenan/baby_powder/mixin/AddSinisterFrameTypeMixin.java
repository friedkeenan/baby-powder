package io.github.friedkeenan.baby_powder.mixin;

import java.util.ArrayList;
import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

import io.github.friedkeenan.baby_powder.PotentiallySinisterFrameType;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.FrameType;

@Mixin(FrameType.class)
@Unique
public abstract class AddSinisterFrameTypeMixin implements PotentiallySinisterFrameType {

    /* Synthetic field added by the compiler. Potentially unstable. */
    @Shadow
    @Final
    @Mutable
    private static FrameType[] $VALUES;

    /* Enum constructors get hidden name and ordinal parameters. */
    @Invoker("<init>")
    private static FrameType Constructor(String internal_name, int internal_ordinal, String name, ChatFormatting chat_color) {
        throw new AssertionError();
    }

    private static FrameType CreateFrameType(String internal_name, String name, ChatFormatting chat_color) {
        final var values = new ArrayList<>(Arrays.asList($VALUES));

        final var frame_type = Constructor(internal_name, values.get(values.size() - 1).ordinal() + 1, name, chat_color);
        values.add(frame_type);

        /* I hate Java, and that I have to pass an empty array to get the right type. */
        $VALUES = values.toArray(new FrameType[0]);

        return frame_type;

    }

    public boolean isSinister() {
        /* We can't have public static fields in a mixin so we added this method. */
        return ((Object) this) == SINISTER;
    }

    private static final FrameType SINISTER = CreateFrameType("SINISTER", "sinister", ChatFormatting.DARK_RED);
}
