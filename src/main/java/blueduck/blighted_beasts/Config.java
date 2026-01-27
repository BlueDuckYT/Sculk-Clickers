package blueduck.blighted_beasts;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = BlightedBeasts.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue CAN_SEER_DETECT_PLAYERS = BUILDER
            .comment("Whether the Seer can aggro onto the player when within range")
            .define("seer_detects", false);
    private static final ModConfigSpec.BooleanValue SEER_ATTACKS_NON_SCULK_MOBS = BUILDER
            .comment("Whether the Seer can affect non-sculk mobs")
            .define("seer_affects_non_sculk", false);

    private static final ModConfigSpec.BooleanValue SCULK_PEARL_CAUSES_INFIGHTING = BUILDER
            .comment("Whether the Sculk Pearl causes infighting between mobs")
            .define("sculk_pearl_infighting", true);

    private static final ModConfigSpec.IntValue SCULK_PEARL_CONFUSION_DURATION = BUILDER
            .comment("How long (in ticks) should the Sculk Pearl's confusion effect last?")
            .defineInRange("sculk_pearl_confusion_duration", 300, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue APPARITION_SPAWN_CHANCE_CATALYST = BUILDER
            .comment("Chance that an Apparition spawns when a mob dies near a Sculk Catalyst")
            .defineInRange("catalyst_apparition_chance", .2, 0, 1);

    private static final ModConfigSpec.DoubleValue APPARITION_SPAWN_CHANCE_BLOATER = BUILDER
            .comment("Chance that an Apparition spawns when a mob dies near a Bloater")
            .defineInRange("bloater_apparition_chance", .1, 0, 1);

    private static final ModConfigSpec.DoubleValue APPARITION_SPAWN_CHANCE_BLOATER_EXPLODE = BUILDER
            .comment("Chance that an Apparition spawns when a bloater explodes")
            .defineInRange("bloater_explode_apparition_chance", .375, 0, 1);

    private static final ModConfigSpec.DoubleValue APPARITION_SPAWN_CHANCE_SICKNESS = BUILDER
            .comment("Chance that an Apparition spawns every tick that the player has the Sculk Sickness (Only used for compatibility, multiplied by stage of sickness)")
            .defineInRange("sickness_apparition_chance", 0.0002, 0, 1);

    private static final ModConfigSpec.DoubleValue APPARITION_HEAL_CHANCE = BUILDER
            .comment("Chance that killing an Apparition reduces Sculk Sickness (Only used for compatibility)")
            .defineInRange("sickness_apparition_heal_chance", 0.1, 0, 1);

    private static final ModConfigSpec.BooleanValue BLOATERS_ATTACK_ALL_MOBS = BUILDER
            .comment("Whether the bloater should attack all mobs or just players")
            .define("bloaters_attack_all_mobs", false);

    private static final ModConfigSpec.BooleanValue BLOATERS_ACT_AS_CATALYSTS = BUILDER
            .comment("Whether the bloater should act as a Sculk Catalyst (Mobs nearby cause sculk spread)")
            .define("bloaters_act_as_catalysts", true);

    private static final ModConfigSpec.BooleanValue SKITTERS_ATTACK_ALL_MOBS = BUILDER
            .comment("Whether the skitter should attack all mobs or just players")
            .define("skitters_attack_all_mobs", true);

    private static final ModConfigSpec.IntValue BECKON_RANGE = BUILDER
            .comment("Aggro range of the Beckon's screech")
            .defineInRange("beckon_range", 12, 0, 200);

    private static final ModConfigSpec.BooleanValue BECKON_DISTRACTS_ONLY_SCULK = BUILDER
            .comment("Whether the Beckon should distract only Sculk mobs (instead of all mobs)")
            .define("beckon_distracts_only_sculk", false);

    private static final ModConfigSpec.BooleanValue BEHEMOTHS_ATTACK_ALL_MOBS = BUILDER
            .comment("Whether the behemoth should attack all mobs or just players")
            .define("behemoth_attack_all_mobs", true);

    private static final ModConfigSpec.BooleanValue BEHEMOTH_BREAKS_BLOCKS = BUILDER
            .comment("Whether the behemoth can break blocks in its path or not")
            .define("behemoth_breaks_blocks", true);

    private static final ModConfigSpec.DoubleValue BEHEMOTH_HARDNESS_THRESHOLD = BUILDER
            .comment("Maximum hardness value the Behemoth can destroy")
            .defineInRange("behemoth_hardness_threshold", 2.0, 0, 10000);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int reaperHealth = 40;
    public static int reaperDamage = 8;
    public static double reaperSpeed = .28;
    public static int reverbHealth = 32;
    public static int reverbDamage = 7;
    public static double reverbSpeed = .3;

    public static double apparitionCatalystSpawnChance;
    public static double apparitionBloaterSpawnChance;
    public static double apparitionBloaterExplodeSpawnChance;

    public static double apparitionSicknessSpawnChance;
    public static double apparitionSicknessHealChance;

    public static int beckonRange;
    public static boolean beckonDistractsSculkOnly;

    public static boolean canSeerDetectPlayers;
    public static boolean canSeerAttackNonSculk;
    public static boolean sculkPearlInfighting;
    public static int sculkPearlDuration;
    public static boolean bloatersAttackAllMobs;
    public static boolean bloatersActAsCatalysts;
    public static boolean skittersAttackAllMobs;

    public static boolean behemothsAttackAllMobs;
    public static boolean behemothBreaksBlocks;
    public static double behemothHardness;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        canSeerDetectPlayers = CAN_SEER_DETECT_PLAYERS.get();
        canSeerAttackNonSculk = SEER_ATTACKS_NON_SCULK_MOBS.get();
        sculkPearlInfighting = SCULK_PEARL_CAUSES_INFIGHTING.get();
        sculkPearlDuration = SCULK_PEARL_CONFUSION_DURATION.get();
        bloatersAttackAllMobs = BLOATERS_ATTACK_ALL_MOBS.get();
        bloatersActAsCatalysts = BLOATERS_ACT_AS_CATALYSTS.get();
        skittersAttackAllMobs = SKITTERS_ATTACK_ALL_MOBS.get();

        apparitionCatalystSpawnChance = APPARITION_SPAWN_CHANCE_CATALYST.get();
        apparitionBloaterSpawnChance = APPARITION_SPAWN_CHANCE_BLOATER.get();
        apparitionBloaterExplodeSpawnChance = APPARITION_SPAWN_CHANCE_BLOATER_EXPLODE.get();

        apparitionSicknessSpawnChance = APPARITION_SPAWN_CHANCE_SICKNESS.get();
        apparitionSicknessHealChance = APPARITION_HEAL_CHANCE.get();

        beckonRange = BECKON_RANGE.get();
        beckonDistractsSculkOnly = BECKON_DISTRACTS_ONLY_SCULK.get();

        behemothsAttackAllMobs = BEHEMOTHS_ATTACK_ALL_MOBS.get();
        behemothBreaksBlocks = BEHEMOTH_BREAKS_BLOCKS.get();
        behemothHardness = BEHEMOTH_HARDNESS_THRESHOLD.get();
    }
}
