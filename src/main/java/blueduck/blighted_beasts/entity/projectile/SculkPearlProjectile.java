package blueduck.blighted_beasts.entity.projectile;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Seer;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SculkPearlProjectile extends ThrowableItemProjectile {
    public SculkPearlProjectile(EntityType<? extends SculkPearlProjectile> type, Level level) {
        super(type, level);
    }

    public SculkPearlProjectile(Level level, LivingEntity owner) {
        super(BlightEntities.SCULK_PEARL.get(), owner, level);
    }

    public SculkPearlProjectile(Level level, double x, double y, double z) {
        super(BlightEntities.SCULK_PEARL.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlightEntities.SCULK_PEARL_ITEM.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        for (int i = 0; i < 32; ++i) {
            this.level().addParticle(ParticleTypes.SCULK_SOUL, 
                    this.getX() + this.random.nextDouble() * 0.2 - 0.1, 
                    this.getY() + this.random.nextDouble() * 0.2 - 0.1, 
                    this.getZ() + this.random.nextDouble() * 0.2 - 0.1, 
                    this.random.nextGaussian(), this.random.nextDouble() * 0.1 - 0.05, this.random.nextGaussian());
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            Entity entity = this.getOwner();
            this.playSound(SoundEvents.SCULK_SHRIEKER_SHRIEK, 2.5F, 1.0F);

            List<Monster> enemiesToRetarget = new ArrayList<>();

            AABB aabb = (new AABB(this.getOnPos())).inflate(20.0D);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity2 : nearbyEntities) {
                if (entity2 != null && !entity2.isDeadOrDying() && (entity2 instanceof Monster) && 
                        (Config.canSeerAttackNonSculk || (entity2 instanceof VibrationSystem)) && !(entity2 instanceof Seer)) {
                    entity2.addEffect(new MobEffectInstance(BlightEntities.CONFUSION, Config.sculkPearlDuration, 0));
                    ((Monster) entity2).setTarget(null);
                }
            }

            this.discard();
        }
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
    }

    @Nullable
    @Override
    public Entity teleport(TeleportTransition transition) {
        Entity entity = this.getOwner();
        if (entity != null && transition.newLevel() != null && entity.level().dimension() != transition.newLevel().dimension()) {
            this.setOwner(null);
        }

        return super.teleport(transition);
    }
}
