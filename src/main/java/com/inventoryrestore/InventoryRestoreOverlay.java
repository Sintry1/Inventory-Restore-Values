package com.inventoryrestore;

import com.inventoryrestore.data.DynamicHpType;
import com.inventoryrestore.data.RestoreItem;
import com.inventoryrestore.data.RestoreItemDatabase;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class InventoryRestoreOverlay extends Overlay
{
	// OSRS inventory widget: group 149, child 0
	private static final int INVENTORY_GROUP_ID = 149;
	private static final int INVENTORY_CHILD_ID = 0;

	private final Client client;
	private final InventoryRestoreConfig config;
	private final InventoryRestorePlugin plugin;
	private final ItemManager itemManager;

	@Inject
	InventoryRestoreOverlay(Client client, InventoryRestoreConfig config,
		InventoryRestorePlugin plugin, ItemManager itemManager)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		this.itemManager = itemManager;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Widget inventoryWidget = client.getWidget(INVENTORY_GROUP_ID, INVENTORY_CHILD_ID);
		if (inventoryWidget == null || inventoryWidget.isHidden())
		{
			return null;
		}

		Widget[] children = inventoryWidget.getDynamicChildren();
		if (children == null)
		{
			return null;
		}

		Font font = FontManager.getRunescapeSmallFont();
		if (config.overlayFontSize() != 11)
		{
			font = font.deriveFont((float) config.overlayFontSize());
		}
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();

		// Pre-scan for last-item-only mode: find the last widget index for each group ID
		final Map<Integer, Integer> lastHpIndex = new HashMap<>();
		final Map<Integer, Integer> lastPrayerIndex = new HashMap<>();
		if (config.lastHpItemOnly() || config.lastPrayerItemOnly())
		{
			for (int i = 0; i < children.length; i++)
			{
				Widget item = children[i];
				if (item.isHidden() || item.getItemId() <= 0)
				{
					continue;
				}
				if (itemManager.getItemComposition(item.getItemId()).getNote() != -1)
				{
					continue;
				}
				RestoreItem restoreItem = RestoreItemDatabase.get(item.getItemId());
				if (restoreItem == null)
				{
					continue;
				}
				int groupId = RestoreItemDatabase.getGroupId(item.getItemId());
				if (config.showHpOverlay() && config.lastHpItemOnly() && restoreItem.hasInstantHp())
				{
					lastHpIndex.put(groupId, i);
				}
				if (config.showPrayerOverlay() && config.lastPrayerItemOnly() && restoreItem.hasPrayerRestore())
				{
					lastPrayerIndex.put(groupId, i);
				}
			}
		}

		// Tracks item IDs already rendered in first-item-only mode
		final Set<Integer> seenHpItems = new HashSet<>();
		final Set<Integer> seenPrayerItems = new HashSet<>();

		for (int i = 0; i < children.length; i++)
		{
			Widget item = children[i];
			if (item.isHidden() || item.getItemId() <= 0)
			{
				continue;
			}

			// Skip noted items
			if (itemManager.getItemComposition(item.getItemId()).getNote() != -1)
			{
				continue;
			}

			RestoreItem restoreItem = RestoreItemDatabase.get(item.getItemId());
			if (restoreItem == null)
			{
				continue;
			}

			boolean showHp = config.showHpOverlay();
			boolean showPrayer = config.showPrayerOverlay();

			if (showHp || showPrayer)
			{
				int groupId = -1; // lazily resolved below

				if (config.firstHpItemOnly() && showHp)
				{
					groupId = RestoreItemDatabase.getGroupId(item.getItemId());
					showHp = seenHpItems.add(groupId);
				}
				if (config.firstPrayerItemOnly() && showPrayer)
				{
					if (groupId == -1)
					{
						groupId = RestoreItemDatabase.getGroupId(item.getItemId());
					}
					showPrayer = seenPrayerItems.add(groupId);
				}

				if (config.lastHpItemOnly() && showHp)
				{
					if (groupId == -1)
					{
						groupId = RestoreItemDatabase.getGroupId(item.getItemId());
					}
					showHp = lastHpIndex.getOrDefault(groupId, -1) == i;
				}
				if (config.lastPrayerItemOnly() && showPrayer)
				{
					if (groupId == -1)
					{
						groupId = RestoreItemDatabase.getGroupId(item.getItemId());
					}
					showPrayer = lastPrayerIndex.getOrDefault(groupId, -1) == i;
				}
			}

			if (!showHp && !showPrayer)
			{
				continue;
			}

			renderItemOverlay(graphics, fm, item.getBounds(), restoreItem, showHp, showPrayer);
		}

		return null;
	}

	private void renderItemOverlay(Graphics2D graphics, FontMetrics fm,
		Rectangle bounds, RestoreItem restoreItem, boolean showHp, boolean showPrayer)
	{
		String hpText = null;
		String prayerText = null;

		// --- HP text ---
		if (showHp && restoreItem.hasInstantHp())
		{
			int hp = computeHp(restoreItem);

			if (!config.enableMinHpThreshold() || hp >= config.minHpThreshold())
			{
				if (restoreItem.hasDelayedHeal())
				{
					if (config.showCombinedHeal())
					{
						hpText = String.valueOf(hp + restoreItem.getDelayedHp());
					}
					else
					{
						// Two-part food (hunter meats): show "instant/delayed"
						hpText = hp + "/" + restoreItem.getDelayedHp();
					}
				}
				else
				{
					hpText = String.valueOf(hp);
				}
			}
		}

		// --- Prayer text ---
		if (showPrayer && restoreItem.hasPrayerRestore())
		{
			if (restoreItem.isPrayerRegen())
			{
				// Prayer Regen Potion: show restore amount / tick interval
				prayerText = "1/12t";
			}
			else if (restoreItem.getPrayerRestoreType() != null)
			{
				int prayerRestore = computePrayer(restoreItem);
				if (prayerRestore > 0)
				{
					prayerText = String.valueOf(prayerRestore);
				}
			}
			else
			{
				// Flat prayer restore (not formula-based, e.g. Jangerberries, Moonlight Moth)
				int flatPrayer = restoreItem.getFlatPrayerRestore();
				if (flatPrayer > 0)
				{
					prayerText = String.valueOf(flatPrayer);
				}
			}
		}

		if (hpText == null && prayerText == null)
		{
			return;
		}

		Color hpColor = resolveHpColor(restoreItem);
		Color prayerColor = resolvePrayerColor(restoreItem);

		if (hpText != null && prayerText != null)
		{
			// Show as "HP/Prayer" on a single line with individually-coloured values.
			// Each value turns red independently when consuming would exceed its cap.
			drawSplitText(graphics, fm, bounds, hpText, hpColor, prayerText, prayerColor);
		}
		else if (hpText != null)
		{
			drawText(graphics, fm, bounds, hpText, hpColor);
		}
		else
		{
			drawText(graphics, fm, bounds, prayerText, prayerColor);
		}
	}

	/**
	 * Returns {@link Color#RED} if consuming the item would exceed the effective HP cap,
	 * otherwise returns the configured HP colour.
	 *
	 * <p>For items that can overheal (Anglerfish, Saradomin brew) the effective cap is
	 * {@code realHp + healAmount}; for all other food it is simply {@code realHp}.
	 * Two-part food uses the combined instant + delayed heal for this check.
	 */
	private Color resolveHpColor(RestoreItem restoreItem)
	{
		if (!restoreItem.hasInstantHp())
		{
			return config.hpColor();
		}

		int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
		int realHp = client.getRealSkillLevel(Skill.HITPOINTS);
		int heal = computeHp(restoreItem);

		if (restoreItem.hasDelayedHeal())
		{
			heal += restoreItem.getDelayedHp();
		}

		// Overheal items boost above real level; their cap is realHp + heal.
		// For all other food the cap is just realHp.
		int effectiveMax = restoreItem.getDynamicHpType() != null ? realHp + heal : realHp;

		return currentHp + heal > effectiveMax ? Color.RED : config.hpColor();
	}

	/**
	 * Returns {@link Color#RED} if consuming the item would exceed max prayer,
	 * otherwise returns the configured prayer colour.
	 * Prayer regen potions are excluded (their restore is gradual, not instant).
	 */
	private Color resolvePrayerColor(RestoreItem restoreItem)
	{
		if (!restoreItem.hasPrayerRestore() || restoreItem.isPrayerRegen())
		{
			return config.prayerColor();
		}

		int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
		int realPrayer = client.getRealSkillLevel(Skill.PRAYER);
		int restore = restoreItem.getPrayerRestoreType() != null
			? computePrayer(restoreItem)
			: restoreItem.getFlatPrayerRestore();

		return currentPrayer + restore > realPrayer ? Color.RED : config.prayerColor();
	}

	/**
	 * Draws text at the configured position inside the item's bounding box.
	 */
	private void drawText(Graphics2D graphics, FontMetrics fm, Rectangle bounds,
		String text, Color color)
	{
		int textWidth = fm.stringWidth(text);
		int ascent = fm.getAscent();

		int x;
		int y;

		switch (config.overlayPosition())
		{
			case TOP_LEFT:
				x = bounds.x + 1;
				y = bounds.y + ascent;
				break;
			case TOP_RIGHT:
				x = bounds.x + bounds.width - textWidth - 1;
				y = bounds.y + ascent;
				break;
			case BOTTOM_LEFT:
				x = bounds.x + 1;
				y = bounds.y + bounds.height - 2;
				break;
			case BOTTOM_RIGHT:
				x = bounds.x + bounds.width - textWidth - 1;
				y = bounds.y + bounds.height - 2;
				break;
			case CENTER:
			default:
				x = bounds.x + (bounds.width - textWidth) / 2;
				y = bounds.y + (bounds.height + ascent) / 2;
				break;
		}

		// Drop shadow for readability over any item background
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + 1, y + 1);

		graphics.setColor(color);
		graphics.drawString(text, x, y);
	}

	/**
	 * Draws two values separated by "/" on a single line, each in its own colour.
	 * Used for combo items (e.g. herblore mixes) that restore both HP and prayer.
	 * The full "left/right" string is positioned according to the configured text position.
	 */
	private void drawSplitText(Graphics2D graphics, FontMetrics fm, Rectangle bounds,
		String leftText, Color leftColor, String rightText, Color rightColor)
	{
		final String sep = "/";
		int leftWidth = fm.stringWidth(leftText);
		int sepWidth = fm.stringWidth(sep);
		int totalWidth = leftWidth + sepWidth + fm.stringWidth(rightText);
		int ascent = fm.getAscent();

		int x;
		int y;

		switch (config.overlayPosition())
		{
			case TOP_LEFT:
				x = bounds.x + 1;
				y = bounds.y + ascent;
				break;
			case TOP_RIGHT:
				x = bounds.x + bounds.width - totalWidth - 1;
				y = bounds.y + ascent;
				break;
			case BOTTOM_LEFT:
				x = bounds.x + 1;
				y = bounds.y + bounds.height - 2;
				break;
			case BOTTOM_RIGHT:
				x = bounds.x + bounds.width - totalWidth - 1;
				y = bounds.y + bounds.height - 2;
				break;
			case CENTER:
			default:
				x = bounds.x + (bounds.width - totalWidth) / 2;
				y = bounds.y + (bounds.height + ascent) / 2;
				break;
		}

		// Drop shadows for all three segments
		graphics.setColor(Color.BLACK);
		graphics.drawString(leftText, x + 1, y + 1);
		graphics.drawString(sep, x + leftWidth + 1, y + 1);
		graphics.drawString(rightText, x + leftWidth + sepWidth + 1, y + 1);

		// Coloured segments
		graphics.setColor(leftColor);
		graphics.drawString(leftText, x, y);
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.drawString(sep, x + leftWidth, y);
		graphics.setColor(rightColor);
		graphics.drawString(rightText, x + leftWidth + sepWidth, y);
	}

	// ------------------------------------------------------------------
	// Stat computations
	// ------------------------------------------------------------------

	private int computeHp(RestoreItem item)
	{
		if (item.getDynamicHpType() == DynamicHpType.ANGLERFISH)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Formula from OSRS wiki: floor(hp/10) + 2*floor(hp/25) + 5*floor(hp/93) + 2
			return (hp / 10) + 2 * (hp / 25) + 5 * (hp / 93) + 2;
		}
		if (item.getDynamicHpType() == DynamicHpType.SARADOMIN_BREW)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// floor(hp * 15 / 100) + 2
			return (hp * 15 / 100) + 2;
		}
		// Chambers of Xeric — Xeric's aid tiers
		// Strong formula sourced from RuneLite itemstats; weak/regular are estimates.
		if (item.getDynamicHpType() == DynamicHpType.XERIC_AID_WEAK)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Formula sourced from OSRS wiki: floor(hp * 5 / 100) + 1
			return (hp * 5 / 100) + 1;
		}
		if (item.getDynamicHpType() == DynamicHpType.XERIC_AID)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Formula sourced from OSRS wiki: floor(hp * 10 / 100) + 2
			return (hp * 10 / 100) + 2;
		}
		if (item.getDynamicHpType() == DynamicHpType.XERIC_AID_STRONG)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Sourced from RuneLite itemstats: SaradominBrew(0.15, …, 5, …)
			return (hp * 15 / 100) + 5;
		}
		// Tombs of Amascut — Nectar
		if (item.getDynamicHpType() == DynamicHpType.NECTAR)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Sourced from RuneLite itemstats: perc(.15, 3)
			return (hp * 15 / 100) + 3;
		}
		// Moons of Peril
		if (item.getDynamicHpType() == DynamicHpType.COOKED_BREAM)
		{
			int cooking = client.getBoostedSkillLevel(Skill.COOKING);
			int fishing = client.getBoostedSkillLevel(Skill.FISHING);
			return Math.max(7, Math.min(cooking / 3, fishing / 3));
		}
		if (item.getDynamicHpType() == DynamicHpType.COOKED_MOSS_LIZARD)
		{
			int cooking = client.getBoostedSkillLevel(Skill.COOKING);
			int hunter = client.getBoostedSkillLevel(Skill.HUNTER);
			return Math.min(cooking / 3, hunter / 2);
		}
		// Varlamore — Haddock
		if (item.getDynamicHpType() == DynamicHpType.HADDOCK)
		{
			int hp = client.getRealSkillLevel(Skill.HITPOINTS);
			// Formula: min(18, floor(hp * 20 / 100))
			return Math.min(18, hp * 20 / 100);
		}
		// FIXED_OVERHEAL falls through: instantHp holds the fixed amount.
		return item.getInstantHp();
	}

	/**
	 * Computes prayer points restored by one dose, accounting for the
	 * Holy Wrench / Ring of the Gods (i) bonus.
	 *
	 * <p>The bonus changes the percentage multiplier rather than adding a flat amount:
	 * <ul>
	 *   <li>Prayer Potion:  25% → 27%</li>
	 *   <li>Super Restore:  25% → 27%</li>
	 *   <li>Sanfew Serum:   30% → 32%</li>
	 *   <li>Ancient Brew:   10% — unaffected by the wrench bonus</li>
	 * </ul>
	 */
	private int computePrayer(RestoreItem item)
	{
		if (item.getPrayerRestoreType() == null)
		{
			return 0;
		}

		int prayer = client.getRealSkillLevel(Skill.PRAYER);
		boolean bonus = plugin.hasPrayerBonus();

		switch (item.getPrayerRestoreType())
		{
			case PRAYER_POTION:
				return prayer * (bonus ? 27 : 25) / 100 + 7;
			case SUPER_RESTORE:
				return prayer * (bonus ? 27 : 25) / 100 + 8;
			case SANFEW_SERUM:
				return prayer * (bonus ? 32 : 30) / 100 + 4;
			case ANCIENT_BREW:
				// Ancient Brew and Forgotten Brew are not affected by the wrench bonus
				return prayer / 10 + 2;
			case REVITALISATION_STRONG:
				// Revitalisation potion (+) — sourced from RuneLite itemstats: SuperRestore(.30, 11)
				return prayer * (bonus ? 32 : 30) / 100 + 11;
			case TEARS_OF_ELIDINIS:
				// Tears of Elidinis — sourced from RuneLite itemstats: perc(.25, 10)
				return prayer * (bonus ? 27 : 25) / 100 + 10;
			case MOONLIGHT_POTION:
			{
				// Moonlight Potion — sourced from RuneLite itemstats (MoonlightPotion.java)
				// Formula: floor(max(prayer * 0.25, herblore * 0.3)) + 7, requires Herblore >= 38
				// Not affected by Holy Wrench / Ring of the Gods (i)
				int herb = client.getBoostedSkillLevel(Skill.HERBLORE);
				return herb < 38 ? 0 : (int) Math.max(prayer * 0.25, herb * 0.3) + 7;
			}
			case PRAYER_REGEN:
				// Handled upstream via isPrayerRegen() / infobox; no instant restore to compute
				return 0;
			default:
				return 0;
		}
	}
}
