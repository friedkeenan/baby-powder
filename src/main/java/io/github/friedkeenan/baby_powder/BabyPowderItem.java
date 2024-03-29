package io.github.friedkeenan.baby_powder;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class BabyPowderItem extends Item {
    public static final String TAG_ENTITY_TYPE = "BabyPowderEntity";

    private static ResourceLocation EntityTypeKey(AgeableMob mob) {
        return EntityType.getKey(mob.getType());
    }

    public static ItemStack ForMob(AgeableMob mob) {
        final var item = new ItemStack(BabyPowderMod.BABY_POWDER);
        final var data = new CompoundTag();

        data.putString(TAG_ENTITY_TYPE, EntityTypeKey(mob).toString());
        item.setTag(data);

        return item;
    }

    public static void MakePoofParticles(AgeableMob mob) {
        /* Reuses the particle effect for when a mob dies. */
        mob.level().broadcastEntityEvent(mob, (byte) 60);
    }

    public static void PlayPoofSound(AgeableMob mob) {
        mob.playSound(BabyPowderMod.BABY_POWDER_POOF);
    }

    public static InteractionResult ApplyBabyPowder(AgeableMob mob, ItemStack item) {
        final var powderable_mob = (BabyPowderableMob) mob;

        if (!mob.isBaby()) {
            return InteractionResult.PASS;
        }

        if (powderable_mob.getBabyPowdered()) {
            return InteractionResult.PASS;
        }

        if (!item.hasTag()) {
            return InteractionResult.PASS;
        }

        final var powder_type = new ResourceLocation(item.getTag().getString(TAG_ENTITY_TYPE));
        final var mob_type    = EntityTypeKey(mob);

        if (!powder_type.equals(mob_type)) {
            return InteractionResult.PASS;
        }

        powderable_mob.setBabyPowdered(true);
        item.shrink(1);

        MakePoofParticles(mob);
        PlayPoofSound(mob);
        mob.gameEvent(GameEvent.ENTITY_INTERACT);

        return InteractionResult.sidedSuccess(mob.level().isClientSide());
    }

    public BabyPowderItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (item.hasTag()) {
            final var entity_type = EntityType.byString(item.getTag().getString(TAG_ENTITY_TYPE));
            if (entity_type.isEmpty()) {
                return;
            }

            components.add(entity_type.get().getDescription());
        }

        super.appendHoverText(item, level, components, flag);
    }
}
