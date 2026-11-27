# SILICON FPS — Replacement Scope and Engineering Gates

## Product goal

SILICON FPS is intended to become one standalone Fabric client mod for Minecraft 1.21.11. It should replace the compatible client-side performance and utility features the user currently relies on, without requiring Sodium, Lithium, ImmediatelyFast, FerriteCore, Particle Core, Mouse Tweaks, or Thorium as permanent dependencies.

"Replace" means independently implemented, tested equivalent functionality where technically appropriate. It does not mean bundling, copying, or depending on those mods.

## Current verified baseline

The current repository contains:
- SILICON Control Center with General, Quality, Performance, and Advanced tabs.
- A conservative low-end video preset.
- A small ambient-particle reduction mixin.
- FPS and Java heap HUD.
- CPU wall-clock instrumentation between Fabric START_MAIN and END_MAIN world-render callbacks.

The current renderer backend is still vanilla. The timing value is not GPU time. No claim of Sodium parity or measured FPS improvement is made.

## Replacement matrix

| Project | Responsibility to replace | SILICON implementation target | Release gate |
|---|---|---|---|
| Sodium | Chunk/terrain rendering | Section lifecycle, visibility, frustum/occlusion culling, async mesh building, GPU buffer arenas, render-layer batching, fallback | Visual parity, chunk rebuild correctness, transparency, resource reloads, world transitions, mod compatibility |
| Lithium | Game simulation and logic hot paths | Independently audited client-relevant optimizations only; server/integrated-server optimizations assessed separately | Deterministic behavior tests; no changes to vanilla mechanics |
| ImmediatelyFast | Immediate-mode rendering | Reusable buffers, batching and reduced draw submission overhead for GUI/HUD, text, entities, block entities and particles | Correct ordering, blending, GUI parity, compatibility |
| FerriteCore | Heap/memory footprint | Profile allocations and object retention; optimize proven high-cost data representations and caches | Heap/GC benchmarks across representative worlds; no stale data or memory leaks |
| Particle Core | Particle cost | Per-type budgets, visibility/distance culling, configurable spawn reduction, safe handling of gameplay feedback | Preserve important feedback; verify culling and spawn behavior |
| Mouse Tweaks | Inventory interaction QoL | Optional configurable drag/scroll inventory interactions | Correct slot/container behavior, no unintended item movement, server-safe vanilla inventory actions |
| Thorium | Bug/annoyance fixes | Audit the exact upstream feature set and Minecraft-version relevance before selecting equivalents | Feature-by-feature compatibility tests; no assumed performance benefit |

## Important boundaries

- These projects solve different problems. A renderer cannot replace memory optimization, simulation optimization, or inventory interaction behavior.
- A client-only mod cannot improve remote server tick performance. SILICON may optimize the local client and, where appropriate, an integrated single-player server; remote server-side gains require server-side installation and a separate supported design.
- No combat mechanics, hitboxes, reach, attack speed, hit detection, packet manipulation, or player-visibility reductions are in scope.
- Entity visibility and simulation distance are preserved by default.
- Never remove the vanilla renderer fallback until the custom backend passes its release gates.
- Do not remove the user's existing optimization mods until the corresponding SILICON replacement is implemented and tested.
- Do not promise a specific FPS target. Measure on the target device and report frame-time distributions, not only peak FPS.

## Engineering sequence

1. **Baseline and instrumentation:** reliable frame-time, heap, GC, chunk-build and upload metrics with low overhead.
2. **Low-risk client optimizations:** bounded queues, allocation reductions and particle controls, each independently toggleable.
3. **Immediate rendering:** reusable buffers and batching with compatibility fallbacks.
4. **Terrain backend:** section manager, immutable build snapshots, bounded worker scheduling, meshing, culling, GPU allocation/upload and draw submission.
5. **Memory subsystem:** profile-driven optimizations with leak and reload tests.
6. **Simulation subsystem:** only correctness-preserving optimizations that apply to client/integrated-server contexts.
7. **Inventory QoL and bug fixes:** optional features, separate from performance claims.
8. **Mod Hub:** discovery and compatibility display first; installation/removal only with explicit user action, backups and clear restart requirements.
9. **Integrated validation:** automated build, dedicated regression worlds, multiplayer-safe behavior, resource reloads, world changes, low-memory stress and target-device testing.

## Acceptance criteria for a unified release

- One SILICON FPS jar; no hard dependency on the listed replacement mods.
- All settings are real, persisted, and accurately described.
- Each subsystem can fall back safely if unsupported.
- No crashes or resource leaks during world changes and resource reloads.
- No visual holes, missing entities, incorrect transparency, or inventory item loss.
- Automated build passes and the jar is inspected.
- Performance claims are based on repeatable measurements on the target device.
- A replacement is marked complete only after its own acceptance tests pass.
