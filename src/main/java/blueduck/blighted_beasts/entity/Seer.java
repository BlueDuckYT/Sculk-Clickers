package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class Seer extends EnderMan {


    public Seer(EntityType<? extends EnderMan> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new Seer.SeerFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new EnderMan.EndermanLookForPlayerGoal(this, this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_28879_) -> Config.canSeerDetectPlayers && p_28879_.distanceTo(this) < 12));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, (double)0.275F).add(Attributes.ATTACK_DAMAGE, 9.0D).add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    boolean isLookingAtMe(Player p_32535_) {
        ItemStack itemstack = p_32535_.getInventory().armor.get(3);
        if (net.minecraftforge.common.ForgeHooks.shouldSuppressEnderManAnger(this, p_32535_, itemstack)) {
            return false;
        } else {
            Vec3 vec3 = p_32535_.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - p_32535_.getX(), this.getEyeY() - p_32535_.getEyeY(), this.getZ() - p_32535_.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > 1.0D - 0.025D / d0 ? p_32535_.hasLineOfSight(this) : false;
        }
    }


    protected float getStandingEyeHeight(Pose p_32517_, EntityDimensions p_32518_) {
        return 2.1F;
    }

    public static boolean canSpawn(EntityType<Seer> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return checkMobSpawnRules(entityType, level, type, pos, rand);
    }

    static class SeerFreezeWhenLookedAt extends Goal {
        private final Seer enderman;
        @Nullable
        private LivingEntity target;

        public SeerFreezeWhenLookedAt(Seer p_32550_) {
            this.enderman = p_32550_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.enderman);
                return d0 > 256.0D ? false : this.enderman.isLookingAtMe((Player)this.target);
            }
        }

        public void start() {

            this.enderman.getNavigation().stop();
            this.enderman.playSound(BlightSounds.SEER_SHRIEK.get(), 2.5F, 1.0F);

            AABB aabb = (new AABB(this.enderman.getOnPos())).inflate(48.0D);
            List<LivingEntity> nearbyEntities = this.enderman.getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != null && !entity.isDeadOrDying() && (entity instanceof Monster) && (Config.canSeerAttackNonSculk || (entity instanceof VibrationListener.VibrationListenerConfig))) {
                    if (target != null && !target.isDeadOrDying()) {
                        ((Monster) entity).setTarget(target);
                    }
                }
            }
        }

        public void tick() {
            this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }

    public void aiStep() {
        if (this.level.isClientSide && this.level.getRandom().nextDouble() < 0.05) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.SCULK_SOUL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * .5D, -this.random.nextDouble() * 0.25 + .1, (this.random.nextDouble() - 0.5D) * .5D);
            }
        }

        super.aiStep();
    }

    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? BlightSounds.SEER_AGGRO.get() : BlightSounds.SEER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_32527_) {
        return BlightSounds.SEER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return BlightSounds.SEER_DEATH.get();
    }


}
