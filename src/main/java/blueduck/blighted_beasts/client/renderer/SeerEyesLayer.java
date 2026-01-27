package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SeerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SeerEyesLayer extends EyesLayer<SeerRenderState, SeerModel> {
    private static final RenderType SEER_EYES = RenderType.eyes(ResourceLocation.fromNamespaceAndPath(BlightedBeasts.MODID, "textures/entity/seer_eyes.png"));

    public SeerEyesLayer(RenderLayerParent<SeerRenderState, SeerModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SEER_EYES;
    }
}
