package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Beckon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BeckonModel extends GeoModel<Beckon> {
    // GeckoLib 5 uses simplified paths without prefix/suffix - files go in geckolib/models and geckolib/animations
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "beckon");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/beckon.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "beckon");

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Beckon animatable) {
        return ANIMATION;
    }
}
