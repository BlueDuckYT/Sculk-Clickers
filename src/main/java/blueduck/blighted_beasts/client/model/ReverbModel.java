package blueduck.blighted_beasts.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class ReverbModel<T extends Reverb> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "reverbmodel"), "main");
	private final ModelPart bone;
	private final ModelPart body0;
	private final ModelPart head;
	private final ModelPart body1;
	private final ModelPart leg3;
	private final ModelPart leg2;
	private final ModelPart leg1;
	private final ModelPart leg0;
	private final ModelPart leg4;
	private final ModelPart leg5;
	private final ModelPart leg6;
	private final ModelPart leg7;

	public ReverbModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.body0 = bone.getChild("body0");
		this.head = body0.getChild("head");
		this.body1 = bone.getChild("body1");
		this.leg3 = body1.getChild("leg3");
		this.leg2 = body1.getChild("leg2");
		this.leg1 = bone.getChild("leg1");
		this.leg0 = bone.getChild("leg0");
		this.leg4 = bone.getChild("leg4");
		this.leg5 = bone.getChild("leg5");
		this.leg6 = bone.getChild("leg6");
		this.leg7 = bone.getChild("leg7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 3.0F));

		PartDefinition body0 = bone.addOrReplaceChild("body0", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -3.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition head = body0.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body1 = bone.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -1.8787F, -2.1213F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition leg3 = body1.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, 4.0F, 0.0F, -0.2618F, 0.6109F));

		PartDefinition leg2 = body1.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.0F, 3.0F, 0.0F, 0.2618F, -0.6109F));

		PartDefinition leg1 = bone.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.0F, -1.0F, 0.0F, -0.7854F, 0.7854F));

		PartDefinition leg0 = bone.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 4.0F, -1.0F, 0.0F, 0.7854F, -0.7854F));

		PartDefinition leg4 = bone.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 32).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.0F, -3.0F, 0.0F, -0.2618F, 0.6109F));

		PartDefinition leg5 = bone.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, -3.0F, 0.0F, 0.2618F, -0.6109F));

		PartDefinition leg6 = bone.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 4.0F, -4.0F, 0.0F, -0.7854F, -0.7854F));

		PartDefinition leg7 = bone.addOrReplaceChild("leg7", CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.0F, -4.0F, 0.0F, 0.7854F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Reverb entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		body1.xRot = 0.7854F + (float)(Math.sin(limbSwing/3) * limbSwingAmount)/7f;

		leg1.yRot = -0.7854F + ((float) Math.sin(limbSwing) * limbSwingAmount * 2/3);
		leg0.yRot =  0.7854F + ((float) Math.sin(limbSwing) * limbSwingAmount * 2/3);
		leg6.yRot = -0.7854F + ((float) Math.sin(limbSwing) * limbSwingAmount * 2/3);
		leg7.yRot =  0.7854F + ((float) Math.sin(limbSwing) * limbSwingAmount * 2/3);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}