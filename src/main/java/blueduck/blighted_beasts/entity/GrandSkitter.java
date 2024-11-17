package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class GrandSkitter extends Skitter{
    public GrandSkitter(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 80.0D)
                .add(Attributes.MOVEMENT_SPEED,  .2)
                .add(Attributes.ATTACK_DAMAGE, 24)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.9D, true));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            this.playSound(BlightSounds.SKITTER_WALK.get(), 2.0F, .6F);
        }
        else {
            this.playSound(BlightSounds.SKITTER_RUN.get(), 2.5F, 0.2F);
        }

    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.6f;
    }

    public float getStepHeight() {
        return 3.5F;
    }

    @Override
    public void onSignalReceive(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, @Nullable Entity entity1, @Nullable Entity entity2, float f) {
        if(isDeadOrDying()) return;

        //this.playSound(BlightSounds.REAPER_CLICK.get(), 0.4F, -1);

        if(entity1 != null) {
            if(canTargetEntity(entity1)) {
                if(!(entity1 instanceof Monster)) {
                    if (this.getTarget() == null && this.getRandom().nextDouble() <= 0.025 || (this.getTarget() != null && this.getRandom().nextDouble() <= 0.225)) {
                        this.setTarget((LivingEntity) entity1);
                    }
                    if(entity1 instanceof Player && this.getRandom().nextDouble() <= 0.05)
                        this.setTarget((LivingEntity) entity1);
                }
                return;
            }
        }

        if(this.getTarget() != null && this.getRandom().nextDouble() <= 0.01)
            this.setTarget(null);

        this.disturbanceLocation = pos;
    }




    public static boolean canSpawnGrand(EntityType<GrandSkitter> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return checkMobSpawnRules(entityType, level, type, pos, rand);
    }

}
