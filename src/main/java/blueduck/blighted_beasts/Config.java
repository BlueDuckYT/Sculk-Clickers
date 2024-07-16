package blueduck.blighted_beasts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = BlightedBeasts.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

//    private static final ForgeConfigSpec.IntValue REAPER_HEALTH = BUILDER
//            .comment("How much health the Reaper spawns with")
//            .defineInRange("reaper_health", 40, 0, Integer.MAX_VALUE);
//    private static final ForgeConfigSpec.IntValue REAPER_DAMAGE = BUILDER
//            .comment("How much damage the Reaper deals")
//            .defineInRange("reaper_damage", 8, 0, Integer.MAX_VALUE);
//    private static final ForgeConfigSpec.DoubleValue REAPER_SPEED = BUILDER
//            .comment("The movement speed of the Reaper")
//            .defineInRange("reaper_speed", .28, 0, Double.MAX_VALUE);
//    private static final ForgeConfigSpec.IntValue REVERB_HEALTH = BUILDER
//            .comment("How much health the Reverb spawns with")
//            .defineInRange("reverb_health", 32, 0, Integer.MAX_VALUE);
//    private static final ForgeConfigSpec.IntValue REVERB_DAMAGE = BUILDER
//            .comment("How much damage the Reverb deals")
//            .defineInRange("reverb_damage", 7, 0, Integer.MAX_VALUE);
//    private static final ForgeConfigSpec.DoubleValue REVERB_SPEED = BUILDER
//            .comment("The movement speed of the Reverb")
//            .defineInRange("reverb_speed", .3, 0, Double.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int reaperHealth = 40;
    public static int reaperDamage = 8;
    public static double reaperSpeed = .28;
    public static int reverbHealth = 32;
    public static int reverbDamage = 7;
    public static double reverbSpeed = .3;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
//        reaperHealth = REAPER_HEALTH.get();
//        reaperDamage = REAPER_DAMAGE.get();
//        reaperSpeed = REAPER_SPEED.get();
//        reverbHealth = REVERB_HEALTH.get();
//        reverbDamage = REVERB_DAMAGE.get();
//        reverbSpeed = REVERB_SPEED.get();
    }
}
