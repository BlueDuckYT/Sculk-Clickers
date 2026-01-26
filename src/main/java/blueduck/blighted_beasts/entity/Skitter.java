package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.BiConsumer;

public class Skitter extends Monster implements GeoEntity, VibrationSystem {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
    private final VibrationSystem.User vibrationUser;
    private final VibrationSystem.Data vibrationData;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Skitter(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        this.xpReward = 5;
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
        this.vibrationUser = new Skitter.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
    }

    public boolean canTargetEntity(@Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level() == p_219386_.level() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return (livingentity instanceof Player || (Config.skittersAttackAllMobs && !(livingentity instanceof Monster)));
            }
        }
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BlightSounds.SKITTER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.SKITTER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BlightSounds.SKITTER_HURT.get();
    }

    public static boolean canSpawn(EntityType<Skitter> entityType, ServerLevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, level, type, pos, rand);
    }

    @Override
    public void tick() {
        if(this.level() instanceof ServerLevel level) {
            VibrationSystem.Ticker.tick(level, this.vibrationData, this.vibrationUser);
        }
        super.tick();
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            consumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return vibrationUser;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ARMOR, 4.0D);
    }

    private PlayState predicate(AnimationState state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.model.walk"));
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.model.idle"));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState state) {
        if (this.swinging && this.swingTime != -1) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.attack"));
            return PlayState.CONTINUE;
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
    }

    public void setDisturbanceLocation(BlockPos disturbanceLocation) {
        this.disturbanceLocation = disturbanceLocation;
    }

    public class GoToDisturbanceGoal extends Goal {
        private final Skitter entity;

        public GoToDisturbanceGoal(Skitter entity) {
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

    class VibrationUser implements VibrationSystem.User {
        private final PositionSource positionSource = new EntityPositionSource(Skitter.this, Skitter.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        @Override
        public boolean canTriggerAvoidVibration() {
            return true;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
            if (event.value().equals(GameEvent.STEP)) return false;

            if (!isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !isRemoved() && level() == level) {
                Entity entity = context.sourceEntity();
                if (entity instanceof LivingEntity livingentity) {
                    return canTargetEntity(livingentity);
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, Holder<GameEvent> pGameEvent, Entity pEntity, Entity pPlayerEntity, float pDistance) {
            if (isDeadOrDying()) return;
            if (pEntity != null) {
                if (canTargetEntity(pEntity)) {
                    if (!(pEntity instanceof Monster)) {
                        setTarget((LivingEntity) pEntity);
                    }
                    return;
                }
            }
            if (getTarget() != null) setTarget(null);
            disturbanceLocation = pPos;
        }
    }
}
