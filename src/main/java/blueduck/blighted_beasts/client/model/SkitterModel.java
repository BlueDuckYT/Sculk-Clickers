package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;

import blueduck.blighted_beasts.entity.Skitter;
import blueduck.blighted_beasts.entity.Skitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.definitions.WardenAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SkitterModel extends AnimatedGeoModel<Skitter> {
	@Override
	public ResourceLocation getModelResource(Skitter object) {
		return new ResourceLocation(BlightedBeasts.MODID, "geo/skitter.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Skitter object) {
		return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/skitter.png");
	}

	@Override
	public ResourceLocation getAnimationResource(Skitter animatable) {
		return new ResourceLocation(BlightedBeasts.MODID, "animations/skitter_animation.json");
	}
}