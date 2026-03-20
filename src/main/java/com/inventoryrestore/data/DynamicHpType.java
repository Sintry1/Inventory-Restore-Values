package com.inventoryrestore.data;

/**
 * Identifies items whose HP heal is not a fixed value but depends on player stats.
 */
public enum DynamicHpType
{
	/**
	 * Anglerfish: heals floor(hp_level * 0.22) + 3, and can overheal.
	 * Minimum 3, maximum 22 (at 99 Hitpoints).
	 */
	ANGLERFISH,

	/**
	 * Saradomin brew: heals floor(hp_level * 0.15) + 2 per dose.
	 * Maximum 16 at 99 Hitpoints.
	 */
	SARADOMIN_BREW
}
