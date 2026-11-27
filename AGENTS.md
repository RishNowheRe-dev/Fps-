# Instructions for AI coding agents working on SILICON FPS

Before changing code, read `docs/PROJECT_DECISIONS_AND_AUTONOMOUS_WORKFLOW.md`.

The project owner has already supplied the product direction and does not want repeated clarification questions. Work autonomously on ordinary implementation choices, inspect the repository and existing UI references, run GitHub Actions, read failure logs, repair failures, and verify artifacts. Ask only for a truly blocking decision.

Do not report completion from a commit alone. A feature is compile-verified only after a successful CI run; runtime behavior remains unverified until tested in Minecraft.

Keep the visual design aligned with the project's black/charcoal/light-gray palette and cinematic moonlit Minecraft landscape. The account-skin preview must be detached from the live player's inventory and must not display armor or held items.

Never claim the mod replaces Sodium or other performance mods until the required rendering/performance implementations and measurements actually exist.
