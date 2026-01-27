package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Beckon extends AbstractGolem implements GeoEntity {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Beckon.class, EntityDataSerializers.BYTE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public int timer;
    public int screamTime = 240;
    public int healTime;

    public Beckon(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
        this.getPersistentData().putBoolean("PersistenceRequired", true);
        timer = screamTime;
        healTime = 200;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21819_) {
        super.addAdditionalSaveData(p_21819_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21815_) {
        super.readAdditionalSaveData(p_21815_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0025F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.36F);
    }

    @Override
    public InteractionResult mobInteract(Player p_30412_, InteractionHand p_30413_) {
        ItemStack itemstack = p_30412_.getItemInHand(p_30413_);
        Item item = itemstack.getItem();
        return super.mobInteract(p_30412_, p_30413_);
    }

    @Override
    public void aiStep() {
        this.updateSwingTimeTurret();

        timer--;
        if (timer <= 0) {
            timer = screamTime;
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(BlightSounds.BECKON_SHRIEK.get(), 1.75F, 1.0F);
            this.level().gameEvent(GameEvent.ENTITY_DAMAGE, this.blockPosition(), GameEvent.Context.of(this, this.level().getBlockState(this.blockPosition())));

            AABB aabb = (new AABB(this.getOnPos())).inflate(Config.beckonRange);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != null && !entity.isDeadOrDying() && (entity instanceof Monster) && (!Config.beckonDistractsSculkOnly || (entity instanceof VibrationSystem))) {
                    if (!this.isDeadOrDying()) {
                        ((Monster) entity).setTarget(this);
                    }
                }
            }
        }

        healTime--;
        if (healTime <= 0 && this.getHealth() < this.getMaxHealth()) {
            healTime = 200;
            this.heal(.05f);
        }

        super.aiStep();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    private PlayState predicate(AnimationTest<Beckon> state) {
        if (isMoving()) {
            state.controller().setAnimation(RawAnimation.begin().thenLoop("animation.model.walk"));
            return PlayState.CONTINUE;
        }
        state.controller().setAnimation(RawAnimation.begin().thenLoop("animation.model.idle"));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationTest<Beckon> state) {
        if (this.swinging && this.swingTime != -1) {
            state.controller().setAnimation(RawAnimation.begin().thenPlay("animation.model.scream"));
            return PlayState.CONTINUE;
        }
        state.controller().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, this::predicate));
        controllers.add(new AnimationController<>("attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
}
