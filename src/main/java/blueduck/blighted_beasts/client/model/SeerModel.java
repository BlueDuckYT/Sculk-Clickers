package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Seer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SeerModel<T extends Seer> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BlightedBeasts.MODID, "seer"), "main");
	private final ModelPart root;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart Head2;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public boolean creepy;

	public SeerModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Body = this.root.getChild("Body");
		this.Head = this.Body.getChild("Head");
		this.Head2 = this.Head.getChild("Head2");
		this.RightArm = this.Body.getChild("RightArm");
		this.LeftArm = this.Body.getChild("LeftArm");
		this.RightLeg = this.root.getChild("RightLeg");
		this.LeftLeg = this.root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F))
		.texOffs(36, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Head2 = Head.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(56, 0).addBox(-3.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -10.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r1 = RightArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 27.0F, 1.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -10.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r2 = LeftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 0).mirror().addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 27.0F, 1.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition RightLeg = root.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(56, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -16.0F, 0.0F));

		PartDefinition LeftLeg = root.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(56, 10).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Seer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		LeftLeg.xRot = (float) Math.sin(limbSwing) * limbSwingAmount * 2/3;
		RightLeg.xRot = (float) -Math.sin(limbSwing) * limbSwingAmount * 2/3;

		LeftArm.xRot = -0.6109F + (float) Math.cos(limbSwing) * limbSwingAmount * 2/3;
		RightArm.xRot = -0.6109F + (float) -Math.cos(limbSwing) * limbSwingAmount * 2/3;
		LeftArm.getChild("cube_r2").xRot = (float) Math.cos(limbSwing) * limbSwingAmount * -1;
		RightArm.getChild("cube_r1").xRot = (float) Math.cos(limbSwing) * limbSwingAmount;

		if (this.creepy) {
			this.Head2.y = (float) (-4.5F + Math.sin(ageInTicks/10));
//			this.Head2.y = -5.5F;
		}
//		else if (entity.getTarget() != null) {
//			this.Head2.y = (float) (-4.5F + Math.sin(ageInTicks));
//		}
		else {
			this.Head2.y = 0.0F;
		}

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}