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

## The problem

Minecraft 26.3 changed how your client smooths out the movement of other players and mobs. The new method often runs out of movement before the server sends the next update, so entities freeze for a moment and then jump forward. In PvP, on minigame servers, or just watching a friend run past, everything looks choppy.

This is a known vanilla bug: [MC-311974](https://bugs.mojang.com/browse/MC-311974) and [MC-311976](https://bugs.mojang.com/browse/MC-311976).

## The fix

Interpolation Fix puts players and mobs back on the 26.2 movement smoothing, which always leaves enough time for the next update to arrive. Movement is smooth again, the same as it was before the update.

## Details

- **Client-side only.** Works on any server, no server mod or plugin needed.
- **No dependencies.** Does not require Fabric API.
- **No config.** Drop it in your mods folder and you are done.
- **Tiny.** A single change with no effect on performance.
- **Singleplayer safe.** Only your client's view changes; worlds and servers are untouched.

## Compatibility

Built for **Fabric** on **Minecraft 26.3**. Works alongside Sodium, Iris, Entity Culling and other client mods.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.19.5 or newer for Minecraft 26.3.
2. Download the jar from [lode.gg](https://lode.gg/mod/interpolation-fix).
3. Put it in your `mods` folder.

## Building

Requires Java 25.

```sh
./gradlew build
```

The jar is written to `build/libs/`.

## License

Open source under the [MIT License](LICENSE). Issues and pull requests are welcome.
