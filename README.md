# SILICON FPS

**A client-side performance project for Minecraft Java Edition, focused on low-end hardware.**

SILICON FPS is being developed as a standalone Fabric optimization suite, with low-end PCs and Android-based Java launchers in mind. The long-term goal is to bring several carefully selected performance and quality-of-life systems into one configurable mod.

> **Development status: early development / not a complete replacement yet.**
> The current build provides a low-end video preset, selected ambient-particle reduction, a small performance HUD, and an initial Control Center. The vanilla Minecraft renderer is still active. Terrain rendering, memory optimization, broad game-logic optimization, and full replacements for other mods are not implemented yet.

## Current features

- **Low-End / Potato Mode:** applies conservative video settings intended to reduce rendering workload.
- **Ambient-particle reduction:** removes selected ambient effects while preserving combat and interaction feedback.
- **Performance HUD:** displays FPS and Java heap information, plus the current world-render-pass timing metric.
- **Control Center:** provides the currently implemented settings in one in-game screen.
- **Config persistence:** stores SILICON FPS settings in `config/siliconfps.properties`.

The render-pass timing is a CPU-side elapsed-time measurement around a vanilla render event. It is **not** GPU time and does not mean SILICON has replaced the terrain renderer.

## Compatibility

| Component | Target |
| --- | --- |
| Minecraft | Java Edition 1.21.11 |
| Mod loader | Fabric |
| Java | 21 or newer |
| Environment | Client-side |
| Required | Fabric Loader 0.18.4+, Fabric API |
| Optional | Mod Menu 17.0.1+ |

The project is designed with low-end devices in mind, including Android Java launchers, but compatibility and performance must be verified on each launcher, device, GPU, and mod combination. No universal FPS increase is guaranteed.

## Installation (development builds)

1. Install a compatible Minecraft Java 1.21.11 Fabric profile with Java 21.
2. Install the required Fabric API version for that Minecraft version.
3. Download the JAR from a successful GitHub Actions build artifact.
4. Place the JAR in that profile's `mods` folder.
5. Launch the game and check the log for the `SILICON FPS initialized successfully` message.

Development artifacts are for testing, not stable release builds. Keep backups of worlds and configuration files before testing.

## Build from source

Requirements: Git, Java 21, and Gradle 9.2.1.

```bash
git clone https://github.com/RishNowheRe-dev/Fps-.git
cd Fps-
gradle build
```

The built mod JAR is produced in `build/libs/`.

GitHub Actions also builds the project on pushes to `main` and pull requests targeting `main`.

## Roadmap

The project uses staged implementation and release gates. Planned work includes:

1. More useful frame-time, memory, garbage-collection, and chunk-work diagnostics.
2. Low-overhead client rendering optimizations with safe fallbacks.
3. A properly integrated terrain-rendering backend, including chunk visibility, meshing, uploads, and draw submission.
4. Memory/allocation improvements.
5. Correctness-preserving client-side simulation improvements where applicable.
6. Inventory quality-of-life features and selected bug fixes.
7. Compatibility testing, configuration polish, and release documentation.

The intention is to cover selected functions from mods such as Sodium, Lithium, ImmediatelyFast, FerriteCore, and Particle Core over time. These projects solve different problems; equivalent functionality will only be claimed when the corresponding implementation exists and has been tested. Mouse Tweaks is primarily an inventory quality-of-life mod, not an FPS optimizer. Thorium's exact features and version compatibility need separate verification.

**Do not remove existing optimization mods just because SILICON FPS is installed.** Remove a mod only after the specific SILICON replacement for its features is implemented, tested, and documented. In particular, the current build does not replace Sodium.

## Performance and fairness principles

- Prefer stable frame pacing and predictable memory use over risky "maximum FPS" tricks.
- Keep player visibility and simulation settings intact unless the user explicitly changes them.
- Do not alter reach, hitboxes, attack speed, hit detection, combat packets, or server mechanics.
- Keep a vanilla-renderer fallback until a replacement backend passes visual and stability checks.
- Measure performance on real target devices; do not publish unverified benchmark claims.

## Bug reports

When reporting a problem, include:

- Minecraft version and launcher
- Device model, CPU, and GPU (if known)
- Java version and allocated memory
- Fabric Loader and Fabric API versions
- SILICON FPS version / build commit
- Other installed mods
- Steps to reproduce and relevant `latest.log` excerpts

Please remove account names, server addresses, and other private information from logs before sharing them publicly.

## License

SILICON FPS is licensed under the MIT License. See [LICENSE](LICENSE).
