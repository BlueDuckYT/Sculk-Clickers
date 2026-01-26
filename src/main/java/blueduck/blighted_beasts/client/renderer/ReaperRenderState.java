package blueduck.blighted_beasts.client.renderer;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReaperRenderState extends LivingEntityRenderState {
    @Nullable
    public BlockPos disturbanceLocation;
    public boolean hasTarget;
    public float attackTime;
}
