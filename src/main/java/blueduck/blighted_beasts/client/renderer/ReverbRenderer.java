package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.ReverbModel;
import blueduck.blighted_beasts.entity.Reverb;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReverbRenderer extends MobRenderer<Reverb, ReverbModel<Reverb>> {
    public ReverbRenderer(EntityRendererProvider.Context context) {
        super(context, new ReverbModel<>(context.bakeLayer(ReverbModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Reverb pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/reverb.png");
    }


}