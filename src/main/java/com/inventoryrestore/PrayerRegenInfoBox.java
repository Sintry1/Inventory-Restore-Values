package com.inventoryrestore;

import com.inventoryrestore.data.RestoreItem;
import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

/**
 * Infobox shown while a prayer-regeneration-over-time effect is active.
 * Displays a countdown to the next prayer restore tick.
 *
 * <p>The regen parameters come from the consumed item:
 * <ul>
 *   <li>Prayer regeneration potion / Prayer enhance: 1 prayer every 12 ticks,
 *       792 ticks per dose (66 prayer total)</li>
 *   <li>Foul chunky potion: same regen as the Prayer regeneration potion</li>
 *   <li>Dull ancient medal: 8 prayer every 6 ticks for 120 ticks (160 prayer total)</li>
 * </ul>
 */
public class PrayerRegenInfoBox extends InfoBox
{
	/** Prayer points restored each interval. */
	private final int restoreAmount;

	/** Ticks between each prayer restore. */
	private final int ticksPerRestore;

	/** Total ticks one dose lasts. */
	private final int ticksPerDose;

	/** Ticks until the next prayer restore fires. */
	private int ticksUntilNext;

	/** Total ticks remaining for the entire effect. */
	private int totalTicksRemaining;

	private final InventoryRestoreConfig config;

	public PrayerRegenInfoBox(BufferedImage image, Plugin plugin, InventoryRestoreConfig config,
		int restoreAmount, int ticksPerRestore, int ticksPerDose)
	{
		super(image, plugin);
		this.restoreAmount = restoreAmount;
		this.ticksPerRestore = ticksPerRestore;
		this.ticksPerDose = ticksPerDose;
		this.ticksUntilNext = ticksPerRestore;
		this.totalTicksRemaining = ticksPerDose;
		this.config = config;
	}

	/**
	 * True if this infobox is tracking the same regen effect as the given item,
	 * so a repeat consumption can extend the timer rather than replace it.
	 */
	public boolean matches(RestoreItem item)
	{
		return item.getPrayerRegenAmount() == restoreAmount
			&& item.getPrayerRegenTicks() == ticksPerRestore
			&& item.getPrayerRegenDuration() == ticksPerDose;
	}

	/**
	 * Add another dose worth of ticks (used when player drinks a second dose while
	 * the effect is already active).
	 */
	public void addDose()
	{
		totalTicksRemaining += ticksPerDose;
	}

	/** Called every game tick. */
	public void tick()
	{
		if (totalTicksRemaining > 0)
		{
			totalTicksRemaining--;
		}
		if (ticksUntilNext > 0)
		{
			ticksUntilNext--;
		}
		if (ticksUntilNext <= 0)
		{
			ticksUntilNext = ticksPerRestore;
		}
	}

	public boolean isExpired()
	{
		return totalTicksRemaining <= 0;
	}

	/** Prayer points still to be restored for the remaining duration. */
	public int getPrayerRemaining()
	{
		return (int) Math.ceil(totalTicksRemaining / (double) ticksPerRestore) * restoreAmount;
	}

	@Override
	public String getText()
	{
		double seconds = ticksUntilNext * 0.6;
		return String.format("%.1fs", seconds);
	}

	@Override
	public Color getTextColor()
	{
		return config.prayerColor();
	}

	@Override
	public String getTooltip()
	{
		double seconds = ticksUntilNext * 0.6;
		return String.format("+%d Prayer in %.1fs | ~%d prayer remaining",
			restoreAmount, seconds, getPrayerRemaining());
	}
}
