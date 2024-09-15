package blueduck.blighted_beasts.client.renderer;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.client.model.SeerModel;
import blueduck.blighted_beasts.entity.Seer;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SeerEyesLayer<T extends Seer> extends EyesLayer<T, SeerModel<T>> {
    private static final RenderType SEER_EYES = RenderType.eyes(new ResourceLocation(BlightedBeasts.MODID, "textures/entity/seer_eyes.png"));

    public SeerEyesLayer(RenderLayerParent<T, SeerModel<T>> p_116964_) {
        super(p_116964_);
    }

    public RenderType renderType() {
        return SEER_EYES;
    }
}
