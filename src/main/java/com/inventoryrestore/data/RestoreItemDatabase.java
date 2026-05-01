package com.inventoryrestore.data;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Maps RuneLite item IDs to their {@link RestoreItem} descriptor.
 *
 * <p>All item IDs are sourced from the OSRS wiki and RuneLite's ItemID constants.
 *
 * <p>Entries are added for every dose/slice/half variant of multi-use items so the
 * overlay always shows the correct per-bite value regardless of how many uses remain.
 */
public final class RestoreItemDatabase
{
	private static final Map<Integer, RestoreItem> DATABASE = new HashMap<>();

	/**
	 * Maps partial/half item IDs to the canonical full-item ID for the same food type.
	 * Used so that first-item-only mode treats e.g. "half botanical pie" and
	 * "botanical pie (full)" as the same item.
	 */
	private static final Map<Integer, Integer> GROUP_IDS = new HashMap<>();

	static
	{
		// ==================================================================
		// PRAYER-RESTORING POTIONS
		// ==================================================================

		// Prayer potion — floor(prayer * 25/100) + 7, or floor(prayer * 27/100) + 7 with wrench
		put(2434, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION));  // Prayer potion(4)
		put(139,  RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION));  // Prayer potion(3)
		put(141,  RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION));  // Prayer potion(2)
		put(143,  RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION));  // Prayer potion(1)

		// Super restore — floor(prayer * 25/100) + 8, or floor(prayer * 27/100) + 8 with wrench
		put(3024, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Super restore(4)
		put(3026, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Super restore(3)
		put(3028, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Super restore(2)
		put(3030, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Super restore(1)

		// Sanfew serum — floor(prayer * 30/100) + 4, or floor(prayer * 32/100) + 4 with wrench
		put(10925, RestoreItem.prayer(PrayerRestoreType.SANFEW_SERUM)); // Sanfew serum(4)
		put(10927, RestoreItem.prayer(PrayerRestoreType.SANFEW_SERUM)); // Sanfew serum(3)
		put(10929, RestoreItem.prayer(PrayerRestoreType.SANFEW_SERUM)); // Sanfew serum(2)
		put(10931, RestoreItem.prayer(PrayerRestoreType.SANFEW_SERUM)); // Sanfew serum(1)

		// Ancient brew — floor(prayer / 10) + 2  (NOT affected by Holy Wrench)
		put(26340, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Ancient brew(4)
		put(26342, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Ancient brew(3)
		put(26344, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Ancient brew(2)
		put(26346, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Ancient brew(1)

		// Forgotten brew — identical formula to Ancient brew, NOT affected by wrench
		put(27629, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Forgotten brew(4)
		put(27632, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Forgotten brew(3)
		put(27635, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Forgotten brew(2)
		put(27638, RestoreItem.prayer(PrayerRestoreType.ANCIENT_BREW)); // Forgotten brew(1)

		// Blighted super restore — same formula as Super restore
		put(24598, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Blighted super restore(4)
		put(24601, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Blighted super restore(3)
		put(24603, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Blighted super restore(2)
		put(24605, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Blighted super restore(1)

		// Prayer regeneration potion — 1 prayer every 12 ticks over ~8 min (66 total per dose)
		// Overlay shows "1/12t". Infobox tracks countdown to next tick.
		put(30125, RestoreItem.prayerRegen()); // Prayer regeneration potion(4)
		put(30128, RestoreItem.prayerRegen()); // Prayer regeneration potion(3)
		put(30131, RestoreItem.prayerRegen()); // Prayer regeneration potion(2)
		put(30134, RestoreItem.prayerRegen()); // Prayer regeneration potion(1)

		// ==================================================================
		// BARBARIAN HERBLORE MIXES (HP + prayer restore)
		// Made via Barbarian Training with Otto Godblessed. Each dose heals
		// 6 HP in addition to the standard potion effect.
		// In-game names: "Prayer mix(2)", "Super restore mix(2)", etc.
		// ==================================================================

		// Prayer mix — HP 6 + Prayer Potion prayer formula
		put(11465, RestoreItem.comboItem(6, PrayerRestoreType.PRAYER_POTION)); // Prayer mix(2)
		put(11467, RestoreItem.comboItem(6, PrayerRestoreType.PRAYER_POTION)); // Prayer mix(1)

		// Super restore mix — HP 6 + Super Restore prayer formula
		put(11493, RestoreItem.comboItem(6, PrayerRestoreType.SUPER_RESTORE)); // Super restore mix(2)
		put(11495, RestoreItem.comboItem(6, PrayerRestoreType.SUPER_RESTORE)); // Super restore mix(1)

		// Zamorak mix — HP 6 only (the Zamorak Brew effect drains prayer, not restores it)
		put(11521, RestoreItem.food(6)); // Zamorak mix(2)
		put(11523, RestoreItem.food(6)); // Zamorak mix(1)

		// ==================================================================
		// DYNAMIC HP FOOD
		// ==================================================================

		// Saradomin brew — floor(hp * 15/100) + 2 per dose (restores HP only, not prayer)
		put(6685, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(4)
		put(6687, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(3)
		put(6689, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(2)
		put(6691, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(1)

		// Anglerfish — floor(hp/10) + 2*floor(hp/25) + 5*floor(hp/93) + 2  (can overheal)
		put(13441, RestoreItem.dynamicFood(DynamicHpType.ANGLERFISH)); // Anglerfish

		// ==================================================================
		// HUNTER MEATS — two-part healing, 7-tick delay between heals
		// Eating a second item before the delay fires cancels the first delayed heal.
		// Overlay shows "instant/delayed" (e.g. "4/4").
		// ==================================================================

		put(29128, RestoreItem.twoPartFood(4,  4,  7)); // Cooked wild kebbit
		put(29131, RestoreItem.twoPartFood(7,  5,  7)); // Cooked barb-tailed kebbit
		put(29134, RestoreItem.twoPartFood(13, 10, 7)); // Cooked dashing kebbit   (+10 run energy on delay)
		put(29137, RestoreItem.twoPartFood(11, 8,  7)); // Cooked pyre fox
		put(29140, RestoreItem.twoPartFood(12, 9,  7)); // Cooked sunlight antelope
		put(29143, RestoreItem.twoPartFood(14, 12, 7)); // Cooked moonlight antelope (also cures poison on delay)
		put(29146, RestoreItem.twoPartFood(6,  5,  7)); // Cooked larupia
		put(29149, RestoreItem.twoPartFood(8,  6,  7)); // Cooked graahk
		put(29152, RestoreItem.twoPartFood(9,  8,  7)); // Cooked kyatt

		// ==================================================================
		// FISH (cooked) — standard
		// ==================================================================

		put(319,   RestoreItem.food(1));  // Anchovies
		put(315,   RestoreItem.food(3));  // Shrimps
		put(11324, RestoreItem.food(3));  // Roe (from barbarian fishing)
		put(325,   RestoreItem.food(4));  // Sardine
		put(347,   RestoreItem.food(5));  // Herring
		put(11326, RestoreItem.food(5));  // Caviar (from barbarian fishing)
		put(355,   RestoreItem.food(6));  // Mackerel
		put(337,   RestoreItem.food(6));  // Giant carp
		put(333,   RestoreItem.food(7));  // Trout
		put(341,   RestoreItem.food(7));  // Cod
		put(351,   RestoreItem.food(8));  // Pike
		put(329,   RestoreItem.food(9));  // Salmon
		put(361,   RestoreItem.food(10)); // Tuna
		put(379,   RestoreItem.food(12)); // Lobster
		put(365,   RestoreItem.food(13)); // Bass
		put(373,   RestoreItem.food(14)); // Swordfish
		put(7946,  RestoreItem.food(16)); // Monkfish
		put(3144,  RestoreItem.food(18)); // Cooked karambwan (can be combo-eaten)
		put(385,   RestoreItem.food(20)); // Shark
		put(397,   RestoreItem.food(21)); // Sea turtle
		put(391,   RestoreItem.food(22)); // Manta ray
		put(11936, RestoreItem.food(22)); // Dark crab

		// Variable-heal fish — using typical/representative values
		put(5003,  RestoreItem.food(10)); // Cave eel       (heals 8–12; shown as representative 10)
		put(3381,  RestoreItem.food(8));  // Cooked slime eel  (heals 6–10; representative 8)

		// ==================================================================
		// FISH — Varlamore (from Varlamore content update)
		// ==================================================================

		put(32312, RestoreItem.food(17)); // Giant krill
		put(31564, RestoreItem.food(17)); // Jumbo squid
		put(31556, RestoreItem.food(15)); // Swordtip squid
		put(32320, RestoreItem.dynamicFood(DynamicHpType.HADDOCK)); // Haddock (min(18, floor(hp*20%)))
		put(32328, RestoreItem.food(19)); // Yellowfin
		put(32336, RestoreItem.food(20)); // Halibut
		put(32344, RestoreItem.foodWithFlatPrayer(22, 5)); // Bluefin (22 HP + 5 prayer)
		put(32352, RestoreItem.food(24)); // Marlin

		// ==================================================================
		// FISH — Morytania & wilderness
		// ==================================================================

		put(2149,  RestoreItem.food(11)); // Lava eel (Taverley Dungeon / lava slabs)
		put(5004,  RestoreItem.food(4));  // Giant frogspawn (heals 3–6; representative 4)

		// ==================================================================
		// BLIGHTED FOOD — Wilderness-only items from Bounty Hunter shop
		// Identical heal values to their regular counterparts.
		// ==================================================================

		put(24589, RestoreItem.food(22));                               // Blighted manta ray
		put(24592, RestoreItem.dynamicFood(DynamicHpType.ANGLERFISH));  // Blighted anglerfish
		put(24595, RestoreItem.food(18));                               // Blighted karambwan

		// ==================================================================
		// FISH — Tai Bwo Wannai & special
		// ==================================================================

		// Roasted snails — heal varies by snail type; representative values used
		put(3369,  RestoreItem.food(6));  // Roasted snail (small, heals 5–7)
		put(3371,  RestoreItem.food(6));  // Roasted snail (medium, heals 5–8)
		put(3373,  RestoreItem.food(8));  // Roasted snail (large, heals 7–9)

		// Spiders-on-a-stick — heals 7–10; representative value used
		put(6297,  RestoreItem.food(8));  // Spider on stick (cooked, heals 7–10)
		put(6299,  RestoreItem.food(8));  // Spider on shaft (cooked, heals 7–10)

		// ==================================================================
		// FISH — Dorgesh-Kaan cave delicacies
		// ==================================================================

		put(10960, RestoreItem.food(2));  // Green gloop soup
		put(10961, RestoreItem.food(2));  // Frogspawn gumbo
		put(10962, RestoreItem.food(2));  // Frog burger
		put(10963, RestoreItem.food(2));  // Crispy frogs' legs
		put(10964, RestoreItem.food(2));  // Bat shish
		put(10965, RestoreItem.food(2));  // Wall beast fingers
		put(10966, RestoreItem.food(2));  // Grubs à la mode
		put(10967, RestoreItem.food(2));  // Whole roasted frog
		put(10968, RestoreItem.food(2));  // Sautéed mushrooms
		put(10969, RestoreItem.food(2));  // Cave crawler fillets
		put(10970, RestoreItem.food(3));  // Steamed pond loach
		put(10971, RestoreItem.food(10)); // Cave eel sushi

		// ==================================================================
		// MEAT, BREAD & BASICS
		// ==================================================================

		put(2140, RestoreItem.food(3));  // Cooked chicken
		put(2142, RestoreItem.food(3));  // Cooked meat
		put(3228, RestoreItem.food(5));  // Cooked rabbit
		put(9052, RestoreItem.food(3));  // Locust meat     (Desert Treasure II)
		put(1861, RestoreItem.food(3));  // Cooked ugthanki meat
		put(2309, RestoreItem.food(5));  // Bread
		put(1993, RestoreItem.food(11)); // Jug of wine   (also lowers Attack; we show the heal only)
		put(1989, RestoreItem.food(7));  // Half-full wine jug
		put(2011, RestoreItem.food(19)); // Curry         (also restores 1 prayer — negligible, skipped)
		put(1883, RestoreItem.food(19)); // Ugthanki kebab
		put(6883, RestoreItem.food(8));  // Peach
		put(2878, RestoreItem.food(15)); // Cooked chompy
		put(2343, RestoreItem.food(14)); // Cooked oomlie wrap
		put(7568, RestoreItem.food(15)); // Cooked jubbly meat (Big Chompy Bird Hunting)

		// ==================================================================
		// FRUIT & VEGETABLES
		// ==================================================================

		put(1942, RestoreItem.food(1));  // Potato
		put(1957, RestoreItem.food(1));  // Onion
		put(1965, RestoreItem.food(1));  // Cabbage
		put(1959, RestoreItem.food(14)); // Pumpkin (Hallowe'en item)
		put(1961, RestoreItem.food(14)); // Easter egg (Easter item)
		put(1982, RestoreItem.food(2));  // Tomato
		put(1963, RestoreItem.food(2));  // Banana
		put(1969, RestoreItem.food(2));  // Spinach roll
		put(1973, RestoreItem.food(3));  // Chocolate bar
		put(1985, RestoreItem.food(2));  // Cheese
		put(5972, RestoreItem.food(8));  // Papaya fruit
		put(22929, RestoreItem.food(10)); // Dragonfruit
		put(247,  RestoreItem.foodWithFlatPrayer(2, 1)); // Jangerberries (2 HP + 1 prayer)

		// Citrus fruits — each segment form heals 2 HP
		put(2102, RestoreItem.food(2));  // Lemon
		put(2104, RestoreItem.food(2));  // Lemon chunks
		put(2106, RestoreItem.food(2));  // Lemon slices
		put(2108, RestoreItem.food(2));  // Orange
		put(2110, RestoreItem.food(2));  // Orange chunks
		put(2112, RestoreItem.food(2));  // Orange slices
		put(2120, RestoreItem.food(2));  // Lime
		put(2122, RestoreItem.food(2));  // Lime chunks
		put(2124, RestoreItem.food(2));  // Lime slices

		// Pineapple
		put(2118, RestoreItem.food(2));  // Pineapple ring
		put(2116, RestoreItem.food(2));  // Pineapple chunks

		// Gnome cooking misc
		put(2126, RestoreItem.food(2));  // Dwellberries
		put(2128, RestoreItem.food(1));  // Equa leaves
		put(2130, RestoreItem.food(1));  // Pot of cream
		put(2152, RestoreItem.food(3));  // Toads' legs
		put(2162, RestoreItem.food(2));  // King worm (fishing bait that can be eaten)
		put(4485, RestoreItem.food(2));  // White pearl fruit (Fairy Tale quest)

		// ==================================================================
		// BAKED POTATOES & FILLINGS
		// ==================================================================

		put(6701, RestoreItem.food(4));  // Baked potato (plain)
		put(6703, RestoreItem.food(14)); // Potato with butter
		put(6705, RestoreItem.food(16)); // Potato with cheese
		put(7054, RestoreItem.food(14)); // Potato with chilli carne
		put(7056, RestoreItem.food(16)); // Potato with egg and tomato
		put(7058, RestoreItem.food(20)); // Potato with mushroom and onion
		put(7060, RestoreItem.food(22)); // Tuna potato

		// ==================================================================
		// CAKES  (4 HP per slice for regular cake, 5 HP for chocolate cake)
		// ==================================================================

		put(1891, RestoreItem.food(4)); // Cake        (full — 3 slices)
		put(1893, RestoreItem.food(4)); // Cake        (2/3)
		put(1895, RestoreItem.food(4)); // Cake        (1/3)
		put(1897, RestoreItem.food(5)); // Chocolate cake (full)
		put(1899, RestoreItem.food(5)); // Chocolate slice (2/3)
		put(1901, RestoreItem.food(5)); // Chocolate slice (1/3)

		// ==================================================================
		// PIZZAS  (each half heals the same amount)
		// ==================================================================

		put(2289, RestoreItem.food(7));  // Plain pizza       (full)
		put(2291, RestoreItem.food(7));  // Plain pizza       (half)
		put(2293, RestoreItem.food(8));  // Meat pizza        (full)
		put(2295, RestoreItem.food(8));  // Meat pizza        (half)
		put(2297, RestoreItem.food(9));  // Anchovy pizza     (full)
		put(2299, RestoreItem.food(9));  // Anchovy pizza     (half)
		put(2301, RestoreItem.food(11)); // Pineapple pizza   (full)
		put(2303, RestoreItem.food(11)); // Pineapple pizza   (half)

		// ==================================================================
		// PIES  (eaten in two halves, both halves heal the same)
		// ==================================================================

		put(2325, RestoreItem.food(5));  // Redberry pie       (full)
		put(2333, RestoreItem.food(5));  // Half redberry pie
		put(2327, RestoreItem.food(6));  // Meat pie           (full)
		put(2315, RestoreItem.food(6));  // Half meat pie
		put(2335, RestoreItem.food(7));  // Apple pie          (full)
		put(2317, RestoreItem.food(7));  // Half apple pie
		put(7178, RestoreItem.food(6));  // Garden pie         (full, also +3 Farming boost)
		put(7180, RestoreItem.food(6));  // Half garden pie
		put(7188, RestoreItem.food(6));  // Fish pie           (full, also +3 Fishing boost)
		put(7190, RestoreItem.food(6));  // Half fish pie
		put(7198, RestoreItem.food(8));  // Admiral pie        (full, also +5 Slayer/Fishing boost)
		put(7200, RestoreItem.food(8));  // Half admiral pie
		put(7208, RestoreItem.food(11)); // Wild pie           (full, also +5 Slayer/+4 Ranged boost)
		put(7210, RestoreItem.food(11)); // Half wild pie
		put(7218, RestoreItem.food(11)); // Summer pie         (full, also +5 Agility boost)
		put(7220, RestoreItem.food(11)); // Half summer pie
		put(21687, RestoreItem.food(8)); // Mushroom pie       (full, also +4 Slayer boost)
		put(21690, RestoreItem.food(8)); // Half mushroom pie
		put(22794, RestoreItem.food(8)); // Botanical pie      (full, also +4 Herblore boost)
		put(22795, RestoreItem.food(8)); // Half botanical pie
		put(22791, RestoreItem.food(10)); // Dragonfruit pie   (full, also +4 Fletching boost)
		put(22793, RestoreItem.food(10)); // Half dragonfruit pie

		// ==================================================================
		// GNOME COOKING — crunchies, battas, bowls, cocktails
		// ==================================================================

		// Crunchies (7–8 HP)
		put(2205, RestoreItem.food(8));  // Worm crunchies
		put(2237, RestoreItem.food(8));  // Premade worm crunchies
		put(2209, RestoreItem.food(7));  // Chocchip crunchies
		put(2239, RestoreItem.food(7));  // Premade chocchip crunchies
		put(2213, RestoreItem.food(7));  // Spicy crunchies
		put(2241, RestoreItem.food(7));  // Premade spicy crunchies
		put(2217, RestoreItem.food(8));  // Toad crunchies
		put(2243, RestoreItem.food(8));  // Premade toad crunchies

		// Battas (11 HP)
		put(2253, RestoreItem.food(11)); // Worm batta
		put(2219, RestoreItem.food(11)); // Premade worm batta
		put(2255, RestoreItem.food(11)); // Toad batta
		put(2221, RestoreItem.food(11)); // Premade toad batta
		put(2259, RestoreItem.food(11)); // Cheese+tomato batta
		put(2223, RestoreItem.food(11)); // Premade cheese+tomato batta
		put(2277, RestoreItem.food(11)); // Fruit batta
		put(2225, RestoreItem.food(11)); // Premade fruit batta
		put(2281, RestoreItem.food(11)); // Vegetable batta
		put(2227, RestoreItem.food(11)); // Premade vegetable batta

		// Bowls (5–13 HP, gnome cooking results)
		put(2191, RestoreItem.food(12)); // Worm hole
		put(2233, RestoreItem.food(12)); // Premade worm hole
		put(2195, RestoreItem.food(12)); // Veg ball
		put(2235, RestoreItem.food(12)); // Premade veg ball
		put(2187, RestoreItem.food(15)); // Tangled toad's legs
		put(2231, RestoreItem.food(15)); // Premade tangled toad's legs
		put(2185, RestoreItem.food(15)); // Chocolate bomb
		put(2229, RestoreItem.food(15)); // Premade chocolate bomb

		// Gnome cooking bowls (Gnome Stronghold restaurant)
		put(7062, RestoreItem.food(5));  // Bowl of chilli carne
		put(7064, RestoreItem.food(8));  // Bowl of egg and tomato
		put(7066, RestoreItem.food(11)); // Bowl of mushroom and onion
		put(7068, RestoreItem.food(13)); // Bowl of tuna and sweetcorn
		put(7078, RestoreItem.food(5));  // Bowl of scrambled eggs
		put(7082, RestoreItem.food(5));  // Bowl of fried mushrooms
		put(7084, RestoreItem.food(5));  // Bowl of fried onions
		put(7086, RestoreItem.food(10)); // Bowl of tuna

		// Cocktails (5–9 HP) — served at Gnome Stronghold bar
		put(2064, RestoreItem.food(7));  // Blurberry special
		put(2028, RestoreItem.food(7));  // Premade blurberry special
		put(2092, RestoreItem.food(5));  // Drunk dragon
		put(2032, RestoreItem.food(5));  // Premade drunk dragon
		put(2074, RestoreItem.food(5));  // Chocolate saturday
		put(2030, RestoreItem.food(5));  // Premade chocolate saturday
		put(2054, RestoreItem.food(5));  // Wizard blizzard
		put(2040, RestoreItem.food(5));  // Premade wizard blizzard
		put(2080, RestoreItem.food(5));  // Short green guy (SGG)
		put(2038, RestoreItem.food(5));  // Premade short green guy
		put(2048, RestoreItem.food(9));  // Pineapple punch
		put(2036, RestoreItem.food(9));  // Premade pineapple punch
		put(2084, RestoreItem.food(9));  // Fruit blast
		put(2034, RestoreItem.food(9));  // Premade fruit blast

		// ==================================================================
		// BARBARIAN FOOD (Barbarian Outpost / Lair)
		// ==================================================================

		put(9980, RestoreItem.food(6));  // Spit-roasted bird meat
		put(7223, RestoreItem.food(7));  // Spit-roasted rabbit meat
		put(9988, RestoreItem.food(8));  // Spit-roasted beast meat

		// ==================================================================
		// SANDWICHES & BAKERY (Canifis, Lumbridge, miscellaneous)
		// ==================================================================

		put(6961, RestoreItem.food(6));  // Baguette
		put(6962, RestoreItem.food(6));  // Triangle sandwich
		put(6963, RestoreItem.food(6));  // Roll
		put(6965, RestoreItem.food(6));  // Square sandwich
		put(25631, RestoreItem.food(6)); // Steak sandwich (B.I.M. — Beneath Cursed Sands)

		// ==================================================================
		// STEWS & SOUPS
		// ==================================================================

		put(2003, RestoreItem.food(11)); // Stew
		put(4239, RestoreItem.food(6));  // Bowl of nettletea
		put(4240, RestoreItem.food(6));  // Milky nettletea (bowl)
		put(4242, RestoreItem.food(6));  // Cup of nettletea
		put(4243, RestoreItem.food(6));  // Milky nettletea (cup)

		// ==================================================================
		// DRINKS / ALCOHOLIC BEVERAGES
		// ==================================================================

		// Ales and beer — vary widely
		put(1917, RestoreItem.food(1));  // Beer
		put(1915, RestoreItem.food(3));  // Grog
		put(1978, RestoreItem.food(3));  // Cup of tea
		put(2955, RestoreItem.food(4));  // Moonlight mead
		put(5749, RestoreItem.food(6));  // Mature moonlight mead
		put(3803, RestoreItem.food(4));  // Viking's tankard (full)
		put(3801, RestoreItem.food(15)); // Keg of beer (Fremennik)
		put(7919, RestoreItem.food(14)); // Rag and bone wine
		put(7157, RestoreItem.food(14)); // Fever rum (Cabin Fever)

		// Spirits (gnome cocktail ingredients that can also be drunk)
		put(2015, RestoreItem.food(5));  // Vodka
		put(2017, RestoreItem.food(5));  // Whisky
		put(2019, RestoreItem.food(5));  // Gin
		put(2021, RestoreItem.food(5));  // Brandy

		// Special drinks
		put(23948, RestoreItem.food(1)); // Elven dawn (Prifddinas)
		put(29277, RestoreItem.food(1)); // Trapper's tipple (Eagles' Peak area)
		put(29409, RestoreItem.food(1)); // Sunbeam ale
		put(27014, RestoreItem.food(1)); // Kovac's grog (Giants' Foundry)
		put(6794,  RestoreItem.food(7)); // Choc-ice (Al Kharid)
		put(4517,  RestoreItem.food(6)); // Giant frog legs (Lumbridge Swamp)

		// ==================================================================
		// VARLAMORE — wines
		// ==================================================================

		put(29944, RestoreItem.food(16)); // Blackbird red
		put(29947, RestoreItem.food(16)); // Chilhuac red
		put(29952, RestoreItem.food(16)); // Ixcoztic white
		put(29955, RestoreItem.food(16)); // Metztonalli white
		put(29958, RestoreItem.food(16)); // Tonameyo white
		put(29963, RestoreItem.food(16)); // Chichilihui rosé
		put(29966, RestoreItem.food(16)); // Imperial rosé

		// ==================================================================
		// CRAB MEATS — Tombs of Amascut crabs
		// ==================================================================

		put(31689, RestoreItem.food(8));  // Red crab meat
		put(31695, RestoreItem.food(14)); // Blue crab meat

		// ==================================================================
		// MISCELLANEOUS SPECIAL FOOD
		// ==================================================================

		put(403,   RestoreItem.food(4));  // Edible seaweed (Tutorial Island / underwater)
		put(4012,  RestoreItem.food(4));  // Monkey nuts (Ape Atoll)
		put(4014,  RestoreItem.food(5));  // Monkey bar (Ape Atoll)
		put(4016,  RestoreItem.food(11)); // Banana stew (Ape Atoll)
		put(7572,  RestoreItem.food(5));  // Red banana (100th island)
		put(7573,  RestoreItem.food(5));  // Tchiki monkey nuts (100th island)
		put(7574,  RestoreItem.food(5));  // Sliced red banana (100th island)
		put(7579,  RestoreItem.food(20)); // Cooked stuffed snake (100th island)
		put(7530,  RestoreItem.food(11)); // Pirate's fishcake (Great Brain Robbery)
		put(1977,  RestoreItem.food(4));  // Chocolatey milk
		put(7934,  RestoreItem.food(10)); // Pest Control field ration
		put(10136, RestoreItem.food(11)); // Hunting fish special (Tai Bwo Wannai)
		put(24785, RestoreItem.food(5));  // Cooked mystery meat (Chambers of Xeric, surface)

		// ==================================================================
		// OTHER NOTABLE FOOD
		// ==================================================================

		// Guthix rest (herblore tea, 4 doses, heals 5 HP per dose)
		put(4417, RestoreItem.food(5)); // Guthix rest(4)
		put(4419, RestoreItem.food(5)); // Guthix rest(3)
		put(4421, RestoreItem.food(5)); // Guthix rest(2)
		put(4423, RestoreItem.food(5)); // Guthix rest(1)

		// ==================================================================
		// CHAMBERS OF XERIC — XERIC'S AID  (three tiers, 4 doses each)
		// All tiers can overheal (dynamic formula).
		// All tier formulas sourced from RuneLite itemstats plugin and OSRS wiki.
		// ==================================================================

		// Xeric's aid (-): floor(hp * 5 / 100) + 1  [estimated]
		put(20973, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_WEAK)); // Xeric's aid(-)(1)
		put(20974, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_WEAK)); // Xeric's aid(-)(2)
		put(20975, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_WEAK)); // Xeric's aid(-)(3)
		put(20976, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_WEAK)); // Xeric's aid(-)(4)

		// Xeric's aid (regular): floor(hp * 10 / 100) + 2  [estimated]
		put(20977, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID)); // Xeric's aid(1)
		put(20978, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID)); // Xeric's aid(2)
		put(20979, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID)); // Xeric's aid(3)
		put(20980, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID)); // Xeric's aid(4)

		// Xeric's aid (+): floor(hp * 15 / 100) + 5  [from itemstats: SaradominBrew(0.15,…,5,…)]
		put(20981, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_STRONG)); // Xeric's aid(+)(1)
		put(20982, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_STRONG)); // Xeric's aid(+)(2)
		put(20983, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_STRONG)); // Xeric's aid(+)(3)
		put(20984, RestoreItem.dynamicFood(DynamicHpType.XERIC_AID_STRONG)); // Xeric's aid(+)(4)

		// ==================================================================
		// CHAMBERS OF XERIC — REVITALISATION POTION  (three tiers, 4 doses each)
		// All tier formulas sourced from RuneLite itemstats plugin and OSRS wiki.
		// ==================================================================

		// Revitalisation potion (-): floor(prayer * 25 / 100) + 7  [estimated ≈ Prayer potion]
		put(20949, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Revitalisation(-)(1)
		put(20950, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Revitalisation(-)(2)
		put(20951, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Revitalisation(-)(3)
		put(20952, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Revitalisation(-)(4)

		// Revitalisation potion (regular): floor(prayer * 25 / 100) + 8  [estimated ≈ Super restore]
		put(20953, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Revitalisation(1)
		put(20954, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Revitalisation(2)
		put(20955, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Revitalisation(3)
		put(20956, RestoreItem.prayer(PrayerRestoreType.SUPER_RESTORE)); // Revitalisation(4)

		// Revitalisation potion (+): floor(prayer * 30 / 100) + 11  [from itemstats: SuperRestore(.30, 11)]
		put(20957, RestoreItem.prayer(PrayerRestoreType.REVITALISATION_STRONG)); // Revitalisation(+)(1)
		put(20958, RestoreItem.prayer(PrayerRestoreType.REVITALISATION_STRONG)); // Revitalisation(+)(2)
		put(20959, RestoreItem.prayer(PrayerRestoreType.REVITALISATION_STRONG)); // Revitalisation(+)(3)
		put(20960, RestoreItem.prayer(PrayerRestoreType.REVITALISATION_STRONG)); // Revitalisation(+)(4)

		// ==================================================================
		// CHAMBERS OF XERIC — PRAYER ENHANCE  (three tiers, 4 doses each)
		// All tiers gradually restore 1 prayer every 12 ticks (same mechanic as
		// Prayer regeneration potion).  Overlay shows "1/12t"; infobox tracks
		// the countdown.  Note: lower tiers restore fewer total prayer points per
		// dose (shorter duration), but the tick rate is the same across all tiers.
		// ==================================================================

		put(20961, RestoreItem.prayerRegen()); // Prayer enhance(-)(1)
		put(20962, RestoreItem.prayerRegen()); // Prayer enhance(-)(2)
		put(20963, RestoreItem.prayerRegen()); // Prayer enhance(-)(3)
		put(20964, RestoreItem.prayerRegen()); // Prayer enhance(-)(4)

		put(20965, RestoreItem.prayerRegen()); // Prayer enhance(1)
		put(20966, RestoreItem.prayerRegen()); // Prayer enhance(2)
		put(20967, RestoreItem.prayerRegen()); // Prayer enhance(3)
		put(20968, RestoreItem.prayerRegen()); // Prayer enhance(4)

		put(20969, RestoreItem.prayerRegen()); // Prayer enhance(+)(1)
		put(20970, RestoreItem.prayerRegen()); // Prayer enhance(+)(2)
		put(20971, RestoreItem.prayerRegen()); // Prayer enhance(+)(3)
		put(20972, RestoreItem.prayerRegen()); // Prayer enhance(+)(4)

		// ==================================================================
		// TOMBS OF AMASCUT
		// ==================================================================

		// Nectar (4 doses) — floor(hp * 15 / 100) + 3; can overheal
		// Formula from RuneLite itemstats: perc(.15, 3)
		put(27315, RestoreItem.dynamicFood(DynamicHpType.NECTAR)); // Nectar(4)
		put(27317, RestoreItem.dynamicFood(DynamicHpType.NECTAR)); // Nectar(3)
		put(27319, RestoreItem.dynamicFood(DynamicHpType.NECTAR)); // Nectar(2)
		put(27321, RestoreItem.dynamicFood(DynamicHpType.NECTAR)); // Nectar(1)

		// Tears of Elidinis (4 doses) — restores prayer: floor(prayer * 25 / 100) + 10
		// Formula from RuneLite itemstats: perc(.25, 10)
		put(27327, RestoreItem.prayer(PrayerRestoreType.TEARS_OF_ELIDINIS)); // Tears of Elidinis(4)
		put(27329, RestoreItem.prayer(PrayerRestoreType.TEARS_OF_ELIDINIS)); // Tears of Elidinis(3)
		put(27331, RestoreItem.prayer(PrayerRestoreType.TEARS_OF_ELIDINIS)); // Tears of Elidinis(2)
		put(27333, RestoreItem.prayer(PrayerRestoreType.TEARS_OF_ELIDINIS)); // Tears of Elidinis(1)

		// Honey locust — 20 HP (overheal) + Prayer Potion prayer formula
		// HP: from RuneLite itemstats: boost(HITPOINTS, 20)
		// Prayer: from RuneLite itemstats: prayerPot = PrayerPotion(7)
		put(27351, RestoreItem.builder()
			.instantHp(20)
			.dynamicHpType(DynamicHpType.FIXED_OVERHEAL)
			.prayerRestoreType(PrayerRestoreType.PRAYER_POTION)
			.build()); // Honey locust

		// ==================================================================
		// MOONS OF PERIL
		// ==================================================================

		// Moonlight potion — prayer: floor(max(prayer * 0.25, herblore * 0.3)) + 7
		// Requires Herblore level 38. Not affected by Holy Wrench / Ring of the Gods (i).
		// Formula sourced from RuneLite itemstats (MoonlightPotion.java).
		put(29080, RestoreItem.prayer(PrayerRestoreType.MOONLIGHT_POTION)); // Moonlight potion(4)
		put(29081, RestoreItem.prayer(PrayerRestoreType.MOONLIGHT_POTION)); // Moonlight potion(3)
		put(29082, RestoreItem.prayer(PrayerRestoreType.MOONLIGHT_POTION)); // Moonlight potion(2)
		put(29083, RestoreItem.prayer(PrayerRestoreType.MOONLIGHT_POTION)); // Moonlight potion(1)

		// Moonlight Moth jar — 22 prayer (flat, not formula-based)
		put(28893, RestoreItem.flatPrayer(22)); // Moonlight Moth jar

		// Moonlight Moth Mix — 6 HP + 22 prayer (flat)
		put(29195, RestoreItem.foodWithFlatPrayer(6, 22)); // Moonlight Moth Mix(2)
		put(29213, RestoreItem.foodWithFlatPrayer(6, 22)); // Moonlight Moth Mix(1)

		// Cooked bream — max(7, min(floor(cooking / 3), floor(fishing / 3)))
		// Formula sourced from RuneLite itemstats (CookedBream.java).
		put(29217, RestoreItem.dynamicFood(DynamicHpType.COOKED_BREAM)); // Cooked bream

		// Cooked moss lizard — min(floor(cooking / 3), floor(hunter / 2))
		// Formula sourced from RuneLite itemstats (CookedMossLizard.java).
		put(29077, RestoreItem.dynamicFood(DynamicHpType.COOKED_MOSS_LIZARD)); // Cooked moss lizard

		// Eclipse red — 16 HP (also +1 Woodcutting, -5 Attack, -1 Fletching; side effects omitted)
		put(29415, RestoreItem.food(16)); // Eclipse red

		// Moon-lite and Sun-shine — 5 HP each
		put(29418, RestoreItem.food(5)); // Moon-lite
		put(29421, RestoreItem.food(5)); // Sun-shine

		// ==================================================================
		// THE GAUNTLET & CORRUPTED GAUNTLET
		// ==================================================================

		// Paddlefish — heals 20 HP (regular Gauntlet, sourced from RuneLite itemstats)
		put(23874, RestoreItem.food(20)); // Paddlefish

		// Combo food variants — heals 16 HP (Gauntlet / Corrupted Gauntlet variants)
		put(25960, RestoreItem.food(16)); // Crystal paddlefish (Gauntlet)
		put(25958, RestoreItem.food(16)); // Corrupted paddlefish (Corrupted Gauntlet)

		// Egniol potion — prayer: floor(prayer * 25/100) + 7 (Prayer Potion formula)
		// Also restores 40 run energy per dose; run energy not tracked here.
		put(23885, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Egniol potion(4)
		put(23884, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Egniol potion(3)
		put(23883, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Egniol potion(2)
		put(23882, RestoreItem.prayer(PrayerRestoreType.PRAYER_POTION)); // Egniol potion(1)

		// ==================================================================
		// ITEM GROUPS — maps partial/half variants to the full item's ID
		// so that first-item-only mode treats them as the same food type.
		// ==================================================================

		// Prayer potion doses
		GROUP_IDS.put(139,  2434); // Prayer potion(3) → (4)
		GROUP_IDS.put(141,  2434); // Prayer potion(2) → (4)
		GROUP_IDS.put(143,  2434); // Prayer potion(1) → (4)

		// Super restore doses
		GROUP_IDS.put(3026, 3024); // Super restore(3) → (4)
		GROUP_IDS.put(3028, 3024); // Super restore(2) → (4)
		GROUP_IDS.put(3030, 3024); // Super restore(1) → (4)

		// Sanfew serum doses
		GROUP_IDS.put(10927, 10925); // Sanfew serum(3) → (4)
		GROUP_IDS.put(10929, 10925); // Sanfew serum(2) → (4)
		GROUP_IDS.put(10931, 10925); // Sanfew serum(1) → (4)

		// Ancient brew doses
		GROUP_IDS.put(26342, 26340); // Ancient brew(3) → (4)
		GROUP_IDS.put(26344, 26340); // Ancient brew(2) → (4)
		GROUP_IDS.put(26346, 26340); // Ancient brew(1) → (4)

		// Forgotten brew doses
		GROUP_IDS.put(27632, 27629); // Forgotten brew(3) → (4)
		GROUP_IDS.put(27635, 27629); // Forgotten brew(2) → (4)
		GROUP_IDS.put(27638, 27629); // Forgotten brew(1) → (4)

		// Blighted super restore doses
		GROUP_IDS.put(24601, 24598); // Blighted super restore(3) → (4)
		GROUP_IDS.put(24603, 24598); // Blighted super restore(2) → (4)
		GROUP_IDS.put(24605, 24598); // Blighted super restore(1) → (4)

		// Prayer regeneration potion doses
		GROUP_IDS.put(30128, 30125); // Prayer regeneration potion(3) → (4)
		GROUP_IDS.put(30131, 30125); // Prayer regeneration potion(2) → (4)
		GROUP_IDS.put(30134, 30125); // Prayer regeneration potion(1) → (4)

		// Prayer mix doses
		GROUP_IDS.put(11467, 11465); // Prayer mix(1) → (2)

		// Super restore mix doses
		GROUP_IDS.put(11495, 11493); // Super restore mix(1) → (2)

		// Zamorak mix doses
		GROUP_IDS.put(11523, 11521); // Zamorak mix(1) → (2)

		// Moonlight Moth Mix doses
		GROUP_IDS.put(29213, 29195); // Moonlight Moth Mix(1) → (2)

		// Saradomin brew doses
		GROUP_IDS.put(6687, 6685); // Saradomin brew(3) → (4)
		GROUP_IDS.put(6689, 6685); // Saradomin brew(2) → (4)
		GROUP_IDS.put(6691, 6685); // Saradomin brew(1) → (4)

		// Guthix rest doses
		GROUP_IDS.put(4419, 4417); // Guthix rest(3) → (4)
		GROUP_IDS.put(4421, 4417); // Guthix rest(2) → (4)
		GROUP_IDS.put(4423, 4417); // Guthix rest(1) → (4)

		// Pizzas
		GROUP_IDS.put(2291, 2289); // Half plain pizza     → Plain pizza (full)
		GROUP_IDS.put(2295, 2293); // Half meat pizza      → Meat pizza (full)
		GROUP_IDS.put(2299, 2297); // Half anchovy pizza   → Anchovy pizza (full)
		GROUP_IDS.put(2303, 2301); // Half pineapple pizza → Pineapple pizza (full)

		// Pies
		GROUP_IDS.put(2333, 2325);  // Half redberry pie    → Redberry pie (full)
		GROUP_IDS.put(2315, 2327);  // Half meat pie        → Meat pie (full)
		GROUP_IDS.put(2317, 2335);  // Half apple pie       → Apple pie (full)
		GROUP_IDS.put(7180, 7178);  // Half garden pie      → Garden pie (full)
		GROUP_IDS.put(7190, 7188);  // Half fish pie        → Fish pie (full)
		GROUP_IDS.put(7200, 7198);  // Half admiral pie     → Admiral pie (full)
		GROUP_IDS.put(7210, 7208);  // Half wild pie        → Wild pie (full)
		GROUP_IDS.put(7220, 7218);  // Half summer pie      → Summer pie (full)
		GROUP_IDS.put(21690, 21687); // Half mushroom pie   → Mushroom pie (full)
		GROUP_IDS.put(22795, 22794); // Half botanical pie  → Botanical pie (full)
		GROUP_IDS.put(22793, 22791); // Half dragonfruit pie → Dragonfruit pie (full)

		// Cakes (3 slices each)
		GROUP_IDS.put(1893, 1891); // Cake (2/3)            → Cake (full)
		GROUP_IDS.put(1895, 1891); // Cake (1/3)            → Cake (full)
		GROUP_IDS.put(1899, 1897); // Chocolate slice (2/3) → Chocolate cake (full)
		GROUP_IDS.put(1901, 1897); // Chocolate slice (1/3) → Chocolate cake (full)

		// Citrus fruit segments → whole fruit
		GROUP_IDS.put(2104, 2102); // Lemon chunks  → Lemon
		GROUP_IDS.put(2106, 2102); // Lemon slices  → Lemon
		GROUP_IDS.put(2110, 2108); // Orange chunks → Orange
		GROUP_IDS.put(2112, 2108); // Orange slices → Orange
		GROUP_IDS.put(2122, 2120); // Lime chunks   → Lime
		GROUP_IDS.put(2124, 2120); // Lime slices   → Lime
		GROUP_IDS.put(2116, 2118); // Pineapple chunks → Pineapple ring

		// Premade gnome food → cooked equivalents
		GROUP_IDS.put(2237, 2205); // Premade worm crunchies   → worm crunchies
		GROUP_IDS.put(2239, 2209); // Premade chocchip crunchies → chocchip crunchies
		GROUP_IDS.put(2241, 2213); // Premade spicy crunchies  → spicy crunchies
		GROUP_IDS.put(2243, 2217); // Premade toad crunchies   → toad crunchies
		GROUP_IDS.put(2219, 2253); // Premade worm batta       → worm batta
		GROUP_IDS.put(2221, 2255); // Premade toad batta       → toad batta
		GROUP_IDS.put(2223, 2259); // Premade cheese+tom batta → cheese+tom batta
		GROUP_IDS.put(2225, 2277); // Premade fruit batta      → fruit batta
		GROUP_IDS.put(2227, 2281); // Premade vegetable batta  → vegetable batta
		GROUP_IDS.put(2233, 2191); // Premade worm hole        → worm hole
		GROUP_IDS.put(2235, 2195); // Premade veg ball         → veg ball
		GROUP_IDS.put(2231, 2187); // Premade tangled toad's legs → tangled toad's legs
		GROUP_IDS.put(2229, 2185); // Premade chocolate bomb   → chocolate bomb

		// Premade cocktails → made equivalents
		GROUP_IDS.put(2028, 2064); // Premade blurberry special → blurberry special
		GROUP_IDS.put(2032, 2092); // Premade drunk dragon      → drunk dragon
		GROUP_IDS.put(2030, 2074); // Premade choc saturday     → chocolate saturday
		GROUP_IDS.put(2040, 2054); // Premade wizard blizzard   → wizard blizzard
		GROUP_IDS.put(2038, 2080); // Premade short green guy   → short green guy
		GROUP_IDS.put(2036, 2048); // Premade pineapple punch   → pineapple punch
		GROUP_IDS.put(2034, 2084); // Premade fruit blast       → fruit blast

		// Nettletea variants → plain bowl
		GROUP_IDS.put(4240, 4239); // Milky nettletea (bowl)  → plain bowl
		GROUP_IDS.put(4242, 4239); // Cup of nettletea        → plain bowl
		GROUP_IDS.put(4243, 4239); // Milky nettletea (cup)   → plain bowl

		// Xeric's aid (-) doses  [4-dose = 20976 is canonical]
		GROUP_IDS.put(20973, 20976); // Xeric's aid(-)(1) → (4)
		GROUP_IDS.put(20974, 20976); // Xeric's aid(-)(2) → (4)
		GROUP_IDS.put(20975, 20976); // Xeric's aid(-)(3) → (4)

		// Xeric's aid (regular) doses  [4-dose = 20980 is canonical]
		GROUP_IDS.put(20977, 20980); // Xeric's aid(1) → (4)
		GROUP_IDS.put(20978, 20980); // Xeric's aid(2) → (4)
		GROUP_IDS.put(20979, 20980); // Xeric's aid(3) → (4)

		// Xeric's aid (+) doses  [4-dose = 20984 is canonical]
		GROUP_IDS.put(20981, 20984); // Xeric's aid(+)(1) → (4)
		GROUP_IDS.put(20982, 20984); // Xeric's aid(+)(2) → (4)
		GROUP_IDS.put(20983, 20984); // Xeric's aid(+)(3) → (4)

		// Revitalisation potion (-) doses  [4-dose = 20952 is canonical]
		GROUP_IDS.put(20949, 20952); // Revitalisation(-)(1) → (4)
		GROUP_IDS.put(20950, 20952); // Revitalisation(-)(2) → (4)
		GROUP_IDS.put(20951, 20952); // Revitalisation(-)(3) → (4)

		// Revitalisation potion (regular) doses  [4-dose = 20956 is canonical]
		GROUP_IDS.put(20953, 20956); // Revitalisation(1) → (4)
		GROUP_IDS.put(20954, 20956); // Revitalisation(2) → (4)
		GROUP_IDS.put(20955, 20956); // Revitalisation(3) → (4)

		// Revitalisation potion (+) doses  [4-dose = 20960 is canonical]
		GROUP_IDS.put(20957, 20960); // Revitalisation(+)(1) → (4)
		GROUP_IDS.put(20958, 20960); // Revitalisation(+)(2) → (4)
		GROUP_IDS.put(20959, 20960); // Revitalisation(+)(3) → (4)

		// Prayer enhance (-) doses  [4-dose = 20964 is canonical]
		GROUP_IDS.put(20961, 20964); // Prayer enhance(-)(1) → (4)
		GROUP_IDS.put(20962, 20964); // Prayer enhance(-)(2) → (4)
		GROUP_IDS.put(20963, 20964); // Prayer enhance(-)(3) → (4)

		// Prayer enhance (regular) doses  [4-dose = 20968 is canonical]
		GROUP_IDS.put(20965, 20968); // Prayer enhance(1) → (4)
		GROUP_IDS.put(20966, 20968); // Prayer enhance(2) → (4)
		GROUP_IDS.put(20967, 20968); // Prayer enhance(3) → (4)

		// Prayer enhance (+) doses  [4-dose = 20972 is canonical]
		GROUP_IDS.put(20969, 20972); // Prayer enhance(+)(1) → (4)
		GROUP_IDS.put(20970, 20972); // Prayer enhance(+)(2) → (4)
		GROUP_IDS.put(20971, 20972); // Prayer enhance(+)(3) → (4)

		// Nectar doses  [4-dose = 27315 is canonical]
		GROUP_IDS.put(27317, 27315); // Nectar(3) → (4)
		GROUP_IDS.put(27319, 27315); // Nectar(2) → (4)
		GROUP_IDS.put(27321, 27315); // Nectar(1) → (4)

		// Tears of Elidinis doses  [4-dose = 27327 is canonical]
		GROUP_IDS.put(27329, 27327); // Tears of Elidinis(3) → (4)
		GROUP_IDS.put(27331, 27327); // Tears of Elidinis(2) → (4)
		GROUP_IDS.put(27333, 27327); // Tears of Elidinis(1) → (4)

		// Moonlight potion doses  [4-dose = 29080 is canonical]
		GROUP_IDS.put(29081, 29080); // Moonlight potion(3) → (4)
		GROUP_IDS.put(29082, 29080); // Moonlight potion(2) → (4)
		GROUP_IDS.put(29083, 29080); // Moonlight potion(1) → (4)

		// Egniol potion doses  [4-dose = 23885 is canonical]
		GROUP_IDS.put(23882, 23885); // Egniol potion(1) → (4)
		GROUP_IDS.put(23883, 23885); // Egniol potion(2) → (4)
		GROUP_IDS.put(23884, 23885); // Egniol potion(3) → (4)
	}

	private static void put(int itemId, RestoreItem item)
	{
		DATABASE.put(itemId, item);
	}

	/**
	 * Returns the {@link RestoreItem} for the given item ID, or {@code null} if
	 * the item is not tracked by this plugin.
	 */
	@Nullable
	public static RestoreItem get(int itemId)
	{
		return DATABASE.get(itemId);
	}

	/**
	 * Returns the canonical group ID for the given item ID.
	 * Half/partial variants of pies, pizzas, and cakes return the full item's ID
	 * so that first-item-only mode treats them as the same food type.
	 * Items with no group mapping return their own ID.
	 */
	public static int getGroupId(int itemId)
	{
		return GROUP_IDS.getOrDefault(itemId, itemId);
	}

	private RestoreItemDatabase()
	{
	}
}
