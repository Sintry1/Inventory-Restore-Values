package com.inventoryrestore;

import com.google.inject.Provides;
import com.inventoryrestore.data.RestoreItem;
import com.inventoryrestore.data.RestoreItemDatabase;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Inventory Restore Values",
	description = "Shows HP and prayer restore amounts on inventory item icons",
	tags = {"inventory", "prayer", "food", "restore", "overlay", "potion", "heal", "dose"}
)
public class InventoryRestorePlugin extends Plugin
{
	// ------------------------------------------------------------------
	// Prayer-bonus item IDs
	// Holy Wrench (inventory), Ring of the Gods (i) (worn), and Prayer Cape (worn)
	// all grant the SAME bonus and do NOT stack with each other.
	// ------------------------------------------------------------------

	private static final int HOLY_WRENCH = 6714;

	// Ring of the Gods (i) has three variants depending on imbuing method
	private static final int RING_OF_THE_GODS_I_NMZ = 13202;
	private static final int RING_OF_THE_GODS_I_SW = 25252;
	private static final int RING_OF_THE_GODS_I_EA = 26764;

	// TODO: confirm Prayer cape (9675) and Prayer cape(t) (9677) IDs before enabling
	// private static final int PRAYER_CAPE   = 9675;
	// private static final int PRAYER_CAPE_T = 9677;

	// ------------------------------------------------------------------
	// Injected services
	// ------------------------------------------------------------------

	@Inject
	private Client client;

	@Inject
	private InventoryRestoreConfig config;

	@Inject
	private InventoryRestoreOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private ItemManager itemManager;

	// ------------------------------------------------------------------
	// State
	// ------------------------------------------------------------------

	/** Item ID the player is about to eat/drink (detected via MenuOptionClicked). */
	private int pendingConsumptionId = -1;

	/** Active delayed heal infobox, or null if none. */
	private DelayedHealInfoBox delayedHealInfoBox;

	/** Active prayer regen infobox, or null if none. */
	private PrayerRegenInfoBox prayerRegenInfoBox;

	// ------------------------------------------------------------------
	// Plugin lifecycle
	// ------------------------------------------------------------------

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		removeDelayedHealInfoBox();
		removePrayerRegenInfoBox();
		pendingConsumptionId = -1;
	}

	// ------------------------------------------------------------------
	// Event handlers
	// ------------------------------------------------------------------

	/**
	 * Capture which item the player intends to eat or drink before the inventory
	 * change fires. This is more reliable than diffing inventory states.
	 */
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		String option = event.getMenuOption();
		if ("Eat".equalsIgnoreCase(option) || "Drink".equalsIgnoreCase(option))
		{
			pendingConsumptionId = event.getItemId();
		}
	}

	/** Confirm consumption once the inventory changes, and start any timers. */
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}
		if (pendingConsumptionId == -1)
		{
			return;
		}

		int consumedId = pendingConsumptionId;
		pendingConsumptionId = -1;

		RestoreItem item = RestoreItemDatabase.get(consumedId);
		if (item == null)
		{
			return;
		}

		// Delayed heal (hunter meats) — eating a second overwrites the first
		if (item.hasDelayedHeal())
		{
			startDelayedHealTimer(consumedId, item);
		}

		// Prayer regeneration potion
		if (item.isPrayerRegen())
		{
			if (prayerRegenInfoBox != null && !prayerRegenInfoBox.isExpired())
			{
				prayerRegenInfoBox.addDose();
			}
			else
			{
				startPrayerRegenTimer(consumedId);
			}
		}
	}

	/** Tick down active timers every game tick. */
	@Subscribe
	public void onGameTick(GameTick event)
	{
		tickDelayedHeal();
		tickPrayerRegen();
	}

	// ------------------------------------------------------------------
	// Timer management
	// ------------------------------------------------------------------

	private void startDelayedHealTimer(int itemId, RestoreItem item)
	{
		removeDelayedHealInfoBox();
		BufferedImage image = itemManager.getImage(itemId);
		delayedHealInfoBox = new DelayedHealInfoBox(image, this, item.getDelayedHp(),
			item.getDelayTicks(), config);
		infoBoxManager.addInfoBox(delayedHealInfoBox);
	}

	private void startPrayerRegenTimer(int itemId)
	{
		removePrayerRegenInfoBox();
		BufferedImage image = itemManager.getImage(itemId);
		prayerRegenInfoBox = new PrayerRegenInfoBox(image, this, config);
		infoBoxManager.addInfoBox(prayerRegenInfoBox);
	}

	private void tickDelayedHeal()
	{
		if (delayedHealInfoBox == null)
		{
			return;
		}
		if (delayedHealInfoBox.tick())
		{
			removeDelayedHealInfoBox();
		}
	}

	private void tickPrayerRegen()
	{
		if (prayerRegenInfoBox == null)
		{
			return;
		}
		prayerRegenInfoBox.tick();
		if (prayerRegenInfoBox.isExpired())
		{
			removePrayerRegenInfoBox();
		}
	}

	private void removeDelayedHealInfoBox()
	{
		if (delayedHealInfoBox != null)
		{
			infoBoxManager.removeInfoBox(delayedHealInfoBox);
			delayedHealInfoBox = null;
		}
	}

	private void removePrayerRegenInfoBox()
	{
		if (prayerRegenInfoBox != null)
		{
			infoBoxManager.removeInfoBox(prayerRegenInfoBox);
			prayerRegenInfoBox = null;
		}
	}

	// ------------------------------------------------------------------
	// Prayer bonus check
	// ------------------------------------------------------------------

	/**
	 * Returns true if the player has an active prayer restoration bonus from any of:
	 * <ul>
	 *   <li>Holy Wrench in inventory</li>
	 *   <li>Ring of the Gods (i) worn (any of the three imbuing variants)</li>
	 * </ul>
	 *
	 * <p>These items all provide the <em>same</em> bonus and do NOT stack.
	 * Any one of them being present is sufficient.
	 *
	 * <p>Effect: changes the prayer potion / super restore multiplier from 25% → 27%,
	 * and the sanfew serum multiplier from 30% → 32%.
	 * Ancient brew and Prayer Regen Potion are NOT affected.
	 */
	public boolean hasPrayerBonus()
	{
		if (hasItemInInventory(HOLY_WRENCH))
		{
			return true;
		}
		return hasItemEquipped(RING_OF_THE_GODS_I_NMZ)
			|| hasItemEquipped(RING_OF_THE_GODS_I_SW)
			|| hasItemEquipped(RING_OF_THE_GODS_I_EA);
		// TODO: add Prayer cape IDs once confirmed
	}

	// ------------------------------------------------------------------
	// Item presence helpers
	// ------------------------------------------------------------------

	private boolean hasItemInInventory(int targetId)
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null)
		{
			return false;
		}
		for (Item item : inventory.getItems())
		{
			if (item.getId() == targetId)
			{
				return true;
			}
		}
		return false;
	}

	private boolean hasItemEquipped(int targetId)
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return false;
		}
		for (Item item : equipment.getItems())
		{
			if (item.getId() == targetId)
			{
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------
	// Config provider
	// ------------------------------------------------------------------

	@Provides
	InventoryRestoreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(InventoryRestoreConfig.class);
	}
}
