package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BehemothModel extends AnimatedGeoModel<Behemoth> {
	@Override
	public ResourceLocation getModelResource(Behemoth object) {
		return new ResourceLocation(BlightedBeasts.MODID, "geo/behemoth.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Behemoth object) {
		return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/behemoth.png");
	}

	@Override
	public ResourceLocation getAnimationResource(Behemoth animatable) {
		return new ResourceLocation(BlightedBeasts.MODID, "animations/behemoth_animation.json");
	}
}