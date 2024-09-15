package blueduck.blighted_beasts.effect;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Seer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ConfusionEffect extends MobEffect {
    public ConfusionEffect(MobEffectCategory category) {
        super(category, 1333402);
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity instanceof Monster) {
            Monster entityMonster = (Monster) entity;
            if ((((Monster) entity).getTarget() == null || ((Monster) entity).getTarget() instanceof Player) && Config.sculkPearlInfighting) {
                List<Monster> enemiesToRetarget = new ArrayList<Monster>();
                AABB aabb = (new AABB(entity.getOnPos()).inflate(16.0D));
                List<LivingEntity> nearbyEntities = entity.level.getEntitiesOfClass(LivingEntity.class, aabb);
                for (LivingEntity entity2 : nearbyEntities) {
                    if (entity2 != null && !entity2.isDeadOrDying() && (entity2 instanceof Monster) && (Config.canSeerAttackNonSculk || (entity2 instanceof VibrationListener.VibrationListenerConfig)) && !(entity2 instanceof Seer)) {
                        enemiesToRetarget.add((Monster) entity2);
                    }
                }
                Monster toTarget = enemiesToRetarget.size() > 0 ? enemiesToRetarget.get(entity.getRandom().nextInt(enemiesToRetarget.size())) : null;
                if (entity.equals(toTarget)) {
                    entityMonster.setTarget(null);
                } else {
                    entityMonster.setTarget(toTarget);
                }
            }
            else if (!Config.sculkPearlInfighting) {
                entityMonster.setTarget(null);
            }
        }
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int k = 10 >> p_19456_;
        if (k > 0) {
            return p_19455_ % k == 0;
        } else {
            return true;
        }
    }


}
