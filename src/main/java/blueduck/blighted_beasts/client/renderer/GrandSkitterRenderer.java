package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.GrandSkitterModel;
import blueduck.blighted_beasts.entity.GrandSkitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrandSkitterRenderer extends MobRenderer<GrandSkitter, GrandSkitterModel<GrandSkitter>> {
    public GrandSkitterRenderer(EntityRendererProvider.Context context) {
        super(context, new GrandSkitterModel<>(context.bakeLayer(GrandSkitterModel.LAYER_LOCATION)), 2.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(GrandSkitter pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/skitter.png");
    }


}