package blueduck.blighted_beasts.effect;

import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Seer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class ConfusionEffect extends MobEffect {
    public ConfusionEffect(MobEffectCategory category) {
        super(category, 1333402);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Monster entityMonster) {
            if ((entityMonster.getTarget() == null || entityMonster.getTarget() instanceof Player) && Config.sculkPearlInfighting) {
                List<Monster> enemiesToRetarget = new ArrayList<>();
                AABB aabb = (new AABB(entity.getOnPos()).inflate(16.0D));
                List<LivingEntity> nearbyEntities = entity.level().getEntitiesOfClass(LivingEntity.class, aabb);
                for (LivingEntity entity2 : nearbyEntities) {
                    if (entity2 != null && !entity2.isDeadOrDying() && (entity2 instanceof Monster) && (Config.canSeerAttackNonSculk || (entity2 instanceof VibrationSystem)) && !(entity2 instanceof Seer)) {
                        enemiesToRetarget.add((Monster) entity2);
                    }
                }
                Monster toTarget = !enemiesToRetarget.isEmpty() ? enemiesToRetarget.get(entity.getRandom().nextInt(enemiesToRetarget.size())) : null;
                if (entity.equals(toTarget)) {
                    entityMonster.setTarget(null);
                } else {
                    entityMonster.setTarget(toTarget);
                }
            } else if (!Config.sculkPearlInfighting) {
                entityMonster.setTarget(null);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int k = 10 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }
}
