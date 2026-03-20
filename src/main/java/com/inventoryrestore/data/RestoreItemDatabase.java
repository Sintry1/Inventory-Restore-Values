package com.inventoryrestore.data;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Maps RuneLite item IDs to their {@link RestoreItem} descriptor.
 *
 * <p>All item IDs are sourced from the OSRS wiki and RuneLite's ItemID constants.
 * Items marked "TODO: verify ID" should be cross-checked in-game using RuneLite's
 * examine feature or the OSRS wiki before shipping.
 *
 * <p>Entries are added for every dose/slice/half variant of multi-use items so the
 * overlay always shows the correct per-bite value regardless of how many uses remain.
 */
public final class RestoreItemDatabase
{
	private static final Map<Integer, RestoreItem> DATABASE = new HashMap<>();

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
		// Note: does NOT restore HP
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
		// DYNAMIC HP FOOD
		// ==================================================================

		// Saradomin brew — floor(hp * 15/100) + 2 per dose (restores HP only, not prayer)
		put(6685, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(4)
		put(6687, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(3)
		put(6689, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(2)
		put(6691, RestoreItem.dynamicFood(DynamicHpType.SARADOMIN_BREW)); // Saradomin brew(1)

		// Anglerfish — floor(hp/10) + 2*floor(hp/25) + 5*floor(hp/93) + 2  (can overheal)
		put(13441, RestoreItem.dynamicFood(DynamicHpType.ANGLERFISH)); // Anglerfish

		// Blighted anglerfish — same formula as regular anglerfish (wilderness only)
		// TODO: verify item ID
		// put(24610, RestoreItem.dynamicFood(DynamicHpType.ANGLERFISH)); // Blighted anglerfish

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
		// FISH (cooked)
		// ==================================================================

		put(315,   RestoreItem.food(3));  // Shrimps
		put(319,   RestoreItem.food(1));  // Anchovies
		put(325,   RestoreItem.food(4));  // Sardine
		put(347,   RestoreItem.food(5));  // Herring
		put(355,   RestoreItem.food(6));  // Mackerel
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

		// Wilderness blighted fish — TODO: verify all IDs below before enabling
		// put(???, RestoreItem.food(22)); // Blighted manta ray
		// put(???, RestoreItem.food(22)); // Blighted anglerfish (dynamic formula — use DynamicHpType.ANGLERFISH)
		// put(???, RestoreItem.food(18)); // Blighted karambwan

		// ==================================================================
		// MEAT, BREAD & BASICS
		// ==================================================================

		put(2140, RestoreItem.food(3));  // Cooked chicken
		put(2142, RestoreItem.food(3));  // Cooked meat
		put(2146, RestoreItem.food(3));  // Cooked rabbit
		put(2309, RestoreItem.food(5));  // Bread
		put(1993, RestoreItem.food(11)); // Jug of wine   (also lowers Attack, we show the heal only)
		put(2011, RestoreItem.food(19)); // Curry         (also restores 1 prayer point — negligible, skipped)
		put(1883, RestoreItem.food(19)); // Ugthanki kebab
		put(6883, RestoreItem.food(8));  // Peach
		put(2878, RestoreItem.food(15)); // Cooked chompy

		// ==================================================================
		// BAKED POTATOES & FILLINGS
		// ==================================================================

		put(6705, RestoreItem.food(16)); // Potato with cheese
		put(7060, RestoreItem.food(22)); // Tuna potato
		put(6703, RestoreItem.food(14)); // Chilli potato
		put(6701, RestoreItem.food(14)); // Egg potato
		put(6697, RestoreItem.food(12)); // Baked potato (with butter)
		put(6699, RestoreItem.food(14)); // Mushroom potato

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
		put(2315, RestoreItem.food(6));  // Half meat pie      — TODO: verify ID
		put(2335, RestoreItem.food(7));  // Apple pie          (full)
		put(2317, RestoreItem.food(7));  // Half apple pie     — TODO: verify ID
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
		put(21690, RestoreItem.food(8)); // Half mushroom pie  — TODO: verify ID
		put(22794, RestoreItem.food(8)); // Botanical pie      (full, also +4 Herblore boost)
		put(22795, RestoreItem.food(8)); // Half botanical pie — TODO: verify ID
		put(22791, RestoreItem.food(10)); // Dragonfruit pie   (full, also +4 Fletching boost) — TODO: verify ID
		put(22793, RestoreItem.food(10)); // Half dragonfruit pie — TODO: verify ID

		// ==================================================================
		// OTHER NOTABLE FOOD
		// ==================================================================

		// Guthix rest (herblore tea, 4 doses, heals 5 HP per dose)
		put(4417, RestoreItem.food(5)); // Guthix rest(4) — TODO: verify ID
		put(4419, RestoreItem.food(5)); // Guthix rest(3) — TODO: verify ID
		put(4421, RestoreItem.food(5)); // Guthix rest(2) — TODO: verify ID
		put(4423, RestoreItem.food(5)); // Guthix rest(1) — TODO: verify ID

		// Marlin (Trouble Brewing minigame) — TODO: verify ID
		// put(7805, RestoreItem.food(24)); // Marlin
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

	private RestoreItemDatabase()
	{
	}
}
