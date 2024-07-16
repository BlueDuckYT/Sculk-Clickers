package blueduck.blighted_beasts.registry;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
import blueduck.blighted_beasts.entity.projectile.ReverbProjectile;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<EntityType<Reaper>> REAPER = ENTITIES.register("reaper",
            () -> EntityType.Builder.of(Reaper::new, MobCategory.MONSTER).sized(0.9f, 1.9f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reaper").toString()));
    public static final RegistryObject<EntityType<Reverb>> REVERB = ENTITIES.register("reverb",
            () -> EntityType.Builder.of(Reverb::new, MobCategory.MONSTER).sized(1.1f, 1.4f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reverb").toString()));

    public static final RegistryObject<EntityType<ReverbProjectile>> REVERB_PROJECTILE = ENTITIES.register("reverb_projectile",
            () -> EntityType.Builder.<ReverbProjectile>of(ReverbProjectile::new, MobCategory.MISC).sized(.6f, .6f)
                    .build(new ResourceLocation(BlightedBeasts.MODID, "reverb_projectile").toString()));


    public static final RegistryObject<ForgeSpawnEggItem> REAPER_SPAWN_EGG = ITEMS.register("reaper_spawn_egg",
            () -> new ForgeSpawnEggItem(REAPER, 10045, 11589343,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<ForgeSpawnEggItem> REVERB_SPAWN_EGG = ITEMS.register("reverb_spawn_egg",
            () -> new ForgeSpawnEggItem(REVERB, 11589343, 10045,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> REVERB_PROJECTILE_ITEM = ITEMS.register("reverb_projectile",
            () -> new Item(new Item.Properties()));




}
