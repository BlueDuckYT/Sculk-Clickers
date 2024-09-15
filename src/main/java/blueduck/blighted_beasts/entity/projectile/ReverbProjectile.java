package blueduck.blighted_beasts.entity.projectile;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
import blueduck.blighted_beasts.registry.BlightEntities;
import com.google.common.base.MoreObjects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ReverbProjectile extends ThrowableProjectile {
    private int duration = 200;

    public ReverbProjectile(EntityType<? extends ReverbProjectile> p_37411_, Level p_37412_) {
        super(p_37411_, p_37412_);
    }

    public ReverbProjectile(Level p_37419_, LivingEntity p_37420_) {
        super(BlightEntities.REVERB_PROJECTILE.get(), p_37420_, p_37419_);
    }

    public ReverbProjectile(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
        super(BlightEntities.REVERB_PROJECTILE.get(), p_37415_, p_37416_, p_37417_, p_37414_);
    }

    @Override
    public void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        Entity entity = p_37259_.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;

        boolean flag =  (livingentity instanceof Reverb && ((livingentity).hasEffect(BlightEntities.CONFUSION.get())) || (!(entity instanceof Reverb) && !(entity instanceof Reaper)));
        if (flag) {
            entity.hurt(DamageSource.thrown(this, livingentity), Config.reverbDamage);
        }
        this.discard();
        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for(int i = 0; i < 2; ++i) {
            this.level.addParticle(ParticleTypes.SCULK_SOUL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * .5D, -this.random.nextDouble() * 0.25 + .1, (this.random.nextDouble() - 0.5D) * .5D);
        }
    }

    protected void onHitBlock(BlockHitResult p_36755_) {
        this.discard();
    }

    public void tick() {
        super.tick();

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            f = 1/0.8F;
        } else {
            f = 1/0.99F;
        }

        this.setDeltaMovement(vec3.scale((double)f));


        this.setPos(d2, d0, d1);
    }

    @Override
    protected void defineSynchedData() {

    }

    protected float getGravity() {
        return 0.0015F;
    }
}
