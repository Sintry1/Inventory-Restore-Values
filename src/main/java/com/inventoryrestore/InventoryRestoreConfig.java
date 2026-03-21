package com.inventoryrestore;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(InventoryRestoreConfig.GROUP)
public interface InventoryRestoreConfig extends Config
{
	String GROUP = "inventoryrestore";

	// -------------------------------------------------------------------------
	// Display section
	// -------------------------------------------------------------------------

	@ConfigSection(
		name = "Display",
		description = "Controls how the overlay text looks",
		position = 0
	)
	String displaySection = "display";

	@ConfigItem(
		keyName = "overlayFontSize",
		name = "Font Size",
		description = "Size of the restore value text drawn on inventory icons",
		section = displaySection,
		position = 0
	)
	@Range(min = 8, max = 24)
	default int overlayFontSize()
	{
		return 11;
	}

	@ConfigItem(
		keyName = "overlayPosition",
		name = "Text Position",
		description = "Where on the item icon to draw the restore value",
		section = displaySection,
		position = 1
	)
	default OverlayTextPosition overlayPosition()
	{
		return OverlayTextPosition.BOTTOM_RIGHT;
	}

	@Alpha
	@ConfigItem(
		keyName = "hpColor",
		name = "HP Restore Colour",
		description = "Colour used for HP restore values",
		section = displaySection,
		position = 2
	)
	default Color hpColor()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
		keyName = "prayerColor",
		name = "Prayer Restore Colour",
		description = "Colour used for prayer restore values",
		section = displaySection,
		position = 3
	)
	default Color prayerColor()
	{
		return new Color(80, 160, 255);
	}

	// -------------------------------------------------------------------------
	// Food filter section
	// -------------------------------------------------------------------------

	@ConfigSection(
		name = "Food Filter",
		description = "Controls which food items show an overlay",
		position = 1
	)
	String foodSection = "food";

	@ConfigItem(
		keyName = "showHpOverlay",
		name = "Show HP Overlay",
		description = "Show the HP restore overlay on food items",
		section = foodSection,
		position = 0
	)
	default boolean showHpOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCombinedHeal",
		name = "Show Combined Heal",
		description = "For two-part food (e.g. hunter meats), show the total heal (instant + delayed) instead of 'instant/delayed'",
		section = foodSection,
		position = 1
	)
	default boolean showCombinedHeal()
	{
		return false;
	}

	@ConfigItem(
		keyName = "enableMinHpThreshold",
		name = "Minimum HP Threshold",
		description = "Only show the HP overlay on food that heals at least this much HP",
		section = foodSection,
		position = 2
	)
	default boolean enableMinHpThreshold()
	{
		return false;
	}

	@ConfigItem(
		keyName = "minHpThreshold",
		name = "Minimum HP to Show",
		description = "Food healing below this value will not show an overlay (requires threshold toggle)",
		section = foodSection,
		position = 3
	)
	@Range(min = 1, max = 30)
	default int minHpThreshold()
	{
		return 10;
	}

	// -------------------------------------------------------------------------
	// Prayer section
	// -------------------------------------------------------------------------

	@ConfigSection(
		name = "Prayer",
		description = "Controls prayer restore overlays",
		position = 2
	)
	String prayerSection = "prayer";

	@ConfigItem(
		keyName = "showPrayerOverlay",
		name = "Show Prayer Overlay",
		description = "Show the prayer restore overlay on potion items",
		section = prayerSection,
		position = 0
	)
	default boolean showPrayerOverlay()
	{
		return true;
	}

	// -------------------------------------------------------------------------
	// General section
	// -------------------------------------------------------------------------

	@ConfigSection(
		name = "General",
		description = "General display options",
		position = 3
	)
	String generalSection = "general";

	@ConfigItem(
		keyName = "firstItemOnly",
		name = "First Item Only",
		description = "Only show the overlay on the first HP-restoring and first prayer-restoring item in the inventory (left to right, top to bottom)",
		section = generalSection,
		position = 0
	)
	default boolean firstItemOnly()
	{
		return false;
	}
}
