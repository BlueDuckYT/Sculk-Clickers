package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.entity.*;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import static blueduck.blighted_beasts.BlightedBeasts.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BlightEntities.REAPER.get(), Reaper.createAttributes().build());
        event.put(BlightEntities.REVERB.get(), Reverb.createAttributes().build());
        event.put(BlightEntities.SEER.get(), Seer.createAttributes().build());
        event.put(BlightEntities.BLOATER.get(), Bloater.createAttributes().build());
        event.put(BlightEntities.SKITTER.get(), Skitter.createAttributes().build());
        event.put(BlightEntities.SCULK_APPARITION.get(), Apparition.createAttributes().build());
        event.put(BlightEntities.BEHEMOTH.get(), Behemoth.createAttributes().build());
        event.put(BlightEntities.BECKON.get(), Beckon.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(BlightEntities.REAPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reaper::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(BlightEntities.REVERB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reverb::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(BlightEntities.SEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Seer::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(BlightEntities.BLOATER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Bloater::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(BlightEntities.SKITTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Skitter::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        // Common setup code if needed
    }
}
