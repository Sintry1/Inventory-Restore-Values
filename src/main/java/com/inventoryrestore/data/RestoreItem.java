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

	/** Prayer points restored per regen interval (0 = no over-time effect). */
	int prayerRegenAmount;

	/** Ticks between each regen restore (only relevant when {@code prayerRegenAmount > 0}). */
	int prayerRegenTicks;

	/** Total ticks the regen effect lasts per dose (only relevant when {@code prayerRegenAmount > 0}). */
	int prayerRegenDuration;

	/**
	 * True for items that fully restore both Hitpoints and Prayer
	 * (e.g. Jar of congealed blood). The overlay shows the player's real
	 * HP/prayer levels instead of a fixed restore amount.
	 */
	boolean fullRestore;

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

	/** Prayer regeneration potion (special over-time behaviour): 1 prayer every 12 ticks, 792 ticks per dose. */
	public static RestoreItem prayerRegen()
	{
		return prayerRegen(1, 12, 792);
	}

	/**
	 * Item whose only effect is prayer regeneration over time
	 * (e.g. Dull ancient medal: 8 prayer every 6 ticks for 120 ticks).
	 * The overlay shows "amount/intervalTicks t"; an infobox tracks the countdown.
	 */
	public static RestoreItem prayerRegen(int amount, int intervalTicks, int durationTicks)
	{
		return RestoreItem.builder()
			.prayerRestoreType(PrayerRestoreType.PRAYER_REGEN)
			.prayerRegenAmount(amount)
			.prayerRegenTicks(intervalTicks)
			.prayerRegenDuration(durationTicks)
			.build();
	}

	/** Item that fully restores both Hitpoints and Prayer (e.g. Jar of congealed blood). */
	public static RestoreItem fullRestore()
	{
		return RestoreItem.builder().fullRestore(true).build();
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
		return instantHp > 0 || dynamicHpType != null || fullRestore;
	}

	public boolean hasDelayedHeal()
	{
		return delayedHp > 0;
	}

	public boolean hasPrayerRestore()
	{
		return prayerRestoreType != null || flatPrayerRestore > 0 || fullRestore;
	}

	public boolean isPrayerRegen()
	{
		return prayerRestoreType == PrayerRestoreType.PRAYER_REGEN;
	}

	/**
	 * True if consuming this item starts a prayer-regeneration-over-time effect.
	 * Covers pure regen items (Prayer regeneration potion, Dull ancient medal) as
	 * well as items that combine an instant restore with regen (Foul chunky potion).
	 */
	public boolean hasPrayerRegenEffect()
	{
		return prayerRegenAmount > 0;
	}
}
