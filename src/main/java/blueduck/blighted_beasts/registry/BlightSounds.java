package blueduck.blighted_beasts.registry;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.effect.ConfusionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlightSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            BlightedBeasts.MODID);

    public static final RegistryObject<SoundEvent> REAPER_AMBIENT = SOUNDS.register("entity.reaper.ambient", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reaper.ambient")));
    public static final RegistryObject<SoundEvent> REAPER_CLICK = SOUNDS.register("entity.reaper.click", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reaper.click")));
    public static final RegistryObject<SoundEvent> REAPER_HURT = SOUNDS.register("entity.reaper.hurt", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reaper.hurt")));
    public static final RegistryObject<SoundEvent> REAPER_DEATH = SOUNDS.register("entity.reaper.death", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reaper.death")));

    public static final RegistryObject<SoundEvent> REVERB_AMBIENT = SOUNDS.register("entity.reverb.ambient", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reverb.ambient")));
    public static final RegistryObject<SoundEvent> REVERB_CLICK = SOUNDS.register("entity.reverb.click", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reverb.click")));
    public static final RegistryObject<SoundEvent> REVERB_SHOOT = SOUNDS.register("entity.reverb.shoot", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reverb.shoot")));
    public static final RegistryObject<SoundEvent> REVERB_HURT = SOUNDS.register("entity.reverb.hurt", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reverb.hurt")));
    public static final RegistryObject<SoundEvent> REVERB_DEATH = SOUNDS.register("entity.reverb.death", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.reverb.death")));

    public static final RegistryObject<SoundEvent> SEER_AMBIENT = SOUNDS.register("entity.seer.ambient", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.seer.ambient")));
    public static final RegistryObject<SoundEvent> SEER_AGGRO = SOUNDS.register("entity.seer.aggro", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.seer.aggro")));
    public static final RegistryObject<SoundEvent> SEER_SHRIEK = SOUNDS.register("entity.seer.shriek", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.seer.shriek")));
    public static final RegistryObject<SoundEvent> SEER_HURT = SOUNDS.register("entity.seer.hurt", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.seer.hurt")));
    public static final RegistryObject<SoundEvent> SEER_DEATH = SOUNDS.register("entity.seer.death", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.seer.death")));

    public static final RegistryObject<SoundEvent> BLOATER_CLICK = SOUNDS.register("entity.bloater.click", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.bloater.click")));
    public static final RegistryObject<SoundEvent> BLOATER_HURT = SOUNDS.register("entity.bloater.hurt", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.bloater.hurt")));
    public static final RegistryObject<SoundEvent> BLOATER_DEATH = SOUNDS.register("entity.bloater.death", () -> new SoundEvent(new ResourceLocation(BlightedBeasts.MODID, "entity.bloater.death")));


}
