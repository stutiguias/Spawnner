TimeSpawner
===========

What is it?
-----------

TimeSpawner lets server staff create mob spawners with a custom mob type, amount, location, and timer.

Example: you can spawn 15 zombies at a fixed location every 60 seconds. The next timer only starts after the previously spawned mobs are gone.

Features
--------

- Set fixed mob spawns with mob type, amount, and time.
- Set random area spawns with mob type, amount, and time.
- Editable YAML spawner files.
- Does not spawn mobs when no player is nearby.
- Keeps track of spawned mobs across restart/shutdown.
- Auto-pull or manually pull mobs back if they get too far away.
- Global settings and per-mob settings.
- Vault economy support for player-purchased spawners.
- Owner management for purchased spawners.
- Enable or disable individual spawners.
- Timer signs for spawners.

Planned
-------

- Add more than one mob type per spawn.
- Add command to force spawn.
- Add timer for player XP from spawners.
- Add monster drop XP.
- Add custom config to each mob.
- Add spawn around a configurable range from online players.

Random Area Spawn
-----------------

1. Use `/sp wand` with your hands free to receive the TimeSpawner wand.
2. Left click the first block.
3. Right click the second block.
4. Use `/sp setspawn <spawnerName> <typeMob> <quantity> <time>`.

Done. Mobs will now spawn randomly inside that area.

Timer Sign
----------

Only one sign is allowed for each spawner.

Create a sign with:

- Line 1: `[TimeSpawner]`
- Line 2: `<spawner_name>`

The quantity and mob type will show on line 3. The timer will show on line 4. Disabled spawners show `OFF`.

Commands
--------

- `/sp <wand|w>` - Gives a stick used to set an area spawn.
- `/sp <setspawn|ss> <spawnerName> <typeMob> <quantity> <time>` - Creates a spawner.
- `/sp <buyspawn|bs> <spawnerName> <typeMob> <quantity> <time>` - Buys and creates an owned spawner.
- `/sp <delspawn|ds> <spawnerName>` - Deletes a spawner.
- `/sp <spawners|sp> [spawnName]` - Lists spawners or shows one spawner.
- `/sp <reloc|rl> [spawnName]` - Pulls mobs back to their spawner.
- `/sp <reset|rs> [spawnName]` - Removes tracked mobs and resets spawner timers.
- `/sp tp <spawnName>` - Teleports to a spawner.
- `/sp disable <spawnerName>` - Disables a spawner.
- `/sp enable <spawnerName>` - Enables a spawner.
- `/sp reload` - Reloads the plugin.
- `/sp <help|?|nothing>` - Shows command help.

Do not use symbols in spawner names.

Owner Management Commands
-------------------------

Players can use these commands on their own purchased spawners:

- `/sp spawners`
- `/sp spawners <name>`
- `/sp tp <name>`
- `/sp reset <name>`
- `/sp reloc <name>`
- `/sp delspawn <name>`
- `/sp disable <name>`
- `/sp enable <name>`

Permissions
-----------

- `tsp.admin` - Gives access to all TimeSpawner commands.
- `tsp.setspawn` - Allows creating admin spawners.
- `tsp.buyspawn` - Allows buying spawners. Default: true.
- `tsp.delspawn` - Allows deleting spawners.
- `tsp.spawners` - Allows viewing spawners.
- `tsp.reload` - Allows reloading the plugin.
- `tsp.wand` - Allows using the area selection wand.
- `tsp.sign` - Allows creating and breaking TimeSpawner signs.
- `tsp.tp` - Allows teleporting to spawners.
- `tsp.reloc` - Allows relocating mobs back to spawners.
- `tsp.reset` - Allows resetting spawners.
- `tsp.disable` - Allows disabling spawners.
- `tsp.enable` - Allows enabling spawners.
- `tsp.dragon.exp` - Allows XP from Ender Dragon spawners. Default: true.
- `tsp.dragon.unlimitedexp` - Allows repeated Ender Dragon XP. Default: false.

Configuration Notes
-------------------

- `Economy.EnableBuySpawners` controls whether players can buy spawners.
- `Economy.BuySpawner` controls default price, quantity/time multipliers, and per-mob prices.
- `DisableSpawner.RemoveMobs` controls whether disabling a spawner removes currently tracked mobs.
- Spawner YAML files include `Enabled: true|false`.

Requirements
------------

- Java 8 or newer.
- Spigot/Paper compatible with the configured server API.
- Vault is required for economy features.

Setup
-----

Put the TimeSpawner jar file in your `/plugins/` folder and restart the server. Config files will appear in `/plugins/TimeSpawner/`.

Video Tutorial
--------------

https://www.youtube.com/watch?v=-OQwoFh4o7c

Lag Warning
-----------

Spawning too many mobs may lag your server. The safe amount depends on your server performance.
