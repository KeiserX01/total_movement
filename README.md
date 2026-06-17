# Albion Total Movement

Fabric server-side helper mod for Minecraft 1.21.1.

Requires Fabric Loader only. Fabric API is not required.

It registers the vanilla scoreboard criterion:

```mcfunction
/scoreboard objectives add movement total_movement
```

The score stores total distance in tenths of a block:

- `1` block = `10` score units.
- `1.5` blocks = `15` score units.

## Technical Design

- Uses `ScoreboardCriterion.create("total_movement")`, which makes the criterion available to vanilla scoreboard commands.
- Injects once at the end of `MinecraftServer#tick` and iterates only connected players.
- Uses Minecraft's own scoreboard persistence. No extra world save file is needed for permanent scores.
- Measures real 3D position deltas, so horizontal and vertical movement are counted together.
- Covers walking, sprinting, jumping, swimming, climbing, elytra flight, knockback, and mounted movement because all of them move the player entity.
- Keeps a fractional accumulator per online player so small movements are not lost between score updates.
- Calls `scoreboard.forEachScore(criterion, player, action)`, using Minecraft's internal objective-by-criterion index instead of scanning all objectives manually.
- Uses tiny mixins for the server tick and for resetting samples after teleports. A pure tick delta cannot reliably distinguish a short `/tp` from real movement.
- Ignores movement updates above 8 blocks per tick so teleports, ender pearls, portals, or third-party position jumps do not add score.

## Installation

1. Install Fabric Loader on the Minecraft 1.21.1 dedicated server.
2. Copy `build/libs/albion-total-movement-1.0.0.jar` into the server `mods/` folder.
3. Start the server.
4. Create the objective:

```mcfunction
/scoreboard objectives add movement total_movement
```

Optional display example:

```mcfunction
/scoreboard objectives setdisplay sidebar movement
```

The mod is marked with `"environment": "*"`, so it can be present on clients, but it is only required on the server.
