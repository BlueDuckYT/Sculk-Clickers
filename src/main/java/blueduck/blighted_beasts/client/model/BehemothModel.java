package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BehemothModel extends GeoModel<Behemoth> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "geo/behemoth.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/behemoth.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "animations/behemoth_animation.json");

    @Override
    public ResourceLocation getModelResource(Behemoth animatable, GeoRenderer<Behemoth> renderer) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Behemoth animatable, GeoRenderer<Behemoth> renderer) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Behemoth animatable) {
        return ANIMATION;
    }
}
