package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.entity.projectile.SculkPearlProjectile;
import blueduck.blighted_beasts.registry.BlightEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SculkPearlRenderer extends EntityRenderer<SculkPearlProjectile, SculkPearlRenderer.SculkPearlRenderState> {
    private final ItemRenderer itemRenderer;

    public SculkPearlRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public SculkPearlRenderState createRenderState() {
        return new SculkPearlRenderState();
    }

    @Override
    public void extractRenderState(SculkPearlProjectile entity, SculkPearlRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.item = new ItemStack(BlightEntities.SCULK_PEARL_ITEM.get());
    }

    @Override
    public void render(SculkPearlRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        this.itemRenderer.renderStatic(state.item, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);
        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SculkPearlRenderState state) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public static class SculkPearlRenderState extends EntityRenderState {
        public ItemStack item = ItemStack.EMPTY;
    }
}
