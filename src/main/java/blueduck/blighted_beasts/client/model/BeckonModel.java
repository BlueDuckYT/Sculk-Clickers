package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Beckon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BeckonModel extends AnimatedGeoModel<Beckon> {
    @Override
    public ResourceLocation getModelResource(Beckon object) {
        return new ResourceLocation(BlightedBeasts.MODID, "geo/beckon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Beckon object) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/beckon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Beckon animatable) {
        return new ResourceLocation(BlightedBeasts.MODID, "animations/beckon_animation.json");
    }
}
