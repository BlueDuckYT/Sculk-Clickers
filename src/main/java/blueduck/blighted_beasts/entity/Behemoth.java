package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.entity.goal.BreakBlockGoal;
import blueduck.blighted_beasts.entity.goal.UnstickGoal;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Behemoth extends Monster implements GeoEntity, VibrationSystem {

    private final VibrationSystem.User vibrationUser = new VibrationSystem.User() {
        private static final int LISTENER_RANGE = 12;
        private final EntityPositionSource positionSource = new EntityPositionSource(Behemoth.this, Behemoth.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return LISTENER_RANGE;
        }

        @Override
        public EntityPositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
            if (gameEvent.value().equals(GameEvent.STEP)) return false;
            if (Behemoth.this.isDeadOrDying() || Behemoth.this.isRemoved() || !level.getWorldBorder().isWithinBounds(pos)) {
                return false;
            }
            if (Behemoth.this.getRandom().nextDouble() < 0.2) {
                Entity entity = context.sourceEntity();
                if (entity instanceof LivingEntity livingentity) {
                    return Behemoth.this.canTargetEntity(livingentity);
                }
                return true;
            }
            return false;
        }

        @Override
        public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float distance) {
            if (Behemoth.this.isDeadOrDying()) return;

            if (sourceEntity != null && Behemoth.this.canTargetEntity(sourceEntity)) {
                if (!(sourceEntity instanceof Monster)) {
                    Behemoth.this.setTarget((LivingEntity) sourceEntity);
                    if (sourceEntity instanceof Player) {
                        Behemoth.this.setTarget((LivingEntity) sourceEntity);
                    }
                    Behemoth.this.isTargeting = true;
                }
                return;
            }

            if (Behemoth.this.getTarget() != null) {
                Behemoth.this.setTarget(null);
            }

            Behemoth.this.disturbanceLocation = pos;
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }
    };

    private final VibrationSystem.Data vibrationData = new VibrationSystem.Data();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BlockPos disturbanceLocation = null;
    public int sniffTimer;
    public int animState = 0;
    public int animCounter = -1;
    public int xCounter = 0;
    public double oldX = 0;
    public double oldZ = 0;
    public double newX = 0;
    public double newZ = 0;
    public boolean isTargeting = false;

    public Behemoth(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.sniffTimer = 200;
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);

        this.setPersistenceRequired();
    }

    public boolean canTargetEntity(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingentity) {
            if (this.level() == entity.level() &&
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) &&
                    !this.isAlliedTo(entity) &&
                    livingentity.getType() != EntityType.ARMOR_STAND &&
                    livingentity.getType() != EntityType.WARDEN &&
                    !livingentity.isInvulnerable() &&
                    !livingentity.isDeadOrDying() &&
                    this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return !(livingentity instanceof Monster);
            }
        }
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            this.playSound(BlightSounds.BEHEMOTH_WALK.get(), 2.0F, 1.0F);
        } else {
            this.playSound(BlightSounds.BEHEMOTH_RUN.get(), 2.0F, 1.0F);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BlightSounds.BEHEMOTH_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.BEHEMOTH_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BlightSounds.BEHEMOTH_HURT.get();
    }

    public static boolean canSpawn(EntityType<Behemoth> entityType, ServerLevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, level, type, pos, rand);
    }

    @Override
    public void tick() {
        super.tick();
        xCounter++;
        if (xCounter >= 4) {
            xCounter = 0;
            oldX = newX;
            newX = xo;
            oldZ = newZ;
            newZ = zo;
        }
        if (animCounter > -1) {
            animCounter++;
        }
        if (animCounter >= 50) {
            animCounter = -1;
        }
        if (sniffTimer > 0) {
            sniffTimer--;
        }
        if (isDeadOrDying()) return;
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            this.isTargeting = false;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            VibrationSystem.Ticker.tick(serverLevel, this.vibrationData, this.vibrationUser);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.isTargeting = target != null;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.08D;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    public double getXZSpeed() {
        return (newX - oldX) * (newX - oldX) + (newZ - oldZ) * (newZ - oldZ);
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, this::predicate));
        controllers.add(new AnimationController<>("attackController", 0, this::attackPredicate));
    }

    private PlayState predicate(AnimationTest<Behemoth> state) {
        if (isMoving() && ((this.getXZSpeed() > .325D || (this.isTargeting && (this.getTarget() != null && !this.getTarget().isDeadOrDying()))))) {
            state.controller().setAnimation(RawAnimation.begin().thenLoop("animation.model.run"));
            return PlayState.CONTINUE;
        } else if (isMoving()) {
            state.controller().setAnimation(RawAnimation.begin().thenLoop("animation.model.walk"));
            return PlayState.CONTINUE;
        } else if (animCounter == -1) {
            state.controller().setAnimation(RawAnimation.begin().thenLoop("animation.model.idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private PlayState attackPredicate(AnimationTest<Behemoth> state) {
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BehemothMeleeAttackGoal());
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(4, new GorillaSniffGoal(this));
        this.goalSelector.addGoal(2, new BreakBlockGoal(this, (difficulty) -> true));
        this.goalSelector.addGoal(2, new UnstickGoal(this, (difficulty) -> true));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, .2)
                .add(Attributes.ATTACK_DAMAGE, 40)
                .add(Attributes.ATTACK_SPEED, 5)
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void aiStep() {
        this.updateSwingTimeTurret();
        super.aiStep();
    }

    protected void updateSwingTimeTurret() {
        int i = 100;
        if (this.swinging) {
            ++this.swingTime;
            if (this.swingTime >= i) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }
        this.attackAnim = (float) this.swingTime / (float) i;
    }

    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
    }

    public void setDisturbanceLocation(BlockPos disturbanceLocation) {
        this.disturbanceLocation = disturbanceLocation;
    }

    public class GoToDisturbanceGoal extends Goal {
        private final Behemoth entity;

        public GoToDisturbanceGoal(Behemoth entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (entity.getDisturbanceLocation() != null && !entity.getNavigation().isInProgress()) {
                Path path = entity.getNavigation().createPath(entity.getDisturbanceLocation(), 0);
                entity.getNavigation().moveTo(path, 1);
            }
            if (entity.getDisturbanceLocation() != null && entity.getNavigation().isDone()) {
                entity.setDisturbanceLocation(null);
            }
        }

        @Override
        public void start() {
        }
    }

    public class GorillaSniffGoal extends Goal {
        private final Behemoth entity;

        public GorillaSniffGoal(Behemoth entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            return entity.sniffTimer <= 0 && (entity.getTarget() == null || entity.getTarget().isDeadOrDying() || !entity.isTargeting);
        }

        @Override
        public void start() {
            sniffTimer = entity.getRandom().nextInt(600) + 200;
            entity.playSound(BlightSounds.BEHEMOTH_SNIFF.get(), 1.75F, 1.0F);
            entity.animState = 0;
            entity.animCounter = 0;
            entity.swing(InteractionHand.MAIN_HAND);

            AABB aabb = (new AABB(entity.getOnPos())).inflate(32);
            List<LivingEntity> nearbyEntities = entity.level().getEntitiesOfClass(LivingEntity.class, aabb);
            List<LivingEntity> entitiesToTarget = new ArrayList<>();
            for (LivingEntity entity2 : nearbyEntities) {
                if (entity2 != null && !entity2.isDeadOrDying() && entity.canTargetEntity(entity2)) {
                    entitiesToTarget.add(entity2);
                }
            }
            if (!entitiesToTarget.isEmpty()) {
                int closest = 0;
                for (int i = 1; i < entitiesToTarget.size(); i++) {
                    if (entitiesToTarget.get(i).distanceTo(entity) < entitiesToTarget.get(closest).distanceTo(entity)) {
                        closest = i;
                    }
                }
                if ((entity.getRandom().nextDouble() * 8 + 8) / entitiesToTarget.get(closest).distanceTo(entity) < 1) {
                    if (entity.getRandom().nextDouble() < 0.325) {
                        entity.setTarget(entitiesToTarget.get(closest));
                    } else {
                        entity.setDisturbanceLocation(entitiesToTarget.get(closest).blockPosition());
                    }
                }
            }
        }
    }

    class BehemothMeleeAttackGoal extends MeleeAttackGoal {
        public BehemothMeleeAttackGoal() {
            super(Behemoth.this, 2.0D, true);
        }

        // getAttackReachSqr was removed in 1.21.5 - attack reach is now determined by entity attributes
    }
}
