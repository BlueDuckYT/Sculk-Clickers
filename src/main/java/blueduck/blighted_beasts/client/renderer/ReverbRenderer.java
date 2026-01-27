package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.ReverbModel;
import blueduck.blighted_beasts.entity.Reverb;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReverbRenderer extends MobRenderer<Reverb, ReverbRenderState, ReverbModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/reverb.png");
    
    public ReverbRenderer(EntityRendererProvider.Context context) {
        super(context, new ReverbModel(context.bakeLayer(ReverbModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ReverbRenderState state) {
        return TEXTURE;
    }

    @Override
    public ReverbRenderState createRenderState() {
        return new ReverbRenderState();
    }

    @Override
    public void extractRenderState(Reverb entity, ReverbRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.disturbanceLocation = entity.getDisturbanceLocation();
        state.hasTarget = entity.getTarget() != null;
    }
}
