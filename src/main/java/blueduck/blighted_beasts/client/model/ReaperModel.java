package blueduck.blighted_beasts.client.model;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.renderer.ReaperRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ReaperModel extends EntityModel<ReaperRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "reaper"), "main");
    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public ReaperModel(ModelPart root) {
        super(root, RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.Body = this.root.getChild("Body");
        this.Head = this.Body.getChild("Head");
        this.RightArm = this.Body.getChild("RightArm");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.RightLeg = this.root.getChild("RightLeg");
        this.LeftLeg = this.root.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition RightLeg = root.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));
        PartDefinition LeftLeg = root.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));
        PartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ReaperRenderState state) {
        super.setupAnim(state);
        
        float limbSwing = state.walkAnimationPos;
        float limbSwingAmount = state.walkAnimationSpeed;
        float ageInTicks = state.ageInTicks;
        float netHeadYaw = state.yRot;
        float headPitch = state.xRot;
        
        this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = headPitch * ((float)Math.PI / 180F);
        this.RightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

        // Zombie arm animation when targeting
        boolean isAggressive = state.disturbanceLocation != null || state.hasTarget;
        float attackTime = state.attackTime;
        
        if (isAggressive) {
            float f = Mth.sin(attackTime * (float)Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
            this.RightArm.zRot = 0.0F;
            this.LeftArm.zRot = 0.0F;
            this.RightArm.yRot = -(0.1F - f * 0.6F);
            this.LeftArm.yRot = 0.1F - f * 0.6F;
            this.RightArm.xRot = (-(float)Math.PI / 2F);
            this.LeftArm.xRot = (-(float)Math.PI / 2F);
            this.RightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.LeftArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.RightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.LeftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.RightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
            this.LeftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
        } else {
            this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
