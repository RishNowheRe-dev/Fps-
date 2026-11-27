# SILICON FPS — Project Decisions & Autonomous Workflow

This file is the durable source of truth for future development sessions. Read it before asking the owner questions or changing UI behavior.

## Non-negotiable product direction

- Product: SILICON FPS, a client-side Fabric optimization and UI mod for Minecraft Java 1.21.11.
- Target device: low-end Android hardware using Aerix Launcher; Java 21; LTW renderer.
- The owner has Android only. Do not assume a desktop IDE, local Gradle, or command-line access.
- The owner expects the assistant to inspect the repository, make code changes, commit them, run CI, inspect failures, fix them, and verify artifacts directly wherever connected tools allow.
- Do not ask the owner to repeat requirements already written here or in the repository.
- Do not ask for permission for ordinary implementation decisions. Choose sensible defaults and proceed.
- Ask a question only when a genuinely blocking product decision cannot be safely inferred.
- Never claim a feature, build, test, artifact, or release is complete unless the relevant evidence exists.
- Never ask the owner to install an intermediate build just to discover a compilation error. CI must pass first.

## Visual design contract

- Main menu, in-game pause menu, hub, and performance Control Center must share a coherent visual language.
- Background direction: cinematic Minecraft-like night landscape, moon, stars, mountain silhouettes, distant forest, atmospheric mist, reflective water where appropriate.
- UI surfaces: near-black / charcoal glass panels with light-gray text and borders. Use restrained accent color only when useful; avoid making lavender the dominant palette.
- Buttons must have clear spacing, readable labels, distinct hover states, and resolution-aware bounds. Never let labels overlap or truncate.
- Main menu and pause menu should use custom layouts, not merely recolor vanilla screens.
- Player preview must reflect the logged-in account skin, be independently rotatable, and show skin/clothing only. Do not display armor, held items, or inventory equipment.
- Never mutate the live player's inventory/equipment as a persistent side effect to render a preview. Prefer a detached preview entity or dedicated skin/model rendering path.
- Account switching and cosmetics must be labelled as unavailable until actually implemented.

## Performance and gameplay constraints

- Keep the mod client-side and fair-play safe.
- Do not alter reach, hitboxes, attack speed, hit detection, packets, or server mechanics.
- Preserve player visibility in PvP.
- Do not claim a Sodium replacement or measured FPS gain until the renderer work exists and performance is measured on target hardware.
- Prefer low-allocation, low-overhead rendering and avoid expensive per-frame work.

## Required development loop

1. Read this file and inspect the current source before editing.
2. Make a coherent, scoped implementation; avoid racing multiple commits that break intermediate builds.
3. Run GitHub Actions build after changes.
4. If CI fails, fetch full job logs, identify exact compiler/runtime errors, fix them, and rerun CI without asking the owner to diagnose.
5. When CI passes, fetch the build artifact, inspect the JAR contents and metadata, and provide the verified artifact link.
6. Clearly distinguish compile verification from in-game testing. Only the owner can confirm behavior in Aerix Launcher unless a reproducible automated runtime test exists.
7. Keep this file updated when the owner makes a new lasting decision.

## Current known limitations to keep visible

- A successful Gradle build proves compilation only, not that mixins or screens behave correctly in-game.
- A player preview must be validated for skin fidelity, rotation, and absence of armor/items.
- Background art must be committed as a bundled resource and actually rendered by the screen implementation; a procedural approximation is not the final artwork.
- The project is not yet a full Sodium/Lithium/ImmediatelyFast/FerriteCore replacement.
