package blueduck.blighted_beasts.entity.goal;

import blueduck.blighted_beasts.entity.Bloater;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BloaterSwellGoal extends Goal {
    private final Bloater bloater;
    @Nullable
    private LivingEntity target;

    public BloaterSwellGoal(Bloater p_25229_) {
        this.bloater = p_25229_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.bloater.getTarget();
        return this.bloater.swell > 0 || livingentity != null && this.bloater.distanceToSqr(livingentity) < 9.0D;
    }

    @Override
    public void start() {
        this.bloater.getNavigation().stop();
        this.target = this.bloater.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.bloater.setSwellDir(-1);
        } else if (this.bloater.distanceToSqr(this.target) > 49.0D) {
            this.bloater.setSwellDir(-1);
        } else if (!this.bloater.getSensing().hasLineOfSight(this.target)) {
            this.bloater.setSwellDir(-1);
        } else {
            this.bloater.setSwellDir(1);
        }
    }
}
