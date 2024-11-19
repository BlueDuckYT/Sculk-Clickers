package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.GrandSkitterModel;
import blueduck.blighted_beasts.client.model.SkitterModel;
import blueduck.blighted_beasts.entity.Skitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkitterRenderer extends MobRenderer<Skitter, SkitterModel<Skitter>> {
    public SkitterRenderer(EntityRendererProvider.Context context) {
        super(context, new SkitterModel<>(context.bakeLayer(SkitterModel.LAYER_LOCATION)), .7F);
    }

    @Override
    public ResourceLocation getTextureLocation(Skitter pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/skitter.png");
    }


}