package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.*;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid= BlightedBeasts.MODID)
public class ForgeEvents {



    @SubscribeEvent
    public static void entityHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Reaper) {
            event.getSource().getDirectEntity().playSound(SoundEvents.CHAIN_PLACE, 0.4F, -1);
            for(int i = 0; i < 2; ++i) {
                event.getEntity().level.addParticle(ParticleTypes.SCULK_SOUL, event.getEntity().getRandomX(0.5D), event.getEntity().getRandomY() - 0.25D, event.getEntity().getRandomZ(0.5D), (event.getEntity().getRandom().nextDouble() - 0.5D) * .5D, -event.getEntity().getRandom().nextDouble() * 0.25 + .1, (event.getEntity().getRandom().nextDouble() - 0.5D) * .5D);
            }
        }
    }

    @SubscribeEvent
    public static void entityAttackEvent(LivingAttackEvent event) {
        if (event.getSource().getDirectEntity() instanceof Skitter) {
            ((Skitter) event.getSource().getDirectEntity()).swing(InteractionHand.MAIN_HAND);
            if (event.getEntity().isDeadOrDying()) {
                ((Skitter) event.getSource().getDirectEntity()).isTargeting = false;
            }
        }
        if (event.getSource().getDirectEntity() instanceof Apparition) {
            ((Apparition) event.getSource().getDirectEntity()).swing(InteractionHand.MAIN_HAND);
            ((Apparition) event.getSource().getDirectEntity()).swingTime = -1;
        }
        if (event.getSource().getDirectEntity() instanceof Behemoth) {
            ((Behemoth) event.getSource().getDirectEntity()).swing(InteractionHand.MAIN_HAND);
            ((Behemoth) event.getSource().getDirectEntity()).swingTime = -1;
            ((Behemoth) event.getSource().getDirectEntity()).animState = 1;
            if (event.getEntity().isDeadOrDying()) {
                ((Behemoth) event.getSource().getDirectEntity()).isTargeting = false;
            }

        }
    }

    @SubscribeEvent
    public static void entityDieEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Bloater) && !(event.getEntity() instanceof Apparition)) {
            AABB aabb = (new AABB(event.getEntity().getOnPos())).inflate(3.0D);
            List<Bloater> nearbyEntities = event.getEntity().getLevel().getEntitiesOfClass(Bloater.class, aabb);
            for (Bloater bloater : nearbyEntities) {
                if (!bloater.level.getBlockState(bloater.blockPosition()).getBlock().isPossibleToRespawnInThis()) {
                    bloater.level.setBlock(bloater.blockPosition(), Blocks.AIR.defaultBlockState(), 2);
                }
                bloater.placeFeature(bloater.getServer().getLevel(bloater.getLevel().dimension()), Holder.direct(bloater.getLevel().registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(new ResourceLocation("blighted_beasts", "sculk_patch_bloater_catalyst"))), bloater.blockPosition());
            }
            if (nearbyEntities.size() > 0) {
                if (event.getEntity().getRandom().nextDouble() < Config.apparitionBloaterSpawnChance) {
                    Apparition apparition = BlightEntities.SCULK_APPARITION.get().create(event.getEntity().level);
                    apparition.moveTo(event.getEntity().getX(), event.getEntity().getY() - 2.25D, event.getEntity().getZ(), event.getEntity().getYRot(), event.getEntity().getXRot());
                    ((ServerLevel)event.getEntity().level).addFreshEntityWithPassengers(apparition);

                    for (int i = 0; i < 60; i++) {
                        event.getEntity().level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, event.getEntity().getRandomX(0.5D), event.getEntity().getRandomY() - 0.25D, event.getEntity().getRandomZ(0.5D), (event.getEntity().level.random.nextDouble() - 0.5D) * .5D, -event.getEntity().level.random.nextDouble() * 0.25 + .1, (event.getEntity().level.random.nextDouble() - 0.5D) * .5D);

                    }
                }
            }
            if (nearSculkCatalyst(event.getEntity().level, event.getEntity())) {
                if (event.getEntity().getRandom().nextDouble() < Config.apparitionCatalystSpawnChance) {
                    Apparition apparition = BlightEntities.SCULK_APPARITION.get().create(event.getEntity().level);
                    apparition.moveTo(event.getEntity().getX(), event.getEntity().getY() - 2.25D, event.getEntity().getZ(), event.getEntity().getYRot(), event.getEntity().getXRot());
                    ((ServerLevel)event.getEntity().level).addFreshEntityWithPassengers(apparition);

                    for (int i = 0; i < 60; i++) {
                        event.getEntity().level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, event.getEntity().getRandomX(0.5D), event.getEntity().getRandomY() - 0.25D, event.getEntity().getRandomZ(0.5D), (event.getEntity().level.random.nextDouble() - 0.5D) * .5D, -event.getEntity().level.random.nextDouble() * 0.25 + .1, (event.getEntity().level.random.nextDouble() - 0.5D) * .5D);

                    }
                }
            }
        }
        if ((event.getEntity() instanceof Apparition) && ModList.get().isLoaded("sculksickness")) {
            Entity entity = event.getSource().getEntity();
            if (entity instanceof Player && ((Player) entity).hasEffect(Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness"))) && ((Player) entity).getRandom().nextDouble() < Config.apparitionSicknessHealChance) {
                MobEffect SCULK_SICKNESS = Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness"));
                for (int i = 0; i < ((Player) entity).getActiveEffects().size(); i++) {
                    if (((MobEffectInstance)(((Player) entity).getActiveEffects().toArray()[i])).getEffect().equals(Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness")))) {
                        MobEffectInstance instance = ((MobEffectInstance)(((Player) entity).getActiveEffects().toArray()[i]));
                        int amplifier = instance.getAmplifier();
                        int duration = instance.getDuration();
                        boolean visible = instance.isVisible();
                        ((Player) entity).removeEffect(Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness")));
                        if (amplifier > 0) {
                            ((Player) entity).addEffect(new MobEffectInstance(SCULK_SICKNESS, duration, amplifier - 1, false, visible));
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void entityTickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide && ModList.get().isLoaded("sculksickness") && event.player.hasEffect(Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness")))) {
            int amplifier = -1;
            for (int i = 0; i < event.player.getActiveEffects().size(); i++) {
                if (((MobEffectInstance) event.player.getActiveEffects().toArray()[i]).getEffect().equals(Registry.MOB_EFFECT.get(new ResourceLocation("sculksickness:sculk_sickness")))) {
                    MobEffectInstance instance = ((MobEffectInstance)(event.player.getActiveEffects().toArray()[i]));
                    amplifier = instance.getAmplifier();
                }
            }
            if (event.player.getRandom().nextDouble() < Config.apparitionSicknessSpawnChance * (amplifier + 1)) {
                int xOffset = event.player.getRandom().nextIntBetweenInclusive(10, 32) * (event.player.getRandom().nextInt() == 0 ? -1 : 1);
                int zOffset = event.player.getRandom().nextIntBetweenInclusive(10, 32) * (event.player.getRandom().nextInt() == 0 ? -1 : 1);
                int yOffset = event.player.getRandom().nextIntBetweenInclusive(-8, 8);

                Apparition apparition = BlightEntities.SCULK_APPARITION.get().create(event.player.level);
                apparition.moveTo(event.player.getX() + xOffset, event.player.getY() + yOffset, event.player.getZ() + zOffset, event.player.getYRot(), event.player.getXRot());
                ((ServerLevel)event.player.level).addFreshEntityWithPassengers(apparition);
            }

        }
    }

    public static boolean nearSculkCatalyst(Level level, Entity entity) {
        BlockPos position = entity.blockPosition();
        for (int i = -7; i < 8; i++) {
            for (int j = -4; j < 5; j++) {
                for (int k = -7; k < 8; k++) {
                    if (level.getBlockState(position.offset(i,j,k)).is(Blocks.SCULK_CATALYST)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
