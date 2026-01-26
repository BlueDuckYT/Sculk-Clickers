package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SeerModel;
import blueduck.blighted_beasts.entity.Seer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SeerRenderer extends MobRenderer<Seer, SeerRenderState, SeerModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/seer.png");
    
    public SeerRenderer(EntityRendererProvider.Context context) {
        super(context, new SeerModel(context.bakeLayer(SeerModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new SeerEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SeerRenderState state) {
        return TEXTURE;
    }

    @Override
    public SeerRenderState createRenderState() {
        return new SeerRenderState();
    }

    @Override
    public void extractRenderState(Seer entity, SeerRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.creepy = entity.isCreepy();
        state.carriedBlock = entity.getCarriedBlock();
    }
}
