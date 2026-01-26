package blueduck.blighted_beasts.registry;

import blueduck.blighted_beasts.BlightedBeasts;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlightSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, BlightedBeasts.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> REAPER_AMBIENT = SOUNDS.register("entity.reaper.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reaper.ambient")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REAPER_CLICK = SOUNDS.register("entity.reaper.click", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reaper.click")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REAPER_HURT = SOUNDS.register("entity.reaper.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reaper.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REAPER_DEATH = SOUNDS.register("entity.reaper.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reaper.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> REVERB_AMBIENT = SOUNDS.register("entity.reverb.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reverb.ambient")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REVERB_CLICK = SOUNDS.register("entity.reverb.click", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reverb.click")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REVERB_SHOOT = SOUNDS.register("entity.reverb.shoot", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reverb.shoot")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REVERB_HURT = SOUNDS.register("entity.reverb.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reverb.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> REVERB_DEATH = SOUNDS.register("entity.reverb.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.reverb.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> SEER_AMBIENT = SOUNDS.register("entity.seer.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.seer.ambient")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SEER_AGGRO = SOUNDS.register("entity.seer.aggro", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.seer.aggro")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SEER_SHRIEK = SOUNDS.register("entity.seer.shriek", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.seer.shriek")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SEER_HURT = SOUNDS.register("entity.seer.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.seer.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SEER_DEATH = SOUNDS.register("entity.seer.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.seer.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> BLOATER_CLICK = SOUNDS.register("entity.bloater.click", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.bloater.click")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOATER_HURT = SOUNDS.register("entity.bloater.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.bloater.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOATER_DEATH = SOUNDS.register("entity.bloater.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.bloater.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> SKITTER_WALK = SOUNDS.register("entity.skitter.walk", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.skitter.ambient")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SKITTER_RUN = SOUNDS.register("entity.skitter.run", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.skitter.run")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SKITTER_AMBIENT = SOUNDS.register("entity.skitter.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.skitter.walk")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SKITTER_HURT = SOUNDS.register("entity.skitter.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.skitter.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SKITTER_DEATH = SOUNDS.register("entity.skitter.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.skitter.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> APPARITION_AMBIENT = SOUNDS.register("entity.apparition.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.apparition.walk")));
    public static final DeferredHolder<SoundEvent, SoundEvent> APPARITION_HURT = SOUNDS.register("entity.apparition.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.apparition.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> APPARITION_DEATH = SOUNDS.register("entity.apparition.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.apparition.death")));

    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_AMBIENT = SOUNDS.register("entity.behemoth.ambient", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.ambient")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_HURT = SOUNDS.register("entity.behemoth.hurt", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_DEATH = SOUNDS.register("entity.behemoth.death", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_WALK = SOUNDS.register("entity.behemoth.walk", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.walk")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_RUN = SOUNDS.register("entity.behemoth.run", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.run")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_SNIFF = SOUNDS.register("entity.behemoth.sniff", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.behemoth.sniff")));

    public static final DeferredHolder<SoundEvent, SoundEvent> BECKON_SHRIEK = SOUNDS.register("entity.beckon.shriek", 
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "entity.beckon.shriek")));
}
