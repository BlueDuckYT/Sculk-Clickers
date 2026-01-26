package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.BeckonModel;
import blueduck.blighted_beasts.entity.Beckon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeckonRenderer extends GeoEntityRenderer<Beckon> {
    public BeckonRenderer(EntityRendererProvider.Context context) {
        super(context, new BeckonModel());
    }
}
