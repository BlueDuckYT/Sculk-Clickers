package blueduck.blighted_beasts.client.renderer;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SeerRenderState extends LivingEntityRenderState {
    public boolean creepy;
    @Nullable
    public BlockState carriedBlock;
}
