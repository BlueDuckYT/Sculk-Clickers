package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.client.model.BeckonModel;
import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Beckon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BeckonRenderer extends GeoEntityRenderer<Beckon> {
    public BeckonRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<Beckon> modelProvider) {
        super(renderManager, modelProvider);
        this.shadowRadius = 0.4f;
    }
    public BeckonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BeckonModel());
        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(Beckon animatable) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/beckon.png");
    }

    @Override
    public RenderType getRenderType(Beckon animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(new ResourceLocation(BlightedBeasts.MODID, "textures/entity/beckon.png"));
    }
}
