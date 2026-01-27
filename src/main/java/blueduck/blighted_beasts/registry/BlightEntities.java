package blueduck.blighted_beasts.registry;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.effect.ConfusionEffect;
import blueduck.blighted_beasts.entity.*;
import blueduck.blighted_beasts.entity.projectile.ReverbProjectile;
import blueduck.blighted_beasts.entity.projectile.SculkPearlProjectile;
import blueduck.blighted_beasts.item.PuppetItem;
import blueduck.blighted_beasts.item.SculkPearlItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlightEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlightedBeasts.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, BlightedBeasts.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, BlightedBeasts.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<Reaper>> REAPER = ENTITIES.register("reaper",
            () -> EntityType.Builder.of(Reaper::new, MobCategory.MONSTER).sized(0.6f, 1.95f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "reaper"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Reverb>> REVERB = ENTITIES.register("reverb",
            () -> EntityType.Builder.of(Reverb::new, MobCategory.MONSTER).sized(1.1f, 1.4f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "reverb"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ReverbProjectile>> REVERB_PROJECTILE = ENTITIES.register("reverb_projectile",
            () -> EntityType.Builder.<ReverbProjectile>of(ReverbProjectile::new, MobCategory.MISC).sized(.6f, .6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "reverb_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Seer>> SEER = ENTITIES.register("seer",
            () -> EntityType.Builder.of(Seer::new, MobCategory.MONSTER).sized(0.7F, 2.4F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "seer"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Bloater>> BLOATER = ENTITIES.register("bloater",
            () -> EntityType.Builder.of(Bloater::new, MobCategory.MONSTER).sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "bloater"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Skitter>> SKITTER = ENTITIES.register("skitter",
            () -> EntityType.Builder.of(Skitter::new, MobCategory.MONSTER).sized(1.3F, 1.9F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "skitter"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Apparition>> SCULK_APPARITION = ENTITIES.register("sculk_apparition",
            () -> EntityType.Builder.of(Apparition::new, MobCategory.MONSTER).sized(0.875F, 1.975F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "sculk_apparition"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Beckon>> BECKON = ENTITIES.register("beckon",
            () -> EntityType.Builder.of(Beckon::new, MobCategory.MONSTER).sized(0.875F, 0.875F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "beckon"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Behemoth>> BEHEMOTH = ENTITIES.register("behemoth",
            () -> EntityType.Builder.of(Behemoth::new, MobCategory.MONSTER).sized(2.3F, 2.5F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "behemoth"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SculkPearlProjectile>> SCULK_PEARL = ENTITIES.register("sculk_pearl",
            () -> EntityType.Builder.<SculkPearlProjectile>of(SculkPearlProjectile::new, MobCategory.MISC).sized(.6f, .6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "sculk_pearl"))));

    public static final DeferredHolder<MobEffect, ConfusionEffect> CONFUSION = EFFECTS.register("confusion", () -> new ConfusionEffect(MobEffectCategory.HARMFUL));

    public static final Supplier<SpawnEggItem> REAPER_SPAWN_EGG = ITEMS.register("reaper_spawn_egg",
            () -> new SpawnEggItem(REAPER.get(), createItemProperties("reaper_spawn_egg")));

    public static final Supplier<SpawnEggItem> REVERB_SPAWN_EGG = ITEMS.register("reverb_spawn_egg",
            () -> new SpawnEggItem(REVERB.get(), createItemProperties("reverb_spawn_egg")));

    public static final Supplier<SpawnEggItem> SEER_SPAWN_EGG = ITEMS.register("seer_spawn_egg",
            () -> new SpawnEggItem(SEER.get(), createItemProperties("seer_spawn_egg")));

    public static final Supplier<SpawnEggItem> BLOATER_SPAWN_EGG = ITEMS.register("bloater_spawn_egg",
            () -> new SpawnEggItem(BLOATER.get(), createItemProperties("bloater_spawn_egg")));

    public static final Supplier<SpawnEggItem> SKITTER_SPAWN_EGG = ITEMS.register("skitter_spawn_egg",
            () -> new SpawnEggItem(SKITTER.get(), createItemProperties("skitter_spawn_egg")));

    public static final Supplier<SpawnEggItem> SCULK_APPARITION_SPAWN_EGG = ITEMS.register("sculk_apparition_spawn_egg",
            () -> new SpawnEggItem(SCULK_APPARITION.get(), createItemProperties("sculk_apparition_spawn_egg")));

    public static final Supplier<SpawnEggItem> BEHEMOTH_SPAWN_EGG = ITEMS.register("behemoth_spawn_egg",
            () -> new SpawnEggItem(BEHEMOTH.get(), createItemProperties("behemoth_spawn_egg")));

    public static final Supplier<SpawnEggItem> BECKON_SPAWN_EGG = ITEMS.register("beckon_spawn_egg",
            () -> new SpawnEggItem(BECKON.get(), createItemProperties("beckon_spawn_egg")));

    public static final Supplier<Item> SCULK_PEARL_ITEM = ITEMS.register("sculk_pearl",
            () -> new SculkPearlItem(createItemProperties("sculk_pearl")));

    public static final Supplier<Item> BECKON_PUPPET_ITEM = ITEMS.register("beckon",
            () -> new PuppetItem(createItemProperties("beckon"), () -> BECKON.get()));

    public static final Supplier<Item> REVERB_PROJECTILE_ITEM = ITEMS.register("reverb_projectile",
            () -> new Item(createItemProperties("reverb_projectile")));

    private static Item.Properties createItemProperties(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, name)));
    }

    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(REAPER_SPAWN_EGG.get());
            event.accept(REVERB_SPAWN_EGG.get());
            event.accept(SEER_SPAWN_EGG.get());
            event.accept(BLOATER_SPAWN_EGG.get());
            event.accept(SKITTER_SPAWN_EGG.get());
            event.accept(SCULK_APPARITION_SPAWN_EGG.get());
            event.accept(BEHEMOTH_SPAWN_EGG.get());
            event.accept(BECKON_SPAWN_EGG.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(SCULK_PEARL_ITEM.get());
            event.accept(BECKON_PUPPET_ITEM.get());
        }
    }
}
