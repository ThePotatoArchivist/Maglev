Copper magnetic levitation minecart rails!

![Minecarts floating above maglev rails in a subway](https://cdn.modrinth.com/data/YrqkULLr/images/67f3646138f8fb9e12f1230f5177f5efe8d14f82.png)

Maglev adds several sets of copper rails which allow minecarts to hover in the air.

This mod was based on [this r/minecraftsuggestions reddit post](https://www.reddit.com/r/minecraftsuggestions/comments/tepegh/levitation_rails_an_upgrade_to_minecart_systems/) by u/Creative-Kreature.

[![Made for ModFest: Toybox](https://raw.githubusercontent.com/ModFest/art/aa6c3c7b64552dc8e6d37c0677bbe46edbe9a4c7/badge/svg/toybox/cozy.svg)](https://modfest.net/toybox)

## Additions

### Maglev Rail

![Recipe: 6 copper ingots and 1 iron ingot crafts 6 maglev rails](https://cdn.modrinth.com/data/cached_images/6f375142e34aa3204ffe0edddfedcab6d86f1191.png)

- Allows a minecart to hover up to 15 blocks above the rail
    - The minecart can be made to hover by dropping it from the end of normal rails
- Comes in all waxed and oxidized copper variants

### Powered Maglev Rail

![Recipe: 6 copper ingots, 1 iron ingot, 1 gold ingot, and 1 redstone dust crafts 2 powered maglev rails](https://cdn.modrinth.com/data/cached_images/f5a164b6ab0518870b7c3ef5ba54239e1f97092a.png)

- Allows minecarts to hover just like maglev rails
- Accelerates the minecart just like powered rails
- Comes in all waxed and oxidized copper variants

### Variable Maglev Rail

![Recipe: 6 copper ingots, 1 iron ingot, 1 nether quartz, and 1 redstone dust crafts 1 variable maglev rail](https://cdn.modrinth.com/data/cached_images/38e98e833abb37b42e61704fc55a81bd7251ea4a.png)

- Allows minecarts to hover just like maglev rails
- Changes the hover height of a minecart to its redstone power level (0-15)
- Comes in all waxed and oxidized copper variants

## Mechanics

- While hovering, minecarts have no friction and have no sound
- Minecarts have different max speeds on different oxidized rails:
  | Oxidation | Max Speed |
  | --- | --- |
  | Unoxidized | 1.2 blocks/tick |
  | Exposed | 0.8 blocks/tick |
  | Weathered | 0.4 blocks/tick |
  | Oxidized | 0.1 blocks/tick |
- Minecarts remember which track they're on so rails can cross underneath and they stay on the right track
- You may place blocks between the rail and minecart to have hidden rails
- If a minecart hits a corner (or changes between flat and incline) while moving too quickly, it may derail. To prevent this, the minecart must be slowed to at most weathered speed. This was unintentional but is currently considered a feature.
- All maglev rails can be manually oxidized by right-clicking with a wet sponge

## Notes

- Does not work with experimental minecart mechanics yet, this will be added in the future
