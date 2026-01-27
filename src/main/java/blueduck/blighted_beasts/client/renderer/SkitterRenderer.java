package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SkitterModel;
import blueduck.blighted_beasts.entity.Skitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkitterRenderer extends MobRenderer<Skitter, SkitterRenderState, SkitterModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/skitter.png");
    
    public SkitterRenderer(EntityRendererProvider.Context context) {
        super(context, new SkitterModel(context.bakeLayer(SkitterModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(SkitterRenderState state) {
        return TEXTURE;
    }

    @Override
    public SkitterRenderState createRenderState() {
        return new SkitterRenderState();
    }

    @Override
    public void extractRenderState(Skitter entity, SkitterRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isSwinging = entity.swinging;
        state.attackAnimProgress = entity.getAttackAnim(partialTick);
    }
}
