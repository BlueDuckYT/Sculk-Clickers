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
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Beckon extends AbstractGolem implements IAnimatable {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Beckon.class, EntityDataSerializers.BYTE);

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public int timer;
    public int screamTime = 240;

    public int healTime;

    public Beckon(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
        this.getPersistentData().putBoolean("PersistenceRequired", true);
        timer = screamTime;
        healTime = 200;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public void addAdditionalSaveData(CompoundTag p_21819_) {
        super.addAdditionalSaveData(p_21819_);
    }

    public void readAdditionalSaveData(CompoundTag p_21815_) {
        super.readAdditionalSaveData(p_21815_);

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0025F));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, (double)0.36F);
    }



    public InteractionResult mobInteract(Player p_30412_, InteractionHand p_30413_) {
        ItemStack itemstack = p_30412_.getItemInHand(p_30413_);
        Item item = itemstack.getItem();

        return super.mobInteract(p_30412_, p_30413_);
    }

    public void aiStep() {
        this.updateSwingTimeTurret();

        timer--;
        if (timer <= 0) {
            timer = screamTime;
            //Scream
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(BlightSounds.BECKON_SHRIEK.get(), 1.75F, 1.0F);
            this.level.gameEvent(GameEvent.ENTITY_DAMAGE, this.blockPosition(), GameEvent.Context.of(this, this.level.getBlockState(this.blockPosition())));
            this.level.gameEvent(GameEvent.ENTITY_ROAR, this.blockPosition(), GameEvent.Context.of(this, this.level.getBlockState(this.blockPosition())));


            AABB aabb = (new AABB(this.getOnPos())).inflate(Config.beckonRange);
            List<LivingEntity> nearbyEntities = this.getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != null && !entity.isDeadOrDying() && (entity instanceof Monster) && (!Config.beckonDistractsSculkOnly || (entity instanceof VibrationListener.VibrationListenerConfig))) {
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

    public boolean causeFallDamage(float p_148875_, float p_148876_, DamageSource p_148877_) {
        return false;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;

    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && this.swingTime != -1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.scream", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();

        return PlayState.STOP;

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
