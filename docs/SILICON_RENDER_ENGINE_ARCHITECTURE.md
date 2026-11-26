# SILICON Render Engine — Architecture and Replacement Plan

## Current implementation status

The current milestone is **instrumentation only**. `SiliconRenderEngine` registers Fabric's world-render START_MAIN and END_MAIN events and records a smoothed CPU wall-clock interval for the main world pass. It does not replace Minecraft's renderer, submit its own terrain geometry, or claim a performance improvement.

The measured interval is not a GPU timer. It is a render-thread wall-clock measurement between two Fabric callbacks.

## Minecraft 1.21.11 constraints

Minecraft 1.21.11 uses a split render lifecycle:

1. **Extraction/preparation:** gather render data and construct render state.
2. **Drawing:** submit prepared work to the frame buffer.

The engine must respect this separation. Mutable world objects must not be passed into asynchronous draw work. Render data crossing threads must be immutable or safely owned snapshots.

Fabric's `WorldRenderEvents` are suitable extension points for compatible additions and instrumentation. They are not, by themselves, a complete replacement API for the terrain renderer.

## Target architecture

### 1. Renderer integration
- A version-specific integration layer around the active level renderer.
- Explicit backend lifecycle: attach, world change, resource reload, resize, detach, shutdown.
- Vanilla fallback remains available until parity and stability are demonstrated.

### 2. Section visibility
- Track render sections and their lifecycle.
- Frustum culling first; conservative occlusion traversal second.
- Never cull a section unless visibility can be established safely.
- Keep nearby sections prioritized to avoid holes while moving.

### 3. Chunk meshing
- Snapshot the required block/biome/light data on the correct thread.
- Build opaque, cutout, and translucent meshes in bounded worker tasks.
- Cancel stale jobs when sections unload or become dirty.
- Prioritize nearby sections and cap work per frame.

### 4. GPU memory and uploads
- Reuse GPU allocations rather than allocating per section every frame.
- Stage uploads and enforce a strict per-frame upload budget.
- Release buffers on world changes and resource reloads.
- Track allocated and used memory separately.

### 5. Draw submission
- Batch compatible terrain by render layer and vertex format.
- Preserve transparency ordering and vanilla material behavior.
- Keep entity, block-entity, particle, weather, and GUI paths compatible.
- Add renderer-specific fast paths only behind capability checks.

## Low-end device policy

- Start with one conservative chunk-build worker; make thread count configurable only after measurement.
- Bound queued mesh work and upload bytes.
- Prefer stable frame pacing over aggressive background throughput.
- Avoid allocating temporary objects in per-frame hot paths.
- Provide a safe fallback for unsupported GPU/driver features.
- Do not assume desktop OpenGL capabilities on Android translation layers.

## Replacement gates

Sodium must not be removed until terrain rendering, chunk rebuilds, visibility, transparency, resource reloads, world transitions, and mod compatibility pass testing.

Lithium, ImmediatelyFast, FerriteCore, Particle Core, Mouse Tweaks, and Thorium are separate projects with different responsibilities. Each replacement must be assessed independently; the render engine alone cannot replace non-rendering optimizations or inventory behavior.

## Research references

- Fabric 1.21.11 world rendering guide: https://github.com/FabricMC/fabric-docs/blob/main/versions/1.21.11/develop/rendering/world.md
- Fabric 1.21.11 WorldRenderEvents API: https://maven.fabricmc.net/docs/fabric-api-0.141.3+1.21.11/net/fabricmc/fabric/api/client/rendering/v1/world/WorldRenderEvents.html
- Minecraft 1.21.11 LevelRenderer mappings: https://mappings.dev/1.21.11/net/minecraft/client/renderer/LevelRenderer
- Sodium's public architecture can be studied for concepts such as section management, asynchronous mesh building, culling, and GPU buffer arenas. SILICON must implement its own code and design rather than copy Sodium source.
