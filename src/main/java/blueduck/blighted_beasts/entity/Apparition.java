package blueduck.blighted_beasts.entity;

import blueduck.blighted_beasts.registry.BlightSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class Apparition extends Vex implements GeoEntity, VibrationSystem {

    private final VibrationSystem.User vibrationUser = new VibrationSystem.User() {
        private static final int LISTENER_RANGE = 16;
        private final EntityPositionSource positionSource = new EntityPositionSource(Apparition.this, Apparition.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return LISTENER_RANGE;
        }

        @Override
        public EntityPositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
            if (Apparition.this.isDeadOrDying() || Apparition.this.isRemoved() || !level.getWorldBorder().isWithinBounds(pos)) {
                return false;
            }
            if (gameEvent.value().equals(GameEvent.STEP)) {
                return false;
            }
            Entity entity = context.sourceEntity();
            return entity instanceof Player;
        }

        @Override
        public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float distance) {
            Apparition.this.disturbanceLocation = pos;
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }
    };

    private final VibrationSystem.Data vibrationData = new VibrationSystem.Data();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BlockPos disturbanceLocation = null;

    public Apparition(EntityType<? extends Vex> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    private PlayState predicate(AnimationState state) {
        if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.model.idle"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.model.move"));
        }
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
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ARMOR, 5.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BlightSounds.APPARITION_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BlightSounds.APPARITION_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BlightSounds.APPARITION_HURT.get();
    }

    @Override
    public void aiStep() {
        this.updateSwingTimeTurret();
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            VibrationSystem.Ticker.tick(serverLevel, this.vibrationData, this.vibrationUser);
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
