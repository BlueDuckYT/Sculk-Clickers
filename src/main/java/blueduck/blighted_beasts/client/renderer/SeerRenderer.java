package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SeerModel;
import blueduck.blighted_beasts.entity.Seer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;

public class SeerRenderer extends MobRenderer<Seer, SeerModel<Seer>> {
    public SeerRenderer(EntityRendererProvider.Context context) {
        super(context, new SeerModel<>(context.bakeLayer(SeerModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new SeerEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Seer pEntity) {
        return new ResourceLocation(BlightedBeasts.MODID, "textures/entity/seer.png");
    }

    public void render(Seer p_114339_, float p_114340_, float p_114341_, PoseStack p_114342_, MultiBufferSource p_114343_, int p_114344_) {
        BlockState blockstate = p_114339_.getCarriedBlock();
        SeerModel<Seer> endermanmodel = this.getModel();
        endermanmodel.creepy = p_114339_.isCreepy();
        super.render(p_114339_, p_114340_, p_114341_, p_114342_, p_114343_, p_114344_);
    }


}