# Inventory Restore Values

A RuneLite plugin that displays HP and prayer restore amounts directly on inventory item icons, so you can see at a glance how much each item will restore without checking the wiki.

## Features

- **HP overlay** — shows the heal value on food items (green by default)
- **Prayer overlay** — shows the prayer restore value on potion items (blue by default), calculated from your real prayer level
- **Holy Wrench / Ring of the Gods (i) detection** — automatically applies the correct bonus multiplier when the item is in your inventory or worn
- **Dynamic formulas** — Anglerfish and Saradomin brew show the value for your current Hitpoints level
- **Two-part food** — hunter meats show `instant/delayed` (e.g. `4/4`) and display an infobox countdown after consumption
- **Prayer Regeneration Potion** — shows `1/12t` on the overlay and an infobox tracking time until the next prayer tick
- **First Item Only** — optionally show the overlay only on the first occurrence of each unique item type in the inventory
- **Minimum HP threshold** — optionally hide overlays on food that heals below a configurable amount
- Skips noted items

## Configuration

| Setting | Default | Description |
|---|---|---|
| Font Size | 11 | Text size (8–24) |
| Text Position | Bottom Right | Where on the icon to draw the value |
| HP Restore Colour | Green | Colour for HP values |
| Prayer Restore Colour | Blue | Colour for prayer values |
| Show HP Overlay | On | Toggle HP overlay |
| Minimum HP Threshold | Off | Hide food below N HP |
| Show Prayer Overlay | On | Toggle prayer overlay |
| First Item Only | Off | One overlay per unique item type |

## Supported Items

**Prayer potions:** Prayer potion, Super restore, Sanfew serum, Ancient brew, Forgotten brew, Blighted super restore, Prayer regeneration potion

**Food:** All standard fish, meats, breads, cakes, pizzas, pies, baked potatoes, hunter meats, Saradomin brew, Anglerfish, and more
