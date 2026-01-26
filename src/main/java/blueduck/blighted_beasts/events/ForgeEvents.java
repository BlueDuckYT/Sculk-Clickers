package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.Config;
import blueduck.blighted_beasts.entity.Apparition;
import blueduck.blighted_beasts.entity.Bloater;
import blueduck.blighted_beasts.entity.Reaper;
import blueduck.blighted_beasts.registry.BlightEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = BlightedBeasts.MODID)
public class ForgeEvents {

    @SubscribeEvent
    public static void entityHurtEvent(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof Reaper) {
            event.getSource().getDirectEntity().playSound(SoundEvents.CHAIN_PLACE, 0.4F, -1);
            for (int i = 0; i < 2; ++i) {
                event.getEntity().level().addParticle(ParticleTypes.SCULK_SOUL, 
                        event.getEntity().getRandomX(0.5D), event.getEntity().getRandomY() - 0.25D, event.getEntity().getRandomZ(0.5D), 
                        (event.getEntity().getRandom().nextDouble() - 0.5D) * 0.5D, 
                        -event.getEntity().getRandom().nextDouble() * 0.25 + 0.1, 
                        (event.getEntity().getRandom().nextDouble() - 0.5D) * 0.5D);
            }
        }
    }

    @SubscribeEvent
    public static void entityDieEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Bloater) && event.getEntity().level() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) event.getEntity().level();
            if (Config.bloatersActAsCatalysts) {
                AABB aabb = (new AABB(event.getEntity().getOnPos())).inflate(3.0D);
                List<Bloater> nearbyEntities = event.getEntity().level().getEntitiesOfClass(Bloater.class, aabb);
                for (Bloater bloater : nearbyEntities) {
                    var registry = serverLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
                    var featureKey = ResourceLocation.fromNamespaceAndPath("blighted_beasts", "sculk_patch_bloater");
                    var feature = registry.get(net.minecraft.resources.ResourceKey.create(Registries.CONFIGURED_FEATURE, featureKey));
                    if (feature.isPresent()) {
                        Bloater.placeFeature(serverLevel, feature.get(), bloater.blockPosition());
                    }

                }
            }
            if (Config.apparitionCatalystSpawnChance > 0) {
                if (nearSculkCatalyst(event.getEntity().level(), event.getEntity())) {
                    if (event.getEntity().getRandom().nextDouble() < Config.apparitionCatalystSpawnChance) {
                        Apparition apparition = BlightEntities.SCULK_APPARITION.get().create(event.getEntity().level());
                        if (apparition != null) {
                            apparition.moveTo(event.getEntity().getX(), event.getEntity().getY() - 2.25D, event.getEntity().getZ(), 
                                    event.getEntity().getYRot(), event.getEntity().getXRot());
                            ((ServerLevel) event.getEntity().level()).addFreshEntityWithPassengers(apparition);

                            for (int i = 0; i < 60; i++) {
                                event.getEntity().level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, 
                                        event.getEntity().getRandomX(0.5D), event.getEntity().getRandomY() - 0.25D, event.getEntity().getRandomZ(0.5D), 
                                        (event.getEntity().level().random.nextDouble() - 0.5D) * 0.5D, 
                                        -event.getEntity().level().random.nextDouble() * 0.25 + 0.1, 
                                        (event.getEntity().level().random.nextDouble() - 0.5D) * 0.5D);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityTickEvent(PlayerTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide() && ModList.get().isLoaded("sculksickness")) {
            var sculkSicknessEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.fromNamespaceAndPath("sculksickness", "sculk_sickness"));
            if (sculkSicknessEffect != null && event.getEntity().hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(sculkSicknessEffect))) {
                int amplifier = -1;
                for (MobEffectInstance effect : event.getEntity().getActiveEffects()) {
                    if (effect.getEffect().value().equals(sculkSicknessEffect)) {
                        amplifier = effect.getAmplifier();
                        break;
                    }
                }
                if (event.getEntity().getRandom().nextDouble() < Config.apparitionSicknessSpawnChance * (amplifier + 1)) {
                    int xOffset = event.getEntity().getRandom().nextIntBetweenInclusive(10, 32) * (event.getEntity().getRandom().nextBoolean() ? -1 : 1);
                    int zOffset = event.getEntity().getRandom().nextIntBetweenInclusive(10, 32) * (event.getEntity().getRandom().nextBoolean() ? -1 : 1);
                    int yOffset = event.getEntity().getRandom().nextIntBetweenInclusive(-8, 8);

                    Apparition apparition = BlightEntities.SCULK_APPARITION.get().create(event.getEntity().level());
                    if (apparition != null) {
                        apparition.moveTo(event.getEntity().getX() + xOffset, event.getEntity().getY() + yOffset, event.getEntity().getZ() + zOffset, 
                                event.getEntity().getYRot(), event.getEntity().getXRot());
                        ((ServerLevel) event.getEntity().level()).addFreshEntityWithPassengers(apparition);
                    }
                }
            }
        }
    }

    public static boolean nearSculkCatalyst(Level level, Entity entity) {
        BlockPos position = entity.blockPosition();
        for (int i = -7; i < 8; i++) {
            for (int j = -4; j < 5; j++) {
                for (int k = -7; k < 8; k++) {
                    if (level.getBlockState(position.offset(i, j, k)).is(Blocks.SCULK_CATALYST)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
