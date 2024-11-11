package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.BiConsumer;

public class Skitter extends Monster implements VibrationListener.VibrationListenerConfig {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;

    public AnimationState walkAnimationState = new AnimationState();
    public AnimationState runAnimationState = new AnimationState();

    public Skitter(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        //this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, (VibrationListener.ReceivingEvent)null, 0.0F, 0));
        this.xpReward = 10;
        this.getNavigation().setCanFloat(true);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 40, this, null, 0.0F, 0));

        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    public boolean shouldListen(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, GameEvent.Context context) {
        if(event.equals(GameEvent.STEP) && level.getRandom().nextDouble() < 0.975) return false;

        if(!this.isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !this.isRemoved() && this.level == level) {
            Entity entity = context.sourceEntity();
            if(entity instanceof LivingEntity livingentity) {
                return this.canTargetEntity(livingentity);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSignalReceive(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, @Nullable Entity entity1, @Nullable Entity entity2, float f) {
        if(isDeadOrDying()) return;

        //this.playSound(BlightSounds.REAPER_CLICK.get(), 0.4F, -1);

        if(entity1 != null) {
            if(canTargetEntity(entity1)) {
                if(!(entity1 instanceof Monster)) {
                    if (this.getTarget() == null || (this.getTarget() != null && this.getRandom().nextDouble() <= 0.225)) {
                        this.setTarget((LivingEntity) entity1);
                    }
                    if(entity1 instanceof Player)
                        this.setTarget((LivingEntity) entity1);
                }
                return;
            }
        }

        if(this.getTarget() != null && this.getRandom().nextDouble() <= 0.225)
            this.setTarget(null);

        this.disturbanceLocation = pos;
    }

    @Contract("null->false")
    public boolean canTargetEntity(@javax.annotation.Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level == p_219386_.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
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

    public static boolean canSpawn(EntityType<Skitter> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return checkMobSpawnRules(entityType, level, type, pos, rand);
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.3f;
    }

    @Override
    public void tick() {
        super.tick();
        if(isDeadOrDying()) return;

        Level level = this.level;
        if(level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }


        if (this.getSpeed() > 0) {
            if (this.getTarget() == null && !this.walkAnimationState.isStarted()) {
                if (this.runAnimationState.isStarted()) {
                    this.runAnimationState.stop();
                }
                this.walkAnimationState.start(this.tickCount);
            } else if (!this.runAnimationState.isStarted()) {
                if (this.walkAnimationState.isStarted()) {
                    this.walkAnimationState.stop();
                }
                this.runAnimationState.start(this.tickCount);
            }
        }
        else {
            this.walkAnimationState.stop();
            this.runAnimationState.stop();
        }
    }

    @Override
    public TagKey<GameEvent> getListenableEvents() {
        return GameEventTags.WARDEN_CAN_LISTEN;
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
        Level level = this.level;
        if(level instanceof ServerLevel serverlevel) {
            consumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.7D, true));
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
    public boolean canTriggerAvoidVibration() {
        return true;
    }

//    @Override
//    public PathNavigation createNavigation(Level pLevel) {
//        return new GroundPathNavigation(this, pLevel) {
//            protected PathFinder createPathFinder(int pMaxVisitedNodes) {
//                this.nodeEvaluator = new WalkNodeEvaluator();
//                this.nodeEvaluator.setCanPassDoors(true);
//                return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes) {
//                    protected float distance(Node node1, Node node2) {
//                        return node1.distanceToXZ(node2);
//                    }
//                };
//            }
//        };
//    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 60.0D)
                .add(Attributes.MOVEMENT_SPEED,  .25)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D);
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
            if(entity.getDisturbanceLocation() != null && !entity.getNavigation().isInProgress()) {
                Path path = entity.getNavigation().createPath(entity.getDisturbanceLocation(), 0);
                entity.getNavigation().moveTo(path, 1);

            }
            if(entity.getDisturbanceLocation() != null && entity.getNavigation().isDone()) {
                entity.setDisturbanceLocation(null);
            }



        }

        @Override
        public void start() {

        }
    }

}
