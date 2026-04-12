package com.inventoryrestore.data;

/**
 * Identifies items whose HP heal is not a fixed value but depends on player stats.
 */
public enum DynamicHpType
{
	/**
	 * Anglerfish: heals floor(hp/10) + 2*floor(hp/25) + 5*floor(hp/93) + 2, and can overheal.
	 * Formula sourced from RuneLite itemstats. Maximum 22 (at 99 Hitpoints).
	 */
	ANGLERFISH,

	/**
	 * Saradomin brew: heals floor(hp_level * 0.15) + 2 per dose.
	 * Maximum 16 at 99 Hitpoints.
	 */
	SARADOMIN_BREW,

	// ------------------------------------------------------------------
	// Chambers of Xeric — Xeric's aid (three tiers)
	// All three formulas follow the same tier pattern sourced from RuneLite's
	// itemstats plugin and the OSRS wiki.
	// ------------------------------------------------------------------

	/**
	 * Xeric's aid (-): heals floor(hp_level * 0.05) + 1.
	 * Formula sourced from OSRS wiki.
	 */
	XERIC_AID_WEAK,

	/**
	 * Xeric's aid (regular): heals floor(hp_level * 0.10) + 2.
	 * Formula sourced from OSRS wiki.
	 */
	XERIC_AID,

	/**
	 * Xeric's aid (+): heals floor(hp_level * 0.15) + 5.
	 * Formula sourced from RuneLite itemstats (SaradominBrew(0.15, …, 5, …)).
	 */
	XERIC_AID_STRONG,

	// ------------------------------------------------------------------
	// Tombs of Amascut
	// ------------------------------------------------------------------

	/**
	 * Nectar: heals floor(hp_level * 0.15) + 3.
	 * Formula sourced from RuneLite itemstats (perc(.15, 3)).
	 */
	NECTAR,

	/**
	 * Sentinel for fixed-amount overheal items (e.g. Honey locust: 20 HP).
	 * The actual heal amount is stored in {@code instantHp}; this type flag
	 * just signals that the item can boost HP above the player's real level.
	 * {@code computeHp()} falls through to {@code item.getInstantHp()}.
	 */
	FIXED_OVERHEAL,

	// ------------------------------------------------------------------
	// Moons of Peril
	// ------------------------------------------------------------------

	/**
	 * Cooked Bream: max(7, min(floor(cooking / 3), floor(fishing / 3))).
	 * Formula sourced from RuneLite itemstats (CookedBream.java).
	 */
	COOKED_BREAM,

	/**
	 * Cooked Moss Lizard: min(floor(cooking / 3), floor(hunter / 2)).
	 * Formula sourced from RuneLite itemstats (CookedMossLizard.java).
	 */
	COOKED_MOSS_LIZARD,

	// ------------------------------------------------------------------
	// Varlamore
	// ------------------------------------------------------------------

	/**
	 * Haddock: min(18, floor(hp_level * 0.20)).
	 * Uses the player's real Hitpoints level, capped at 18.
	 */
	HADDOCK
}
