package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Apparition;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ApparitionModel extends GeoModel<Apparition> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "geo/apparition.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/apparition.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "animations/apparition_animation.json");

    @Override
    public ResourceLocation getModelResource(Apparition animatable, GeoRenderer<Apparition> renderer) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Apparition animatable, GeoRenderer<Apparition> renderer) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Apparition animatable) {
        return ANIMATION;
    }
}
