package blueduck.blighted_beasts.entity.projectile;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Bloater;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.entity.Reverb;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ReverbProjectile extends ThrowableProjectile {
    private int duration = 200;

    public ReverbProjectile(EntityType<? extends ReverbProjectile> type, Level level) {
        super(type, level);
    }

    public ReverbProjectile(Level level, LivingEntity owner) {
        super(BlightEntities.REVERB_PROJECTILE.get(), owner, level);
    }

    public ReverbProjectile(Level level, double x, double y, double z) {
        super(BlightEntities.REVERB_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;

        boolean shouldDamage = (livingOwner instanceof Reverb && livingOwner.hasEffect(BlightEntities.CONFUSION)) ||
                (!(entity instanceof Reverb) && !(entity instanceof Reaper) && !(entity instanceof Bloater));
        if (shouldDamage) {
            entity.hurt(this.damageSources().thrown(this, livingOwner), Config.reverbDamage);
        }
        this.discard();
        Vec3 vec3 = this.getDeltaMovement();
        for (int i = 0; i < 2; ++i) {
            this.level().addParticle(ParticleTypes.SCULK_SOUL, 
                    this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 
                    (this.random.nextDouble() - 0.5D) * 0.5D, -this.random.nextDouble() * 0.25 + 0.1, 
                    (this.random.nextDouble() - 0.5D) * 0.5D);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
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
            f = 1 / 0.8F;
        } else {
            f = 1 / 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double) f));
        this.setPos(d2, d0, d1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No synched data needed
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0015;
    }
}
