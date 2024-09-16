package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.projectile.ReverbProjectile;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
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

public class Reverb extends Monster implements VibrationListener.VibrationListenerConfig, RangedAttackMob {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;


    public Reverb(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        //this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, (VibrationListener.ReceivingEvent)null, 0.0F, 0));
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));

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

        this.playSound(BlightSounds.REVERB_CLICK.get(), 0.4F, -1);

        if(entity1 != null) {
            if(canTargetEntity(entity1)) {
                if(!(entity1 instanceof Monster)) {
                    this.setTarget((LivingEntity) entity1);
                    if(entity1 instanceof Player)
                        this.setTarget((LivingEntity) entity1);
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

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BlightSounds.REVERB_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.REVERB_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BlightSounds.REVERB_HURT.get();
    }

    public static boolean canSpawn(EntityType<Reverb> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
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
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 15.0f));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        //this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
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
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, (float)Config.reverbSpeed)
                .add(Attributes.ATTACK_DAMAGE, Config.reverbDamage)
                .add(Attributes.MAX_HEALTH, Config.reverbHealth)
                .add(Attributes.ARMOR, 2.0D);
    }

    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
    }

    public void performRangedAttack(LivingEntity p_32141_, float p_32142_) {
        ThrowableProjectile abstractarrow = new ReverbProjectile(p_32141_.getLevel(), this);
        double d0 = p_32141_.getX() - this.getX();
        double d1 = p_32141_.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = p_32141_.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 0.4F, (float)(14 - this.getLevel().getDifficulty().getId() * 4));
        this.playSound(BlightSounds.REVERB_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.setTarget(null);
        this.getLevel().addFreshEntity(abstractarrow);
    }

    public void setDisturbanceLocation(BlockPos disturbanceLocation) {
        this.disturbanceLocation = disturbanceLocation;
    }

    public class GoToDisturbanceGoal extends Goal {
        private final Reverb entity;

        public GoToDisturbanceGoal(Reverb entity) {
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
