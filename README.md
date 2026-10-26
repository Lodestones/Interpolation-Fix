<div align="center">
<br />
<img alt="Interpolation Fix" width="640" src=".github/banner.png" />
<br />
<b>
  <i>Brings back the smooth entity movement from Minecraft 26.2. Fixes the stutter on players and mobs introduced in 26.3.</i>
</b>
<h4>Supports 26.3</h4>

<a href="https://lode.gg/mod/interpolation-fix"><img alt="Download" src="https://img.shields.io/badge/download-lode.gg-2bd4c6?style=for-the-badge" /></a>
<a href="https://discord.com/invite/X8KP2XMdqu"><img alt="Discord" src="https://img.shields.io/badge/discord-join-5865F2?style=for-the-badge&logo=discord&logoColor=white" /></a>
<a href="LICENSE"><img alt="MIT License" src="https://img.shields.io/badge/license-MIT-blue?style=for-the-badge" /></a>
</div>

<br />

## ⚠️ WARNING ⚠️
Any server whose anticheat is **modeled specifically on 26.3** will **flag you for Reach and can ban you**. Use this mod with heavy caution, preferably for recordings rather than combat.

| Servers       | Allows 26.3? | Tested? | Flagged? | Bannable? |
|---------------|--------------|---------|----------|-----------|
| mcpvp.com     | ✅           | ✅      | ✅       | ✅        |
| mcpvp.club    | ✅           | ✅      | ❓       | ❓        |
| minemen.club  | ✅           | ✅      | ❓       | ❌        |
| pvphq.net     | ❌           | ❓      | ❓       | ❓        |
| pvplegacy.com | ❌           | ❓      | ❓       | ❓        |
| donutsmp.net  | ✅           | ✅      | ❓       | ❓        |
| pvptiers.com  | ❌           | ❓      | ❓       | ❓        |

Any server that runs anticheats on 26.2 or lower with the old interpolation combined with ViaVersion will continue using the old interpolation which should theoretically make you safe.

## The problem

Minecraft 26.3 changed how your client smooths out the movement of other players and mobs. The new method often runs out of movement before the server sends the next update, so entities freeze for a moment and then jump forward. In PvP, on minigame servers, or just watching a friend run past, everything looks choppy.

This is a known vanilla bug: [MC-311974](https://bugs.mojang.com/browse/MC-311974) and [MC-311976](https://bugs.mojang.com/browse/MC-311976).

## The fix

Interpolation Fix puts players and mobs back on the 26.2 movement smoothing, which always leaves enough time for the next update to arrive. Movement is smooth again, the same as it was before the update.

## Details

- **Safe by default.** Out of the box the fix only turns on for servers that opt in, and in singleplayer. Everywhere else you get vanilla 26.3.
- **Three modes.** Server Opt-In (default), Always, or Off. Change it in the Mod Menu settings screen, or bind "Cycle Interpolation Fix Mode" under Controls > Misc. Your choice is saved.
- **Works with AntiStutter servers.** Servers that support LokaMC's AntiStutter turn this mod on too.
- **Tiny.** No effect on performance.

## For server owners

Drop `Interpolation-Fix-Paper.jar` (from the [GitHub releases](https://github.com/Lodestones/Interpolation-Fix/releases)) into your `plugins` folder to opt your server in. It runs on Paper, Purpur and Folia 1.21.6 and newer. Only do this if your anticheat accepts 26.2 style interpolation from players using the mod.

Under the hood, a server opts in by registering the `interpolationfix:linear_3` (or `antistutter:linear_3`) plugin channel. Players using the fix register the channel back, so anticheats can tell who to model the 26.2 way. Plugins can check a player with `InterpolationFixPlugin.usesLinearInterpolation(player)`. Nothing is ever sent on the channel.

## Compatibility

Built for **Fabric** on **Minecraft 26.3**. Works alongside Sodium, Iris, Entity Culling and other client mods.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.19.5 or newer for Minecraft 26.3, plus [Fabric API](https://modrinth.com/mod/fabric-api).
2. Download the jar from [lode.gg](https://lode.gg/mod/interpolation-fix).
3. Put it in your `mods` folder. [Mod Menu](https://modrinth.com/mod/modmenu) is optional, for the settings screen.

## Building

Requires Java 25.

```sh
./gradlew build
```

The mod jar is written to `build/libs/` and the Paper plugin to `paper/build/libs/`.

## License

Open source under the [MIT License](LICENSE). Issues and pull requests are welcome.
