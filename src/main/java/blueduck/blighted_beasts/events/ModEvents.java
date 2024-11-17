package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.entity.*;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static blueduck.blighted_beasts.BlightedBeasts.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BlightEntities.REAPER.get(), Reaper.createAttributes().build());
        event.put(BlightEntities.REVERB.get(), Reverb.createAttributes().build());
        event.put(BlightEntities.SEER.get(), Seer.createAttributes().build());
        event.put(BlightEntities.BLOATER.get(), Bloater.createAttributes().build());
        event.put(BlightEntities.SKITTER.get(), Skitter.createAttributes().build());

        event.put(BlightEntities.GRAND_SKITTER.get(), GrandSkitter.createAttributes().build());



    }

    @SubscribeEvent
    public static void registerPlacement(SpawnPlacementRegisterEvent event) {
        event.register(BlightEntities.REAPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reaper::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.REVERB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reverb::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.SEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Seer::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.BLOATER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Bloater::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.SKITTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Skitter::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.GRAND_SKITTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, GrandSkitter::canSpawnGrand, SpawnPlacementRegisterEvent.Operation.OR);

    }

        @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        //SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());

    }



}
