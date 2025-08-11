package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Apparition;
import blueduck.blighted_beasts.entity.Apparition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ApparitionModel extends AnimatedGeoModel<Apparition> {
	@Override
	public ResourceLocation getModelResource(Apparition object) {
		return new ResourceLocation(BlightedBeasts.MODID, "geo/apparition.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Apparition object) {
		return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/apparition.png");
	}

	@Override
	public ResourceLocation getAnimationResource(Apparition animatable) {
		return new ResourceLocation(BlightedBeasts.MODID, "animations/apparition_animation.json");
	}
}