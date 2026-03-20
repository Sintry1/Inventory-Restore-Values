package com.inventoryrestore;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

/**
 * Infobox shown after eating a hunter meat with a delayed second heal.
 * Displays the countdown (in seconds) until the delayed heal fires.
 * Only one instance is active at a time; eating a second item overwrites this one.
 */
public class DelayedHealInfoBox extends InfoBox
{
	private int ticksRemaining;
	private final int healAmount;
	private final InventoryRestoreConfig config;

	public DelayedHealInfoBox(BufferedImage image, Plugin plugin, int healAmount, int delayTicks,
		InventoryRestoreConfig config)
	{
		super(image, plugin);
		this.healAmount = healAmount;
		this.ticksRemaining = delayTicks;
		this.config = config;
	}

	@Override
	public String getText()
	{
		double seconds = ticksRemaining * 0.6;
		return String.format("%.1fs", seconds);
	}

	@Override
	public Color getTextColor()
	{
		return config.hpColor();
	}

	@Override
	public String getTooltip()
	{
		double seconds = ticksRemaining * 0.6;
		return String.format("+%d HP in %.1fs", healAmount, seconds);
	}

	/** Called every game tick by the plugin. Returns true when the heal has fired. */
	public boolean tick()
	{
		if (ticksRemaining > 0)
		{
			ticksRemaining--;
		}
		return ticksRemaining <= 0;
	}

	public int getHealAmount()
	{
		return healAmount;
	}
}
