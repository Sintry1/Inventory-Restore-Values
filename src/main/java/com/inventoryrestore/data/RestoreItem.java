package com.inventoryrestore.data;

import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

/**
 * Describes what a single item eat/dose restores.
 *
 * <p>For food with static HP: use {@code instantHp > 0}, leave prayer fields null.
 * <p>For two-part hunter meats: set both {@code instantHp} and {@code delayedHp}/{@code delayTicks}.
 * <p>For prayer potions: set {@code prayerRestoreType}; HP fields stay 0.
 * <p>For dynamic HP (Anglerfish, Saradomin brew): use {@code dynamicHpType}.
 * <p>Items can have both HP and prayer fields (e.g. items that restore both).
 */
@Value
@Builder
public class RestoreItem
{
	/** Instant HP restored on eat/drink (0 = none). */
	int instantHp;

	/** Delayed HP restored after {@code delayTicks} game ticks (0 = no delay). */
	int delayedHp;

	/** Ticks until the delayed HP fires (only relevant when {@code delayedHp > 0}). */
	int delayTicks;

	/** Non-null if the prayer restore follows a formula. */
	@Nullable
	PrayerRestoreType prayerRestoreType;

	/** Non-null if the HP heal follows a dynamic formula rather than a fixed value. */
	@Nullable
	DynamicHpType dynamicHpType;

	/**
	 * Fixed prayer points restored instantly (0 = none).
	 * Used for items like Jangerberries that restore a flat prayer amount
	 * not derived from the player's prayer level.
	 */
	int flatPrayerRestore;

	// ------------------------------------------------------------------
	// Convenience factories
	// ------------------------------------------------------------------

	/** Plain food with a fixed HP heal. */
	public static RestoreItem food(int hp)
	{
		return RestoreItem.builder().instantHp(hp).build();
	}

	/** Two-part healing food (e.g. hunter meats). */
	public static RestoreItem twoPartFood(int instant, int delayed, int delayTicks)
	{
		return RestoreItem.builder()
			.instantHp(instant)
			.delayedHp(delayed)
			.delayTicks(delayTicks)
			.build();
	}

	/** Food whose HP heal is computed dynamically (Anglerfish, Saradomin brew). */
	public static RestoreItem dynamicFood(DynamicHpType type)
	{
		return RestoreItem.builder().dynamicHpType(type).build();
	}

	/** Prayer-restoring potion with no HP component. */
	public static RestoreItem prayer(PrayerRestoreType type)
	{
		return RestoreItem.builder().prayerRestoreType(type).build();
	}

	/** Prayer regeneration potion (special over-time behaviour). */
	public static RestoreItem prayerRegen()
	{
		return RestoreItem.builder().prayerRestoreType(PrayerRestoreType.PRAYER_REGEN).build();
	}

	/**
	 * Combo item: fixed HP heal + formula-based prayer restore (e.g. herblore mixes).
	 * The prayer text and HP text are displayed side-by-side with individual colours.
	 */
	public static RestoreItem comboItem(int hp, PrayerRestoreType prayerType)
	{
		return RestoreItem.builder().instantHp(hp).prayerRestoreType(prayerType).build();
	}

	/** Item that restores HP and also gives a flat prayer restore not based on level. */
	public static RestoreItem foodWithFlatPrayer(int hp, int flatPrayer)
	{
		return RestoreItem.builder().instantHp(hp).flatPrayerRestore(flatPrayer).build();
	}

	/** Item that gives only a flat prayer restore with no HP component (e.g. Moonlight Moth jar). */
	public static RestoreItem flatPrayer(int flatPrayer)
	{
		return RestoreItem.builder().flatPrayerRestore(flatPrayer).build();
	}

	// ------------------------------------------------------------------
	// Helpers
	// ------------------------------------------------------------------

	public boolean hasInstantHp()
	{
		return instantHp > 0 || dynamicHpType != null;
	}

	public boolean hasDelayedHeal()
	{
		return delayedHp > 0;
	}

	public boolean hasPrayerRestore()
	{
		return prayerRestoreType != null || flatPrayerRestore > 0;
	}

	public boolean isPrayerRegen()
	{
		return prayerRestoreType == PrayerRestoreType.PRAYER_REGEN;
	}
}
