package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
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


    }

    @SubscribeEvent
    public static void registerPlacement(SpawnPlacementRegisterEvent event) {
        event.register(BlightEntities.REAPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reaper::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BlightEntities.REVERB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Reverb::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }

        @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        //SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());

    }



}
