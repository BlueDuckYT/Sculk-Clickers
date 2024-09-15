package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.goal.BloaterSwellGoal;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;

public class Bloater extends Monster implements VibrationListener.VibrationListenerConfig {

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    private int oldSwell;
    public int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;

    public Bloater(EntityType<? extends Bloater> p_32278_, Level p_32279_) {
        super(p_32278_, p_32279_);

        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BloaterSwellGoal(this));
//        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
//        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new GoToDisturbanceGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        //this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
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
    public void onSignalReceive(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, @org.jetbrains.annotations.Nullable Entity entity1, @org.jetbrains.annotations.Nullable Entity entity2, float f) {
        if(isDeadOrDying()) return;

        this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 0.4F, -1);

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
                return (livingentity instanceof Player || (Config.bloatersAttackAllMobs && !(livingentity instanceof Monster)));
            }
        }

        return false;
    }

    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, p_149689_);
        this.swell += (int)(p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
    }

    public void addAdditionalSaveData(CompoundTag p_32304_) {
        super.addAdditionalSaveData(p_32304_);

        p_32304_.putShort("Fuse", (short)this.maxSwell);
        p_32304_.putByte("ExplosionRadius", (byte)this.explosionRadius);
    }

    public void readAdditionalSaveData(CompoundTag p_32296_) {
        super.readAdditionalSaveData(p_32296_);
        if (p_32296_.contains("Fuse", 99)) {
            this.maxSwell = p_32296_.getShort("Fuse");
        }

        if (p_32296_.contains("ExplosionRadius", 99)) {
            this.explosionRadius = p_32296_.getByte("ExplosionRadius");
        }


    }

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

        Level level = this.level;
        if(level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }

        super.tick();
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

    public void setTarget(@Nullable LivingEntity p_149691_) {
        if (!(p_149691_ instanceof Goat)) {
            super.setTarget(p_149691_);
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_32309_) {
        return SoundEvents.WARDEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }


    public boolean doHurtTarget(Entity p_32281_) {
        return true;
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
        if (!this.level.isClientSide) {
            this.dead = true;
            placeFeature(getServer().getLevel(getLevel().dimension()), Holder.direct(getLevel().registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(new ResourceLocation("blighted_beasts","sculk_patch_bloater"))), blockPosition());
            for(int i = 0; i < 64; ++i) {
                this.level.addParticle(ParticleTypes.SCULK_SOUL, this.getX() + this.random.nextDouble() * 0.2 - 0.1, this.getY() + this.random.nextDouble() * 0.2 - 0.1, this.getZ() + this.random.nextDouble() * 0.2 - 0.1, this.random.nextGaussian(), this.random.nextDouble() * 0.1 - 0.05, this.random.nextGaussian());
            }
            this.discard();

            AABB aabb = (new AABB(this.getOnPos())).inflate(2.0D);
            List<LivingEntity> nearbyEntities = this.getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity2 : nearbyEntities) {
                if (entity2 != null && !entity2.isDeadOrDying()) {
                    entity2.hurt(DamageSource.explosion(this), 12f);
                }
            }
            //this.spawnLingeringCloud();
        }

    }

    public static void placeFeature(ServerLevel serverLevel, Holder<ConfiguredFeature<?,?>> holder, BlockPos blockPos) {
        ConfiguredFeature<?,?> configuredFeature = holder.value();
        configuredFeature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), serverLevel.getRandom(), blockPos);
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level.addFreshEntity(areaeffectcloud);
        }

    }



    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    public static boolean canSpawn(EntityType<Bloater> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return checkMobSpawnRules(entityType, level, type, pos, rand);
    }


    public BlockPos getDisturbanceLocation() {
        return this.disturbanceLocation;
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
}
