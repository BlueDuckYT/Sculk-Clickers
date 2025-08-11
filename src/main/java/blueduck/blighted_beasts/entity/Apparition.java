package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class Apparition extends Vex implements VibrationListener.VibrationListenerConfig, IAnimatable {

    public BlockPos disturbanceLocation = null;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Apparition(EntityType<? extends Vex> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));
    }




    @Override
    public boolean shouldListen(ServerLevel level, GameEventListener eventListener, BlockPos pos, GameEvent event, GameEvent.Context context) {
        if(event.equals(GameEvent.STEP)) return false;

        if(!this.isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !this.isRemoved() && this.level == level) {
            Entity entity = context.sourceEntity();
            if(entity instanceof Player) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSignalReceive(ServerLevel p_223865_, GameEventListener p_223866_, BlockPos p_223867_, GameEvent p_223868_, @Nullable Entity p_223869_, @Nullable Entity p_223870_, float p_223871_) {

    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (this.swinging && this.swingTime != -1) {
            return PlayState.STOP;
        }
        else if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.move", ILoopType.EDefaultLoopTypes.LOOP));
        }

        return PlayState.CONTINUE;

    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && this.swingTime != -1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                5, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public TagKey<GameEvent> getListenableEvents() {
        return GameEventTags.WARDEN_CAN_LISTEN;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ARMOR, 5.0D);
    }

    protected SoundEvent getAmbientSound() {
        return BlightSounds.APPARITION_AMBIENT.get();
    }

    protected SoundEvent getDeathSound() {
        return BlightSounds.APPARITION_DEATH.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_34023_) {
        return BlightSounds.APPARITION_HURT.get();
    }

    public void aiStep() {
        this.updateSwingTimeTurret();
        super.aiStep();
    }
    @Override
    public void tick() {
        super.tick();
        if(isDeadOrDying()) return;

        Level level = this.level;
        if(level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }

        int i = 60;
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

    protected void updateSwingTimeTurret() {
        int i = 80;
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
