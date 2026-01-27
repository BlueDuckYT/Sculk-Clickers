package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightEntities;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class Seer extends Monster {

    public Seer(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        this.xpReward = 5;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BlightSounds.SEER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.SEER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BlightSounds.SEER_HURT.get();
    }

    public static boolean canSpawn(EntityType<Seer> entityType, ServerLevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, level, type, pos, rand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        if(Config.canSeerDetectPlayers) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            AABB aabb = (new AABB(this.getOnPos())).inflate(20.0D);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != null && !entity.isDeadOrDying() && (entity instanceof Monster) && (Config.canSeerAttackNonSculk || (entity instanceof VibrationSystem)) && !(entity instanceof Seer)) {
                    entity.addEffect(new MobEffectInstance(BlightEntities.CONFUSION.getDelegate(), 100, 0));
                }
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 2.0D);
    }
}
