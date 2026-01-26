package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Beckon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BeckonModel extends GeoModel<Beckon> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "geo/beckon.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/beckon.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "animations/beckon_animation.json");

    @Override
    public ResourceLocation getModelResource(Beckon animatable, GeoRenderer<Beckon> renderer) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Beckon animatable, GeoRenderer<Beckon> renderer) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Beckon animatable) {
        return ANIMATION;
    }
}
