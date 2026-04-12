package com.inventoryrestore.data;

/**
 * Identifies which prayer-restoration formula to apply for a given potion.
 * Formulas assume the player's real prayer level and any active prayer bonuses
 * (Holy Wrench / Ring of the Gods (i)) are passed in at render time.
 */
public enum PrayerRestoreType
{
	/** floor(prayer * 0.25) + 7, boosted by wrench/ring */
	PRAYER_POTION,

	/** floor(prayer * 0.27) + 8, boosted by wrench/ring */
	SUPER_RESTORE,

	/** floor(prayer * 0.30) + 4, boosted by wrench/ring */
	SANFEW_SERUM,

	/** floor(prayer * 0.10) + 2, NOT boosted by wrench/ring */
	ANCIENT_BREW,

	/** 1 point every 12 ticks (7.2 s) – overlay shows "1/12t", tracked via infobox */
	PRAYER_REGEN,

	// ------------------------------------------------------------------
	// Chambers of Xeric — Revitalisation potion
	// All three tier formulas are sourced from RuneLite itemstats and the OSRS wiki.
	// ------------------------------------------------------------------

	/**
	 * Revitalisation potion (+): floor(prayer * 0.30) + 11, boosted by wrench/ring.
	 * Formula sourced from RuneLite itemstats (SuperRestore(.30, 11)).
	 */
	REVITALISATION_STRONG,

	// ------------------------------------------------------------------
	// Tombs of Amascut
	// ------------------------------------------------------------------

	/**
	 * Tears of Elidinis: floor(prayer * 0.25) + 10, boosted by wrench/ring.
	 * Formula sourced from RuneLite itemstats (perc(.25, 10)).
	 */
	TEARS_OF_ELIDINIS,

	// ------------------------------------------------------------------
	// Moons of Peril
	// ------------------------------------------------------------------

	/**
	 * Moonlight Potion: floor(max(prayer * 0.25, herblore * 0.3)) + 7.
	 * Requires Herblore level 38. NOT boosted by Holy Wrench / Ring of the Gods (i).
	 * Formula sourced from RuneLite itemstats (MoonlightPotion.java).
	 */
	MOONLIGHT_POTION
}
