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
	PRAYER_REGEN
}
