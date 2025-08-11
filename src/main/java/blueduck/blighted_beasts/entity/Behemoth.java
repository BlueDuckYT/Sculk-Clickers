package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.entity.goal.BreakBlockGoal;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;

public class Behemoth extends Monster implements VibrationListener.VibrationListenerConfig, IAnimatable {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;

    public int sniffTimer;
    public int animState = 0;

    public int animCounter = -1;

    public int xCounter = 0;
    public double oldX = 0;
    public double oldZ = 0;
    public double newX = 0;
    public double newZ = 0;

    public boolean isTargeting = false;

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Behemoth(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        //this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, (VibrationListener.ReceivingEvent)null, 0.0F, 0));
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 12, this, null, 0.0F, 0));
        sniffTimer = 200;
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    public boolean shouldListen(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, GameEvent.Context context) {
        if(event.equals(GameEvent.STEP)) return false;

        if(!this.isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !this.isRemoved() && this.level == level && this.getRandom().nextDouble() < 0.2) {
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
                    this.setTarget((LivingEntity) entity1);
                    if(entity1 instanceof Player)
                        this.setTarget((LivingEntity) entity1);
                    this.isTargeting = true;
                }
                return;
            }
        }

        if(this.getTarget() != null)
            this.setTarget(null);

        this.disturbanceLocation = pos;
    }

    @Contract("null->false")
    public boolean canTargetEntity(@javax.annotation.Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level == p_219386_.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return !(livingentity instanceof Monster);
            }
        }

        return false;
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            this.playSound(BlightSounds.BEHEMOTH_WALK.get(), 2.0F, 1.0F);
        }
        else {
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
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BlightSounds.BEHEMOTH_HURT.get();
    }

    public static boolean canSpawn(EntityType<Behemoth> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return checkMobSpawnRules(entityType, level, type, pos, rand);
    }

//    @Override
//    protected float nextStep() {
//        return this.moveDist + 0.3f;
//    }

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
        if(isDeadOrDying()) return;
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            this.isTargeting = false;
        }
        Level level = this.level;
        if(level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }
    }

    public void setTarget(@javax.annotation.Nullable LivingEntity p_21544_) {
        super.setTarget(p_21544_);
        this.isTargeting = p_21544_ != null;
    }

    public float getStepHeight() {
        return 1.0F;
    }

    @Override
    public TagKey<GameEvent> getListenableEvents() {
        return GameEventTags.WARDEN_CAN_LISTEN;
    }

    public double getXZSpeed() {
        return (newX - oldX)*(newX - oldX) + (newZ - oldZ)*(newZ - oldZ);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//        if (this.swinging || animCounter != -1) {
//            switch (animState) {
//                case 0: event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.sniff", ILoopType.EDefaultLoopTypes.LOOP));
//                case 1: event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", ILoopType.EDefaultLoopTypes.LOOP));
//                case 2: event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.smash", ILoopType.EDefaultLoopTypes.LOOP));
//            }
//            if (animCounter >= 48) {
//                event.getController().markNeedsReload();
//                return PlayState.STOP;
//            }
//            return PlayState.CONTINUE;
//        }

        if (event.isMoving() && (((Behemoth) event.getAnimatable()).getXZSpeed() > .325D || (this.isTargeting && (((Behemoth) event.getAnimatable()).getTarget() != null || !((Behemoth) event.getAnimatable()).getTarget().isDeadOrDying())))) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.run", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (animCounter == -1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;


    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
//        if (this.swinging) {
//
//        }
//
//        event.getController().markNeedsReload();
//
        return PlayState.STOP;

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.0D, true));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(4, new GorillaSniffGoal(this));
        this.goalSelector.addGoal(2, new BreakBlockGoal(this, (difficulty) -> true));
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
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, .2)
                .add(Attributes.ATTACK_DAMAGE, 40)
                .add(Attributes.ATTACK_SPEED, 5)
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

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

        this.attackAnim = (float)this.swingTime / (float)i;
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
        public void tick() {


        }

        @Override
        public void start() {
            sniffTimer = entity.getRandom().nextInt(600) + 200;
            //[SniffSound]
            entity.playSound(BlightSounds.BEHEMOTH_SNIFF.get(), 1.75F, 1.0F);

            entity.animState = 0;
            entity.animCounter = 0;
            entity.swing(InteractionHand.MAIN_HAND);

            AABB aabb = (new AABB(entity.getOnPos())).inflate(32);
            List<LivingEntity> nearbyEntities = entity.getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
            List<LivingEntity> entitiesToTarget = new ArrayList<>();
            for (LivingEntity entity2 : nearbyEntities) {
                if (entity2 != null && !entity2.isDeadOrDying() && entity.canTargetEntity(entity2)) {
                    entitiesToTarget.add(entity2);
                }
            }
            int closest = 0;
            for (int i = 1; i < entitiesToTarget.size(); i++) {
                if (entitiesToTarget.get(i).distanceTo(entity) < entitiesToTarget.get(closest).distanceTo(entity)) {
                    closest = i;
                }
            }
            if ((entity.getRandom().nextDouble() * 8 + 8) / entitiesToTarget.get(closest).distanceTo(entity) < 1) {
                if (entity.getRandom().nextDouble() < 0.325) {
                    entity.setTarget(entitiesToTarget.get(closest));
                }
                else {
                    entity.setDisturbanceLocation(entitiesToTarget.get(closest).blockPosition());
                }
            }
        }
    }

}
