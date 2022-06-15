package io.github.friedkeenan.baby_powder;

import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

public class BabyPowderDispenseItemBehavior extends OptionalDispenseItemBehavior {
    @Override
    public ItemStack execute(BlockSource source, ItemStack item) {
        final var level = source.getLevel();

        /*
            BlockSource::getLevel returns ServerLevel; it should never be clientside.
            The game however checks it nonetheless, so I will too.
        */
        if (level.isClientSide) {
            return item;
        }

        final var pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        final var mobs = level.getEntitiesOfClass(AgeableMob.class, new AABB(pos), EntitySelector.NO_SPECTATORS);

        this.setSuccess(false);
        for (AgeableMob mob : mobs) {
            final var result = BabyPowderItem.ApplyBabyPowder(mob, item);

            if (result.consumesAction()) {
                this.setSuccess(true);
                break;
            }
        }

        return item;
    }
}
