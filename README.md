# Inventory Restore Values

A RuneLite plugin that displays HP and prayer restore amounts directly on inventory item icons, so you can see at a glance how much each item will restore without checking the wiki.

## Features

### HP Overlay
- Shows the heal value on food items in your chosen colour (green by default)
- **Dynamic formulas** — Anglerfish, Saradomin brew, Xeric's aid, Nectar, Haddock, Cooked bream, and Cooked moss lizard show the correct value for your current stats
- **Two-part food** — hunter meats show `instant/delayed` (e.g. `4/4`), or the combined total if the *Show Combined Heal* option is enabled; an infobox countdown appears after consumption
- **Overheal indicator** — the value turns red when consuming the item would exceed your effective HP cap

### Prayer Overlay
- Shows the prayer restore value on potions in your chosen colour (blue by default), calculated from your real prayer level
- **Bonus detection** — automatically applies the correct bonus multiplier when you have a Holy Wrench in your inventory, or a Ring of the Gods (i) or Prayer cape worn
- **Prayer Regeneration Potion** — shows `1/12t` on the overlay and an infobox tracking the countdown to the next prayer tick after consumption
- **Overspill indicator** — the value turns red when consuming the item would exceed your max prayer

### Filtering
- **First Item Only** — show the overlay only on the first occurrence of each item type in the inventory (HP and prayer controlled separately)
- **Last Item Only** — show the overlay only on the last occurrence of each item type in the inventory (HP and prayer controlled separately)
- **Minimum HP threshold** — optionally hide the HP overlay on food that heals below a configurable amount
- Noted items are always skipped

## Configuration

Settings are split into three sections in the plugin panel.

### Display

| Setting | Default | Description |
|---|---|---|
| Font Size | 11 | Text size (8–24) |
| Text Position | Bottom Right | Where on the icon to draw the value (Top Left, Top Right, Bottom Left, Bottom Right, Centre) |
| HP Restore Colour | Green | Colour used for HP restore values |
| Prayer Restore Colour | Blue | Colour used for prayer restore values |

### Food Filter

| Setting | Default | Description |
|---|---|---|
| Show HP Overlay | On | Toggle the HP restore overlay |
| Show Combined Heal | Off | For two-part food, show the total heal (instant + delayed) instead of `instant/delayed` |
| Minimum HP Threshold | Off | Only show the overlay on food healing at least N HP |
| Minimum HP to Show | 10 | Threshold value (1–30); requires *Minimum HP Threshold* to be enabled |
| First Item Only | Off | Show the HP overlay only on the first of each item type |
| Last Item Only | Off | Show the HP overlay only on the last of each item type |

### Prayer

| Setting | Default | Description |
|---|---|---|
| Show Prayer Overlay | On | Toggle the prayer restore overlay |
| First Item Only | Off | Show the prayer overlay only on the first of each potion type |
| Last Item Only | Off | Show the prayer overlay only on the last of each potion type |

## Supported Items

### Prayer Potions
Prayer potion, Super restore, Sanfew serum, Ancient brew, Forgotten brew, Blighted super restore, Prayer regeneration potion

### Herblore Mixes (Barbarian Training)
Prayer mix, Super restore mix, Zamorak mix

### Standard Fish
Anchovies, Shrimps, Sardine, Herring, Mackerel, Giant carp, Trout, Cod, Pike, Salmon, Tuna, Lobster, Bass, Swordfish, Monkfish, Karambwan, Shark, Sea turtle, Manta ray, Dark crab, and more including Barbarian fishing catches (Roe, Caviar) and cave-dwelling fish

### Hunter Meats (Two-part Healing)
Wild kebbit, Larupia, Barb-tailed kebbit, Graahk, Kyatt, Pyre fox, Sunlight antelope, Dashing kebbit, Moonlight antelope

### Dynamic HP Food
Anglerfish and Saradomin brew — values are calculated from your current Hitpoints level

### General Food
Meats, breads, fruit and vegetables, baked potatoes, cakes, pizzas, pies, gnome cooking (crunchies, battas, bowls, cocktails), stews, soups, ales, wines, and a wide range of other food items

### Varlamore
Varlamore wines

### Sailing
Giant krill, Jumbo squid, Swordtip squid, Haddock (dynamic formula), Yellowfin, Halibut, Bluefin, Marlin, and

### Chambers of Xeric
Xeric's aid (−/regular/+), Revitalisation potion (−/regular/+), Prayer enhance (−/regular/+)

### Tombs of Amascut
Nectar, Tears of Elidinis, Honey locust

### Moons of Peril
Moonlight potion, Moonlight Moth jar, Moonlight Moth Mix, Cooked bream (dynamic), Cooked moss lizard (dynamic), Eclipse red, Moon-lite, Sun-shine
