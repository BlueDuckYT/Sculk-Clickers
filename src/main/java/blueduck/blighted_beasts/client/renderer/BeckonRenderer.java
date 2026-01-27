package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.BeckonModel;
import blueduck.blighted_beasts.entity.Beckon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BeckonRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Beckon, R> {
    public BeckonRenderer(EntityRendererProvider.Context context) {
        super(context, new BeckonModel());
    }
}
