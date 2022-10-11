package io.github.friedkeenan.baby_powder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.DispenserBlock;

public class BabyPowderMod implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("baby_powder");

    public static final BabyPowderItem BABY_POWDER = new BabyPowderItem(new Item.Properties().rarity(Rarity.RARE));
    public static final SoundEvent BABY_POWDER_POOF = new SoundEvent(new ResourceLocation("baby_powder:entity.baby_powder.poof"));

    public void onInitialize() {
        BabyPowderStats.register();

        Registry.register(Registry.ITEM, new ResourceLocation("baby_powder:baby_powder"), BABY_POWDER);
        Registry.register(Registry.SOUND_EVENT, BABY_POWDER_POOF.getLocation(), BABY_POWDER_POOF);

        DispenserBlock.registerBehavior(BABY_POWDER, new BabyPowderDispenseItemBehavior());

        LOGGER.info("baby_powder initialized!");
    }
}
