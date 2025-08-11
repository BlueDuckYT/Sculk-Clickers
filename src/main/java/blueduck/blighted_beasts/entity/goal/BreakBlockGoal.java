package blueduck.blighted_beasts.entity.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import blueduck.blighted_beasts.entity.Behemoth;

public class BreakBlockGoal extends DoorInteractGoal {
    private final Predicate<Difficulty> validDifficulties;
    private int cooldown = 0;
    private static final double CHECK_RANGE = 3.0;

    public BreakBlockGoal(Behemoth goriller, Predicate<Difficulty> difficultyPredicate) {
        super(goriller);
        this.validDifficulties = difficultyPredicate;
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() == null || !this.isValidDifficulty(mob.level.getDifficulty())) {
            return false;
        }


        // Only activate when on cooldown and there are obstructing blocks
        return hasObstructingBlocks() && mob.position().distanceToSqr(doorPos.getX(), doorPos.getY(), doorPos.getZ()) < 5 && this.mob.getNavigation().getPath() != null && !this.mob.getNavigation().getPath().canReach();
    }

    private boolean hasObstructingBlocks() {
        LivingEntity target = mob.getTarget();
        if (target == null) return false;

        Level level = mob.level;
        List<BlockPos> obstructingBlocks = new ArrayList<>();

        // Check blocks along the line between mob and target
        Vec3 start = mob.position().add(0, mob.getBbHeight()/2, 0);
        Vec3 end = target.position().add(0, target.getBbHeight()/2, 0);
        double distance = start.distanceTo(end);

        // Sample points along the line
        for (double d = 0; d <= distance; d += 0.5) {
            double progress = d / distance;
            BlockPos checkPos = new BlockPos(
                    Mth.lerp(progress, start.x, end.x),
                    Mth.lerp(progress, start.y, end.y),
                    Mth.lerp(progress, start.z, end.z)
            );

            // Check if block is solid and breakable
            BlockState state = level.getBlockState(checkPos);
            if (!state.isAir() && state.getDestroySpeed(level, checkPos) <= 2.0f) {
                VoxelShape collisionShape = state.getCollisionShape(level, checkPos);
                if (!collisionShape.isEmpty()) {
                    obstructingBlocks.add(checkPos);
                }
            }
        }

        // Set the doorPos to the first obstructing block found
        if (!obstructingBlocks.isEmpty()) {
            this.doorPos = obstructingBlocks.get(0);
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        // Break blocks in a 3x3 area around the obstruction
        breakObstructingBlocks();
        //this.cooldown = 20; // 1 second cooldown
        this.mob.swing(this.mob.getUsedItemHand());
        ((Behemoth)this.mob).animState = 2; // Set smash animation
    }

    private void breakObstructingBlocks() {
        Level level = this.mob.level;



        // Break blocks in 3x3 area around the obstruction
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos targetPos = doorPos.offset(x, y, z);
                    BlockState state = level.getBlockState(targetPos);

                    if (state.getDestroySpeed(level, targetPos) <= 2.0f) {
                        level.destroyBlock(targetPos, true);
                        level.levelEvent(2001, targetPos, Block.getId(state));
                    }
                }
            }
        }

        // Play explosion sound effect
        level.levelEvent(1021, doorPos, 0);
    }

    @Override
    public void tick() {

    }

    @Override
    public void stop() {
        super.stop();
        // Reset state for next use
        this.doorPos = null;
    }

    @Override
    public boolean canContinueToUse() {
        // We want this to be a one-time execution per activation
        return false;
    }

    private boolean isValidDifficulty(Difficulty difficulty) {
        return true;//this.validDifficulties.test(difficulty);
    }
}