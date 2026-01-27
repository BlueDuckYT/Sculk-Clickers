package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.ApparitionModel;
import blueduck.blighted_beasts.entity.Apparition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class ApparitionRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Apparition, R> {
    public ApparitionRenderer(EntityRendererProvider.Context context) {
        super(context, new ApparitionModel());
    }
}
