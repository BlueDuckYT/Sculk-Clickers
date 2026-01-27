package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.projectile.ReverbProjectile;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.BiConsumer;

public class Reverb extends Monster implements VibrationSystem, RangedAttackMob {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
    private final VibrationSystem.User vibrationUser;
    private final VibrationSystem.Data vibrationData;

    public Reverb(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));

        this.vibrationUser = new Reverb.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    public boolean canTargetEntity(@Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level() == p_219386_.level() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
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

    public static boolean canSpawn(EntityType<Reverb> entityType, ServerLevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, level, type, pos, rand);
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.3f;
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
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 15.0f));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
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
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, (float) Config.reverbSpeed)
                .add(Attributes.ATTACK_DAMAGE, Config.reverbDamage)
                .add(Attributes.MAX_HEALTH, Config.reverbHealth)
                .add(Attributes.ARMOR, 2.0D);
    }

    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
    }

    @Override
    public void performRangedAttack(LivingEntity p_32141_, float p_32142_) {
        ThrowableProjectile abstractarrow = new ReverbProjectile(p_32141_.level(), this);
        double d0 = p_32141_.getX() - this.getX();
        double d1 = p_32141_.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = p_32141_.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 0.4F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(BlightSounds.REVERB_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.setTarget(null);
        this.level().addFreshEntity(abstractarrow);
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
        private final PositionSource positionSource = new EntityPositionSource(Reverb.this, Reverb.this.getEyeHeight());

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
            playSound(BlightSounds.REVERB_CLICK.get(), 2, 1);
            if (pEntity != null) {
                if (canTargetEntity(pEntity)) {
                    if (!(pEntity instanceof Monster)) {
                        setTarget((LivingEntity) pEntity);
                        if (pEntity instanceof Player)
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
