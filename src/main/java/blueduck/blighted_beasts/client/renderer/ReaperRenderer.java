package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.ReaperModel;
import blueduck.blighted_beasts.entity.Reaper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReaperRenderer extends MobRenderer<Reaper, ReaperModel<Reaper>> {
    public ReaperRenderer(EntityRendererProvider.Context context) {
        super(context, new ReaperModel<>(context.bakeLayer(ReaperModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Reaper pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/reaper.png");
    }


}