# CauldronBrew
_Spigot plugin that can make things in cauldron_

[Link to the spigot page](https://www.spigotmc.org/resources/cauldronbrew.86981/)

## 
### Config file
```yml
Localization: en

# Main Settings of cauldron
Cauldron:
  secondstoboil: 10 # Seconds to boil
  blocktoupgrage: iron_block # Click on block to upgrade. If you want craft in cauldron without upgrade. Just leave this empty
  maxitemsincauldron: 32 # How much items cauldron can take

# Event PlayerDropItem and checks where item is to drop in cauldron
# You can just forget about it
dropitem:
  checknearbycauldron:
    delay: 5
    radius: 0.1
    secondstocheck: 20

# Holograms in SUPER ALPHA!!!!
Hologram:
  enabled: false
  visiblinradius:
    enabled: true
    radius: 10
  floating:
    enabled: true
    distance: 0.2

# Don't touch
configversion: 0.1
```

### Crafts file
```yml
glowstone_dust: # Name of the craft
  enabled: true # Enabled or not
  effect: # Effect when you craft
    name: "epic" # Name of the particle effect
    color: orange # Color of the particle effect
  result: glowstone_dust # Result of the craft (If you want to craft custom item go to the spigot page)
  ingredients: sugar(1);torch(1);blaze_powder(1) # Ingredients [ item_name(amount);item_name(amount)]
```
