package blueduck.blighted_beasts.entity.projectile;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Seer;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SculkPearlProjectile extends ThrowableItemProjectile {
    public SculkPearlProjectile(EntityType<? extends SculkPearlProjectile> p_37491_, Level p_37492_) {
        super(p_37491_, p_37492_);
    }

    public SculkPearlProjectile(Level p_37499_, LivingEntity p_37500_) {
        super(BlightEntities.SCULK_PEARL.get(), p_37500_, p_37499_);
    }

    public SculkPearlProjectile(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
        super(BlightEntities.SCULK_PEARL.get(), p_37415_, p_37416_, p_37417_, p_37414_);
    }

    protected Item getDefaultItem() {
        return BlightEntities.SCULK_PEARL_ITEM.get();
    }

    protected void onHitEntity(EntityHitResult p_37502_) {
        super.onHitEntity(p_37502_);
        p_37502_.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(HitResult p_37504_) {
        super.onHit(p_37504_);

        for(int i = 0; i < 32; ++i) {
            this.level.addParticle(ParticleTypes.SCULK_SOUL, this.getX() + this.random.nextDouble() * 0.2 - 0.1, this.getY() + this.random.nextDouble() * 0.2 - 0.1, this.getZ() + this.random.nextDouble() * 0.2 - 0.1, this.random.nextGaussian(), this.random.nextDouble() * 0.1 - 0.05, this.random.nextGaussian());
        }

        if (!this.level.isClientSide && !this.isRemoved()) {
                Entity entity = this.getOwner();
                this.playSound(SoundEvents.SCULK_SHRIEKER_SHRIEK, 2.5F, 1.0F);

                List<Monster> enemiesToRetarget = new ArrayList<Monster>();

                AABB aabb = (new AABB(this.getOnPos())).inflate(20.0D);
                List<LivingEntity> nearbyEntities = this.getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
                for (LivingEntity entity2 : nearbyEntities) {
                    if (entity2 != null && !entity2.isDeadOrDying() && (entity2 instanceof Monster) && (Config.canSeerAttackNonSculk || (entity2 instanceof VibrationListener.VibrationListenerConfig)) && !(entity2 instanceof Seer)) {
                        entity2.addEffect(new MobEffectInstance(BlightEntities.CONFUSION.get(), Config.sculkPearlDuration, 0));
                        ((Monster) entity2).setTarget(null);
                        //enemiesToRetarget.add((Monster) entity2);
                    }
                }

//                for (Monster entity2 : enemiesToRetarget) {
//                    if (Config.sculkPearlInfighting) {
//                        Monster toTarget = enemiesToRetarget.get(entity2.getRandom().nextInt(enemiesToRetarget.size()));
//                        if (entity2.equals(toTarget)) {
//                            entity2.setTarget(null);
//                        } else {
//                            entity2.setTarget(toTarget);
//                        }
//                    } else {
//                        entity2.setTarget(null);
//                    }
//                }

                this.discard();
            }



    }

    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }

    }

    @Nullable
    public Entity changeDimension(ServerLevel p_37506_, net.minecraftforge.common.util.ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != p_37506_.dimension()) {
            this.setOwner((Entity)null);
        }

        return super.changeDimension(p_37506_, teleporter);
    }
}
