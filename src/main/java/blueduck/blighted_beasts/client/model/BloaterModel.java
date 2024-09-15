package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Bloater;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BloaterModel<T extends Bloater> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BlightedBeasts.MODID, "bloater"), "main");
	private final ModelPart root;
	private final ModelPart Body;
	private final ModelPart catalyst;
	private final ModelPart tentacle;
	private final ModelPart Head;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;

	public BloaterModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Body = this.root.getChild("Body");
		this.catalyst = this.Body.getChild("catalyst");
		this.tentacle = this.Body.getChild("tentacle");
		this.Head = this.Body.getChild("Head");
		this.leg0 = this.root.getChild("leg0");
		this.leg1 = this.root.getChild("leg1");
		this.leg2 = this.root.getChild("leg2");
		this.leg3 = this.root.getChild("leg3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition catalyst = Body.addOrReplaceChild("catalyst", CubeListBuilder.create().texOffs(28, 46).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 2.0F));

		PartDefinition tentacle = Body.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, -2.0F, -8.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -2.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition leg0 = root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.0F, 3.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -6.0F, 3.0F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -6.0F, -4.0F));

		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Bloater entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		leg0.xRot = (float) Math.sin(limbSwing) * limbSwingAmount;
		leg1.xRot = (float) -Math.sin(limbSwing) * limbSwingAmount;
		leg3.xRot = (float) Math.sin(limbSwing) * limbSwingAmount;
		leg2.xRot = (float) -Math.sin(limbSwing) * limbSwingAmount;

		Body.xRot = (float) (.1745F - Math.sin(limbSwing) * limbSwingAmount * -.1745F);

		if (entity.getTarget() != null) {
			tentacle.xRot = (float) (Math.cos(limbSwing) * limbSwingAmount);
			tentacle.zRot = (float) (Math.cos(limbSwing/2) * limbSwingAmount);
		}

		catalyst.xScale = 1 + entity.swell * 0.02F;
		catalyst.yScale = 1 + entity.swell * 0.02F;
		catalyst.zScale = 1 + entity.swell * 0.02F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}