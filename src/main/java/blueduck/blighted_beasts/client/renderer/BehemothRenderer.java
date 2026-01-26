package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.BehemothModel;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BehemothRenderer extends GeoEntityRenderer<Behemoth> {
    public BehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BehemothModel());
    }
}
