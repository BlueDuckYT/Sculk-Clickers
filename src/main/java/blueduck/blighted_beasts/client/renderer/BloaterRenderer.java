package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.BloaterModel;
import blueduck.blighted_beasts.entity.Bloater;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BloaterRenderer extends MobRenderer<Bloater, BloaterModel<Bloater>> {
    public BloaterRenderer(EntityRendererProvider.Context context) {
        super(context, new BloaterModel<>(context.bakeLayer(BloaterModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Bloater pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/bloater.png");
    }


}