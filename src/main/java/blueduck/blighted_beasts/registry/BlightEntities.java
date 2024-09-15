package blueduck.blighted_beasts.registry;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.effect.ConfusionEffect;
import blueduck.blighted_beasts.entity.Bloater;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
import blueduck.blighted_beasts.entity.Seer;
import blueduck.blighted_beasts.entity.projectile.ReverbProjectile;
import blueduck.blighted_beasts.entity.projectile.SculkPearlProjectile;
import blueduck.blighted_beasts.item.SculkPearlItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlightEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            BlightedBeasts.MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            BlightedBeasts.MODID);

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            BlightedBeasts.MODID);

    public static final RegistryObject<EntityType<Reaper>> REAPER = ENTITIES.register("reaper",
            () -> EntityType.Builder.of(Reaper::new, MobCategory.MONSTER).sized(0.6f, 1.95f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reaper").toString()));
    public static final RegistryObject<EntityType<Reverb>> REVERB = ENTITIES.register("reverb",
            () -> EntityType.Builder.of(Reverb::new, MobCategory.MONSTER).sized(1.1f, 1.4f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reverb").toString()));

    public static final RegistryObject<EntityType<ReverbProjectile>> REVERB_PROJECTILE = ENTITIES.register("reverb_projectile",
            () -> EntityType.Builder.<ReverbProjectile>of(ReverbProjectile::new, MobCategory.MISC).sized(.6f, .6f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reverb_projectile").toString()));


    public static final RegistryObject<EntityType<Seer>> SEER = ENTITIES.register("seer",
            () -> EntityType.Builder.of(Seer::new, MobCategory.MONSTER).sized(0.7F, 2.4F)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "seer").toString()));

    public static final RegistryObject<EntityType<Bloater>> BLOATER = ENTITIES.register("bloater",
            () -> EntityType.Builder.of(Bloater::new, MobCategory.MONSTER).sized(0.6F, 1.95F)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "bloater").toString()));


    public static final RegistryObject<EntityType<SculkPearlProjectile>> SCULK_PEARL = ENTITIES.register("sculk_pearl",
            () -> EntityType.Builder.<SculkPearlProjectile>of(SculkPearlProjectile::new, MobCategory.MISC).sized(.6f, .6f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "sculk_pearl").toString()));





    public static final RegistryObject<MobEffect> CONFUSION = EFFECTS.register("confusion", () -> new ConfusionEffect(MobEffectCategory.HARMFUL));



    public static final RegistryObject<ForgeSpawnEggItem> REAPER_SPAWN_EGG = ITEMS.register("reaper_spawn_egg",
            () -> new ForgeSpawnEggItem(REAPER, 10045, 11589343,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<ForgeSpawnEggItem> REVERB_SPAWN_EGG = ITEMS.register("reverb_spawn_egg",
            () -> new ForgeSpawnEggItem(REVERB, 11589343, 10045,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<ForgeSpawnEggItem> SEER_SPAWN_EGG = ITEMS.register("seer_spawn_egg",
            () -> new ForgeSpawnEggItem(SEER, 11589343, 7062271,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<ForgeSpawnEggItem> BLOATER_SPAWN_EGG = ITEMS.register("bloater_spawn_egg",
            () -> new ForgeSpawnEggItem(BLOATER, 11589343, 7062271,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SCULK_PEARL_ITEM = ITEMS.register("sculk_pearl",
            () -> new SculkPearlItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> REVERB_PROJECTILE_ITEM = ITEMS.register("reverb_projectile",
            () -> new Item(new Item.Properties()));




}
