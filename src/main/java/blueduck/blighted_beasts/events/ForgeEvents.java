package blueduck.blighted_beasts.events;

import blueduck.blighted_beasts.BlightedBeasts;
import blueduck.blighted_beasts.entity.Bloater;
import blueduck.blighted_beasts.entity.Reaper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
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
    public static void entityDieEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Bloater)) {
            AABB aabb = (new AABB(event.getEntity().getOnPos())).inflate(3.0D);
            List<Bloater> nearbyEntities = event.getEntity().getLevel().getEntitiesOfClass(Bloater.class, aabb);
            for (Bloater bloater : nearbyEntities) {
                bloater.placeFeature(bloater.getServer().getLevel(bloater.getLevel().dimension()), Holder.direct(bloater.getLevel().registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(new ResourceLocation("blighted_beasts", "sculk_patch_bloater_catalyst"))), bloater.blockPosition());
            }
        }

    }


}
