package io.github.friedkeenan.baby_powder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;

public class BabyPowderStats {
    public static final ResourceLocation BABIES_KILLED_TOTAL = new ResourceLocation("baby_powder:babies_killed_total");
    public static final ResourceLocation BABIES_KILLED_MONSTER = new ResourceLocation("baby_powder:babies_killed_monster");
    public static final ResourceLocation BABIES_KILLED_INNOCENT = new ResourceLocation("baby_powder:babies_killed_innocent");

    public static final StatType<EntityType<?>> BABIES_KILLED = new StatType<>(Registry.ENTITY_TYPE);
    public static final StatType<EntityType<?>> BABIES_KILLED_BY = new StatType<>(Registry.ENTITY_TYPE);

    public static void RegisterCustomStat(ResourceLocation location) {
        Registry.register(Registry.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location);
    }

    public static void RegisterRegistryStat(String string, StatType<?> stat) {
        Registry.register(Registry.STAT_TYPE, string, stat);
    }

    public static void register() {
        RegisterCustomStat(BABIES_KILLED_TOTAL);
        RegisterCustomStat(BABIES_KILLED_MONSTER);
        RegisterCustomStat(BABIES_KILLED_INNOCENT);

        RegisterRegistryStat("baby_powder:babies_killed", BABIES_KILLED);
        RegisterRegistryStat("baby_powder:babies_killed_by", BABIES_KILLED_BY);
    }
}
