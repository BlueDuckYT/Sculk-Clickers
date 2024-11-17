package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.animation.SkitterAnimation;
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

public class SkitterModel<T extends Skitter> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BlightedBeasts.MODID, "skitter"), "main");
	public final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart leg2;

	public AnimationState walkAnimationState = new AnimationState();
	public AnimationState runAnimationState = new AnimationState();

	public SkitterModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.leg0 = this.root.getChild("leg0");
		this.leg1 = this.root.getChild("leg1");
		this.leg2 = this.root.getChild("leg2");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 2.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(46, 28).addBox(-11.0F, -13.0F, 0.0F, 0.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(46, 28).addBox(-3.0F, -13.0F, 0.0F, 0.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, -10.0F, 0.0F, 14.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, -10.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).addBox(-6.0F, 0.0F, -11.0F, 12.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-5.0F, -1.0F, -10.0F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -6.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition leg0 = root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(38, 49).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 24.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(50, 49).addBox(2.0F, 0.0F, 0.5F, 3.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -24.0F, -6.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(38, 49).mirror().addBox(-2.0F, 0.0F, -1.0F, 3.0F, 24.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(50, 49).mirror().addBox(-5.0F, 0.0F, 0.5F, 3.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -24.0F, -6.0F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(38, 49).mirror().addBox(-1.5F, 0.0F, -1.0F, 3.0F, 24.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -24.0F, 11.0F));

		PartDefinition cube_r2 = leg2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 49).addBox(0.0F, -24.0F, 0.0F, 3.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 2.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Skitter entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

//		this.root().getAllParts().forEach(ModelPart::resetPose);
//
//		this.animate(entity.walkAnimationState, SkitterAnimation.walk, ageInTicks);
//		this.animate(entity.runAnimationState, SkitterAnimation.run, ageInTicks);


		leg0.xRot = (float) Math.sin(limbSwing) * limbSwingAmount/2;
		leg1.xRot = (float) -Math.sin(limbSwing) * limbSwingAmount/2;
		leg2.xRot = (float) Math.cos(limbSwing) * limbSwingAmount/2;

		body.y = (float) Math.sin(ageInTicks * 0.1 + 5) - 24.0F;

		float scale = (float) Math.abs(Math.sin(ageInTicks * 0.1 + 5)) * 0.01F + 1;
		body.xScale = scale;
		body.yScale = scale;
		body.zScale = scale;

		head.xRot = (float) Math.cos(ageInTicks * 0.1) * 0.05F + .1745F;

		if (entity.swinging) {
			head.z = (float) (Math.sin(entity.attackAnim * 6.28) * -6.0F  - 6.0F);
		}
		else {
			head.z = -6F;
		}


	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}