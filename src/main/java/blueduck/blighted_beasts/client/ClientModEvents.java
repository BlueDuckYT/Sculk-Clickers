package blueduck.blighted_beasts.client;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.ReaperModel;
import blueduck.blighted_beasts.client.model.ReverbModel;
import blueduck.blighted_beasts.client.renderer.ReaperRenderer;
import blueduck.blighted_beasts.client.renderer.ReverbProjectileRenderer;
import blueduck.blighted_beasts.client.renderer.ReverbRenderer;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlightedBeasts.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    public ClientModEvents() {

    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ReaperModel.LAYER_LOCATION, ReaperModel::createBodyLayer);
        event.registerLayerDefinition(ReverbModel.LAYER_LOCATION, ReverbModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BlightEntities.REAPER.get(), ReaperRenderer::new);
        event.registerEntityRenderer(BlightEntities.REVERB.get(), ReverbRenderer::new);
        event.registerEntityRenderer(BlightEntities.REVERB_PROJECTILE.get(), ReverbProjectileRenderer::new);
    }
}
