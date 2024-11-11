package blueduck.blighted_beasts;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

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

      private static final ForgeConfigSpec.BooleanValue CAN_SEER_DETECT_PLAYERS = BUILDER
            .comment("Whether the Seer can aggro onto the player when within range")
            .define("seer_detects", false);
    private static final ForgeConfigSpec.BooleanValue SEER_ATTACKS_NON_SCULK_MOBS = BUILDER
            .comment("Whether the Seer can affect non-sculk mobs")
            .define("seer_affects_non_sculk", false);

    private static final ForgeConfigSpec.BooleanValue SCULK_PEARL_CAUSES_INFIGHTING = BUILDER
            .comment("Whether the Sculk Pearl causes infighting between mobs")
            .define("sculk_pearl_infighting", true);

        private static final ForgeConfigSpec.IntValue SCULK_PEARL_CONFUSION_DURATION = BUILDER
            .comment("How long (in ticks) should the Sculk Pearl's confusion effect last?")
            .defineInRange("sculk_pearl_confusion_duration", 300, 0, Integer.MAX_VALUE);


    private static final ForgeConfigSpec.BooleanValue BLOATERS_ATTACK_ALL_MOBS = BUILDER
            .comment("Whether the bloater should attack all mobs or just players")
            .define("bloaters_attack_all_mobs", false);

    private static final ForgeConfigSpec.BooleanValue SKITTERS_ATTACK_ALL_MOBS = BUILDER
            .comment("Whether the skitter should attack all mobs or just players")
            .define("skitters_attack_all_mobs", true);



    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int reaperHealth = 40;
    public static int reaperDamage = 8;
    public static double reaperSpeed = .28;
    public static int reverbHealth = 32;
    public static int reverbDamage = 7;
    public static double reverbSpeed = .3;

    public static boolean canSeerDetectPlayers;
    public static boolean canSeerAttackNonSculk;
    public static boolean sculkPearlInfighting;
    public static int sculkPearlDuration;
    public static boolean bloatersAttackAllMobs;
    public static boolean skittersAttackAllMobs;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
//        reaperHealth = REAPER_HEALTH.get();
//        reaperDamage = REAPER_DAMAGE.get();
//        reaperSpeed = REAPER_SPEED.get();
//        reverbHealth = REVERB_HEALTH.get();
//        reverbDamage = REVERB_DAMAGE.get();
//        reverbSpeed = REVERB_SPEED.get();

        canSeerDetectPlayers = CAN_SEER_DETECT_PLAYERS.get();
        canSeerAttackNonSculk = SEER_ATTACKS_NON_SCULK_MOBS.get();
        sculkPearlInfighting = SCULK_PEARL_CAUSES_INFIGHTING.get();
        sculkPearlDuration = SCULK_PEARL_CONFUSION_DURATION.get();
        bloatersAttackAllMobs = BLOATERS_ATTACK_ALL_MOBS.get();
        skittersAttackAllMobs = SKITTERS_ATTACK_ALL_MOBS.get();
    }
}
