[![Made for ModFest: Toybox](https://raw.githubusercontent.com/ModFest/art/aa6c3c7b64552dc8e6d37c0677bbe46edbe9a4c7/badge/svg/toybox/cozy.svg)](https://modfest.net/toybox)

Copper magnetic levitation minecart rails!

Maglev adds several sets of copper rails which allow minecarts to hover in the air.

This mod was based on [this r/minecraftsuggestions reddit post](https://www.reddit.com/r/minecraftsuggestions/comments/tepegh/levitation_rails_an_upgrade_to_minecart_systems/) by u/Creative-Kreature.

## Additions

### Maglev Rail

- Allows a minecart to hover up to 15 blocks above the rail
    - The minecart can be made to hover by dropping it from the end of normal rails
- Comes in all waxed and oxidized copper variants

### Powered Maglev Rail

- Allows minecarts to hover just like maglev rails
- Accelerates the minecart just like powered rails
- Comes in all waxed and oxidized copper variants

### Variable Maglev Rail

- Allows minecarts to hover just like maglev rails
- Changes the hover height of a minecart to its redstone power level (0-15)
- Comes in all waxed and oxidized copper variants

## Mechanics

- While hovering, minecarts have no friction and have no sound
- Minecarts have different max speeds on different oxidized rails:
  | Oxidation | Max Speed |
  | --- | --- |
  | Unoxidized | 12 blocks/tick |
  | Exposed | 8 blocks/tick |
  | Weathered | 4 blocks/tick |
  | Oxidized | 1 blocks/tick |
- Minecarts remember which track they're on so rails can cross underneath and they stay on the right track
- You may place blocks between the rail and minecart to have hidden rails
- If a minecart hits a corner (including a change between flat and incline) while moving too quickly, it may derail. To prevent this, the minecart must be slowed to at most weathered speed. This was unintentional but is currently considered a feature.
- All maglev rails can be manually oxidized by right-clicking with a wet sponge

## Notes

- Does not work with experimental minecart mechanics yet, this will be added in the future