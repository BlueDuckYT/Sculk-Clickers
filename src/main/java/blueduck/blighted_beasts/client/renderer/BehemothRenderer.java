package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.BehemothModel;
import blueduck.blighted_beasts.entity.Behemoth;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BehemothRenderer extends GeoEntityRenderer<Behemoth> {
    public BehemothRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<Behemoth> modelProvider) {
        super(renderManager, modelProvider);
        this.shadowRadius = 0.4f;
    }
    public BehemothRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BehemothModel());
        this.shadowRadius = 1.2f;
    }

    @Override
    public ResourceLocation getTextureLocation(Behemoth animatable) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/behemoth.png");
    }

    @Override
    public RenderType getRenderType(Behemoth animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(new ResourceLocation(BlightedBeasts.MODID, "textures/entity/behemoth.png"));
    }
}
