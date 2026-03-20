package com.inventoryrestore;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

/**
 * Infobox shown while a Prayer regeneration potion effect is active.
 * Displays a countdown to the next +1 prayer tick.
 *
 * <p>The prayer regeneration potion restores 1 prayer point every 12 game ticks
 * (7.2 seconds), for a total of 66 prayer per dose (792 ticks total duration).
 */
public class PrayerRegenInfoBox extends InfoBox
{
	/** Ticks between each +1 prayer restore. */
	private static final int TICKS_PER_RESTORE = 12;

	/** Total ticks one dose lasts (66 restores × 12 ticks). */
	public static final int TICKS_PER_DOSE = 792;

	/** Ticks until the next prayer point fires. Counts down from 12 to 0. */
	private int ticksUntilNext;

	/** Total ticks remaining for the entire effect. */
	private int totalTicksRemaining;

	private final InventoryRestoreConfig config;

	public PrayerRegenInfoBox(BufferedImage image, Plugin plugin, InventoryRestoreConfig config)
	{
		super(image, plugin);
		this.ticksUntilNext = TICKS_PER_RESTORE;
		this.totalTicksRemaining = TICKS_PER_DOSE;
		this.config = config;
	}

	/**
	 * Add another dose worth of ticks (used when player drinks a second dose while
	 * the effect is already active).
	 */
	public void addDose()
	{
		totalTicksRemaining += TICKS_PER_DOSE;
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
			ticksUntilNext = TICKS_PER_RESTORE;
		}
	}

	public boolean isExpired()
	{
		return totalTicksRemaining <= 0;
	}

	/** Prayer points still to be restored for the remaining duration. */
	public int getPrayerRemaining()
	{
		return (int) Math.ceil(totalTicksRemaining / (double) TICKS_PER_RESTORE);
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
		return String.format("+1 Prayer in %.1fs | ~%d prayer remaining", seconds, getPrayerRemaining());
	}
}
