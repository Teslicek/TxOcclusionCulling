# TxOcclusionCulling

Client-side Fabric mod for **Minecraft 26.3** with **Sodium** that skips terrain hidden behind other terrain while the GPU is the bottleneck.

## Terrain occlusion culling

Sodium draws every terrain section that its visibility graph and the view frustum let through, even when other terrain completely hides it. At render distance 32 a GPU occlusion probe (`/txocclusion`) found a third of the drawn terrain vertices in fully hidden sections. While the render thread spends a noticeable share of its time waiting for the GPU, TxOcclusionCulling skips those sections using the GPU's own depth:

- The main render pass is split right after opaque terrain and solid entities, before water, translucent features and clouds. Between the two halves the depth buffer is reduced to 320x180, each texel keeping the farthest depth of its screen block, and copied to the CPU.
- When a copy arrives, about two frames later, every drawn section's box is projected with the camera of that frame and tested against a farthest-depth pyramid of it, at the finest level where the rectangle spans at most 4x4 texels. Sections whose on-screen rectangle lies behind the stored depth are left out of Sodium's draw commands. Sections that were completely off screen in that frame are always drawn.
- A new depth copy is taken at most every 4 ms, and less often when testing would take more than half of the time the render thread spends waiting for the GPU, so the test never costs more CPU time than the GPU wait it can remove. Terrain that comes out from behind something because the camera or an entity moved, or that enters the screen edge while turning, appears a few milliseconds plus two frames late.
- The culler measures how long the render thread waits for the GPU over half-second windows and only runs while that wait is above 10 percent, switching off below 3 percent, so CPU-limited scenes pay nothing.

## Why it only runs while GPU-bound

Culling trades CPU time for GPU time. Where the GPU is the limit, the render thread already spends part of every frame waiting for it, the tests fit into that wait, the GPU finishes sooner and FPS rises. Where the CPU is the limit, the GPU already idles, drawing less cannot raise FPS, and the tests only add CPU time. An earlier version that ran everywhere lowered FPS in a CPU-limited lobby, which is why the wait meter exists. The 10 and 3 percent thresholds were chosen to leave a clear margin and avoid switching back and forth; they were not tuned by measurement.

## Measured

Singleplayer, render distance 32, RTX 5070, same view and the same number of solid terrain vertices in each run:

| | Without the culler | With the culler |
|---|---|---|
| FPS | 1740 to 1866 | 2151 |
| GPU time per frame | 0.509 ms | 0.443 ms |
| Solid terrain GPU time | 0.315 ms | 0.242 ms |
| Terrain culled | none | 21.3% |
| Culled sections that were visible | | 0 |

In one profile taken while flying, testing took 7.00% of the render thread while the render thread waited for the GPU 5.49%. That is why each test is now timed and spaced to at most half of the measured GPU wait.

## Commands

`/txocclusion` measures the next frame with GPU occlusion queries and reports, in chat and in `logs/txocclusion-<date>.txt`:

- the share of drawn terrain vertices in fully hidden sections,
- how much the culler removed,
- how many culled sections were actually visible.

## Build

```
gradlew build
```

Produces `build/libs/txocclusionculling-26.3-1.0.0.jar` and copies it to `dist/`.

## Install

Drop `dist/txocclusionculling-26.3-1.0.0.jar` into your Minecraft `mods/` folder.

Requires:

- Fabric Loader `>= 0.19.5`
- Fabric API
- Minecraft `26.3` on the Vulkan renderer
- Sodium
- Java `25`
