package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SkitterModel;
import blueduck.blighted_beasts.entity.Skitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SkitterRenderer extends GeoEntityRenderer<Skitter> {
    public SkitterRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<Skitter> modelProvider) {
        super(renderManager, modelProvider);
        this.shadowRadius = 0.4f;
    }
    public SkitterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SkitterModel());
        this.shadowRadius = .8f;
    }

    @Override
    public ResourceLocation getTextureLocation(Skitter animatable) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/skitter.png");
    }

    @Override
    public RenderType getRenderType(Skitter animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(new ResourceLocation(BlightedBeasts.MODID, "textures/entity/skitter.png"));
    }
}