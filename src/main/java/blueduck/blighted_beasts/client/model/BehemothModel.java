package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BehemothModel extends GeoModel<Behemoth> {
    // GeckoLib 5 uses simplified paths without prefix/suffix - files go in geckolib/models and geckolib/animations
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "behemoth");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/behemoth.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "behemoth");

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Behemoth animatable) {
        return ANIMATION;
    }
}
