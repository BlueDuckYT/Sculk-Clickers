package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.goal.BloaterSwellGoal;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;

public class Bloater extends Monster implements VibrationSystem {

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Bloater.class, EntityDataSerializers.INT);
    private int oldSwell;
    public int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;

    private final VibrationSystem.User vibrationUser;
    private final VibrationSystem.Data vibrationData;

    public Bloater(EntityType<? extends Bloater> p_32278_, Level p_32279_) {
        super(p_32278_, p_32279_);

        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));

        this.vibrationUser = new Bloater.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BloaterSwellGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public boolean canTargetEntity(@Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level() == p_219386_.level() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return (livingentity instanceof Player || (Config.bloatersAttackAllMobs && !(livingentity instanceof Monster)));
            }
        }
        return false;
    }

    @Override
    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    @Override
    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, p_149689_);
        this.swell += (int)(p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }
        return flag;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SWELL_DIR, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_32304_) {
        super.addAdditionalSaveData(p_32304_);
        p_32304_.putShort("Fuse", (short)this.maxSwell);
        p_32304_.putByte("ExplosionRadius", (byte)this.explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_32296_) {
        super.readAdditionalSaveData(p_32296_);
        if (p_32296_.contains("Fuse")) {
            this.maxSwell = p_32296_.getShort("Fuse").orElse((short)30);
        }
        if (p_32296_.contains("ExplosionRadius")) {
            this.explosionRadius = p_32296_.getByte("ExplosionRadius").orElse((byte)3);
        }
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }

        if(this.level() instanceof ServerLevel level) {
            Ticker.tick(level, this.vibrationData, this.vibrationUser);
        }
        super.tick();
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
        Level level = this.level();
        if(level instanceof ServerLevel serverlevel) {
            consumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_149691_) {
        if (!(p_149691_ instanceof Goat)) {
            super.setTarget(p_149691_);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_32309_) {
        return BlightSounds.BLOATER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.BLOATER_DEATH.get();
    }


    public float getSwelling(float p_32321_) {
        return Mth.lerp(p_32321_, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int p_32284_) {
        this.entityData.set(DATA_SWELL_DIR, p_32284_);
    }

    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            this.dead = true;
            if (this.level() instanceof ServerLevel serverLevel) {
                var registry = serverLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
                var featureKey = ResourceLocation.fromNamespaceAndPath("blighted_beasts", "sculk_patch_bloater");
                var feature = registry.get(net.minecraft.resources.ResourceKey.create(Registries.CONFIGURED_FEATURE, featureKey));
                if (feature.isPresent()) {
                    placeFeature(serverLevel, feature.get(), blockPosition());
                }
            }
            for(int i = 0; i < 64; ++i) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX() + this.random.nextDouble() * 0.2 - 0.1, this.getY() + this.random.nextDouble() * 0.2 - 0.1, this.getZ() + this.random.nextDouble() * 0.2 - 0.1, this.random.nextGaussian(), this.random.nextDouble() * 0.1 - 0.05, this.random.nextGaussian());
            }
            this.discard();

            AABB aabb = (new AABB(this.getOnPos())).inflate(2.0D);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity2 : nearbyEntities) {
                if (entity2 != null && !entity2.isDeadOrDying()) {
                    entity2.hurt(this.damageSources().explosion(this, this), 12f);
                }
            }
        }
    }

    public static void placeFeature(ServerLevel serverLevel, Holder<ConfiguredFeature<?,?>> holder, BlockPos blockPos) {
        ConfiguredFeature<?,?> configuredFeature = holder.value();
        configuredFeature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), serverLevel.getRandom(), blockPos);
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level().addFreshEntity(areaeffectcloud);
        }
    }

    public static boolean canSpawn(EntityType<Bloater> entityType, ServerLevelAccessor level, EntitySpawnReason type, BlockPos pos, RandomSource rand) {
        return checkMonsterSpawnRules(entityType, level, type, pos, rand);
    }

    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
    }

    @Override
    public Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return vibrationUser;
    }

    public void setDisturbanceLocation(BlockPos disturbanceLocation) {
        this.disturbanceLocation = disturbanceLocation;
    }

    public class GoToDisturbanceGoal extends Goal {
        private final Bloater entity;

        public GoToDisturbanceGoal(Bloater entity) {
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

    class VibrationUser implements VibrationSystem.User {
        private final PositionSource positionSource = new EntityPositionSource(Bloater.this, Bloater.this.getEyeHeight());

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
            if(event.value().equals(GameEvent.STEP)) return false;

            if(!isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !isRemoved() && level() == level) {
                Entity entity = context.sourceEntity();
                if(entity instanceof LivingEntity livingentity) {
                    return canTargetEntity(livingentity);
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, Holder<GameEvent> pGameEvent, Entity pEntity, Entity pPlayerEntity, float pDistance) {
            if(isDeadOrDying()) return;
            playSound(BlightSounds.BLOATER_CLICK.get(), 2, 1);
            if(pEntity != null) {
                if(canTargetEntity(pEntity)) {
                    if(!(pEntity instanceof Monster)) {
                        setTarget((LivingEntity) pEntity);
                        if(pEntity instanceof Player)
                            setTarget((LivingEntity) pEntity);
                    }
                    return;
                }
            }

            if(getTarget() != null) setTarget(null);
            disturbanceLocation = pPos;
        }
    }
}
