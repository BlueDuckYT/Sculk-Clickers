package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.ReaperModel;
import blueduck.blighted_beasts.entity.Reaper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReaperRenderer extends MobRenderer<Reaper, ReaperRenderState, ReaperModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/reaper.png");
    
    public ReaperRenderer(EntityRendererProvider.Context context) {
        super(context, new ReaperModel(context.bakeLayer(ReaperModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ReaperRenderState state) {
        return TEXTURE;
    }

    @Override
    public ReaperRenderState createRenderState() {
        return new ReaperRenderState();
    }

    @Override
    public void extractRenderState(Reaper entity, ReaperRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.disturbanceLocation = entity.getDisturbanceLocation();
        state.hasTarget = entity.getTarget() != null;
        state.attackTime = entity.getAttackAnim(partialTick);
    }
}
