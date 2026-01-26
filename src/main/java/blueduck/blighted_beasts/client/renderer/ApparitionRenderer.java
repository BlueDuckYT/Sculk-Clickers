package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.ApparitionModel;
import blueduck.blighted_beasts.entity.Apparition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ApparitionRenderer extends GeoEntityRenderer<Apparition> {
    public ApparitionRenderer(EntityRendererProvider.Context context) {
        super(context, new ApparitionModel());
    }
}
