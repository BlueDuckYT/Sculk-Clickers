package blueduck.blighted_beasts.client;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.*;
import blueduck.blighted_beasts.client.renderer.*;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = BlightedBeasts.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    public ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ReaperModel.LAYER_LOCATION, ReaperModel::createBodyLayer);
        event.registerLayerDefinition(ReverbModel.LAYER_LOCATION, ReverbModel::createBodyLayer);
        event.registerLayerDefinition(SeerModel.LAYER_LOCATION, SeerModel::createBodyLayer);
        event.registerLayerDefinition(BloaterModel.LAYER_LOCATION, BloaterModel::createBodyLayer);
        event.registerLayerDefinition(SkitterModel.LAYER_LOCATION, SkitterModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BlightEntities.REAPER.get(), ReaperRenderer::new);
        event.registerEntityRenderer(BlightEntities.REVERB.get(), ReverbRenderer::new);
        event.registerEntityRenderer(BlightEntities.REVERB_PROJECTILE.get(), ReverbProjectileRenderer::new);
        event.registerEntityRenderer(BlightEntities.SEER.get(), SeerRenderer::new);
        event.registerEntityRenderer(BlightEntities.BLOATER.get(), BloaterRenderer::new);
        event.registerEntityRenderer(BlightEntities.SKITTER.get(), SkitterRenderer::new);
        event.registerEntityRenderer(BlightEntities.SCULK_APPARITION.get(), ApparitionRenderer::new);
        event.registerEntityRenderer(BlightEntities.BEHEMOTH.get(), BehemothRenderer::new);
        event.registerEntityRenderer(BlightEntities.BECKON.get(), BeckonRenderer::new);
        event.registerEntityRenderer(BlightEntities.SCULK_PEARL.get(), SculkPearlRenderer::new);
    }
}
