package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.BehemothModel;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BehemothRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Behemoth, R> {
    public BehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BehemothModel());
    }
}
