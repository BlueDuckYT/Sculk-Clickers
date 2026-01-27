package blueduck.blighted_beasts.entity.goal;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Behemoth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UnstickGoal extends DoorInteractGoal {
    private final Predicate<Difficulty> validDifficulties;
    private int cooldown = 0;
    private static final double CHECK_RANGE = 3.0;

    public UnstickGoal(Behemoth goriller, Predicate<Difficulty> difficultyPredicate) {
        super(goriller);
        this.validDifficulties = difficultyPredicate;
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() == null || !this.isValidDifficulty(mob.level().getDifficulty()) || !Config.behemothBreaksBlocks) {
            return false;
        }

        // Only activate when on cooldown and there are obstructing blocks
        return hasObstructingBlocks() && mob.position().distanceToSqr(doorPos.getX(), doorPos.getY(), doorPos.getZ()) > 5 && this.mob.getNavigation().getPath() != null && !this.mob.getNavigation().getPath().canReach();
    }

    private boolean hasObstructingBlocks() {
        LivingEntity target = mob.getTarget();
        if (target == null) return false;

        Level level = mob.level();
        List<BlockPos> obstructingBlocks = new ArrayList<>();

        // Check blocks along the line between mob and target
        Vec3 start = mob.position().add(0, mob.getBbHeight()/2, 0);
        Vec3 end = target.position().add(0, target.getBbHeight()/2, 0);
        double distance = start.distanceTo(end);

        // Sample points along the line
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos checkPos = mob.blockPosition().offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.isAir() && state.getDestroySpeed(level, checkPos) <= Config.behemothHardness && state.getDestroySpeed(level, checkPos) >= 0 && state.getBlock().getExplosionResistance() < 1000.0f) {
                        VoxelShape collisionShape = state.getCollisionShape(level, checkPos);
                        if (!collisionShape.isEmpty()) {
                            obstructingBlocks.add(checkPos);
                        }
                    }
                }
            }
        }

        // Set the doorPos to the first obstructing block found
        if (!obstructingBlocks.isEmpty()) {
            this.doorPos = mob.blockPosition();
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        // Break blocks in a 3x3 area around the obstruction
        breakObstructingBlocks();
        this.mob.swing(this.mob.getUsedItemHand());
        ((Behemoth)this.mob).animState = 2; // Set smash animation
    }

    private void breakObstructingBlocks() {
        Level level = this.mob.level();

        // Break blocks in 3x3 area around the obstruction
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos targetPos = mob.blockPosition().offset(x, y, z);
                    BlockState state = level.getBlockState(targetPos);

                    if (state.getDestroySpeed(level, targetPos) <= Config.behemothHardness && state.getDestroySpeed(level, targetPos) >= 0 && state.getBlock().getExplosionResistance() < 1000.0f) {
                        level.destroyBlock(targetPos, true);
                        level.levelEvent(2001, targetPos, Block.getId(state));
                    }
                }
            }
        }

        // Play explosion sound effect
        level.levelEvent(1021, mob.blockPosition(), 0);
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
        return true;
    }
}
