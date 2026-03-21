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

		// Tracks item IDs already rendered in first-item-only mode
		final java.util.Set<Integer> seenHpItems = new java.util.HashSet<>();
		final java.util.Set<Integer> seenPrayerItems = new java.util.HashSet<>();

		for (Widget item : children)
		{
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

			if (config.firstItemOnly())
			{
				if (showHp)
				{
					showHp = seenHpItems.add(item.getItemId());
				}
				if (showPrayer)
				{
					showPrayer = seenPrayerItems.add(item.getItemId());
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
			else
			{
				prayerText = String.valueOf(computePrayer(restoreItem));
			}
		}

		if (hpText == null && prayerText == null)
		{
			return;
		}

		int lineHeight = fm.getHeight();

		if (hpText != null && prayerText != null)
		{
			// Stack both values: for bottom positions, HP is on top (smaller offset);
			// for top positions the vertical offset pushes prayer below HP.
			boolean isTopPosition = config.overlayPosition() == OverlayTextPosition.TOP_LEFT
				|| config.overlayPosition() == OverlayTextPosition.TOP_RIGHT;

			if (isTopPosition)
			{
				drawText(graphics, fm, bounds, hpText, config.hpColor(), 0);
				drawText(graphics, fm, bounds, prayerText, config.prayerColor(), lineHeight);
			}
			else
			{
				// Bottom / center: prayer text is the lower line (larger offset)
				drawText(graphics, fm, bounds, prayerText, config.prayerColor(), 0);
				drawText(graphics, fm, bounds, hpText, config.hpColor(), lineHeight);
			}
		}
		else if (hpText != null)
		{
			drawText(graphics, fm, bounds, hpText, config.hpColor(), 0);
		}
		else
		{
			drawText(graphics, fm, bounds, prayerText, config.prayerColor(), 0);
		}
	}

	/**
	 * Draws text at the configured position inside the item's bounding box.
	 *
	 * @param verticalOffset extra pixels to shift the text away from the anchor edge,
	 *                       used to stack two lines without them overlapping.
	 */
	private void drawText(Graphics2D graphics, FontMetrics fm, Rectangle bounds,
		String text, Color color, int verticalOffset)
	{
		int textWidth = fm.stringWidth(text);
		int ascent = fm.getAscent();

		int x;
		int y;

		switch (config.overlayPosition())
		{
			case TOP_LEFT:
				x = bounds.x + 1;
				y = bounds.y + ascent + verticalOffset;
				break;
			case TOP_RIGHT:
				x = bounds.x + bounds.width - textWidth - 1;
				y = bounds.y + ascent + verticalOffset;
				break;
			case BOTTOM_LEFT:
				x = bounds.x + 1;
				y = bounds.y + bounds.height - 2 - verticalOffset;
				break;
			case BOTTOM_RIGHT:
				x = bounds.x + bounds.width - textWidth - 1;
				y = bounds.y + bounds.height - 2 - verticalOffset;
				break;
			case CENTER:
			default:
				x = bounds.x + (bounds.width - textWidth) / 2;
				y = bounds.y + (bounds.height + ascent) / 2 + verticalOffset;
				break;
		}

		// Drop shadow for readability over any item background
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + 1, y + 1);

		graphics.setColor(color);
		graphics.drawString(text, x, y);
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
			default:
				return 0;
		}
	}
}
