package io.github.friedkeenan.baby_powder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;

public class BabyPowderStats {
    public static final ResourceLocation BABIES_KILLED_TOTAL    = new ResourceLocation("baby_powder:babies_killed_total");
    public static final ResourceLocation BABIES_KILLED_MONSTER  = new ResourceLocation("baby_powder:babies_killed_monster");
    public static final ResourceLocation BABIES_KILLED_INNOCENT = new ResourceLocation("baby_powder:babies_killed_innocent");

    public static final StatType<EntityType<?>> BABIES_KILLED    = RegisterRegistryStat("babies_killed",    BuiltInRegistries.ENTITY_TYPE);
    public static final StatType<EntityType<?>> BABIES_KILLED_BY = RegisterRegistryStat("babies_killed_by", BuiltInRegistries.ENTITY_TYPE);

    public static ResourceLocation RegisterCustomStat(ResourceLocation location, StatFormatter formatter) {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location, formatter);

        return location;
    }

    public static <T> StatType<T> RegisterRegistryStat(String name, Registry<T> registry) {
        return Registry.register(BuiltInRegistries.STAT_TYPE, new ResourceLocation("baby_powder", name), new StatType<>(registry));
    }

    public static void register() {
        /* For some reason custom stats can't be registered in their static definition. */
        RegisterCustomStat(BABIES_KILLED_TOTAL,    StatFormatter.DEFAULT);
        RegisterCustomStat(BABIES_KILLED_MONSTER,  StatFormatter.DEFAULT);
        RegisterCustomStat(BABIES_KILLED_INNOCENT, StatFormatter.DEFAULT);
    }
}
