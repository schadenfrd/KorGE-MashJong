[2026-01-19 11:52] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Logging output missing",
    "EXPECTATION": "They expected the new debug logs (e.g., 'starting game...') to appear in the Android Studio Run console when launching the desktop game.",
    "NEW INSTRUCTION": "WHEN adding Logger-based debug logs THEN explain output destination and enable console printing"
}

[2026-01-19 11:57] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Close events not triggered",
    "EXPECTATION": "They expected Stop/Dispose/Destroy debug logs to appear and the app to close when clicking the desktop window close button, and also expected desktop UI (text) to match mobile.",
    "NEW INSTRUCTION": "WHEN desktop close logs are missing THEN instrument window creation and identify which window emits events"
}

[2026-01-19 12:07] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Desktop close + UI mismatch",
    "EXPECTATION": "They expected close-button clicks to trigger Stop/Dispose/Destroy logs and the desktop UI to show the same text as on mobile.",
    "NEW INSTRUCTION": "WHEN desktop shows two windows THEN log all window creations and attach close listeners to each window"
}

[2026-01-19 12:28] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Run console logs visible",
    "EXPECTATION": "They wanted to see their debug logs (e.g., 'Starting game...') in the Android Studio Run console for the desktop app.",
    "NEW INSTRUCTION": "WHEN adding desktop debug logs THEN set Logger.defaultLevel=DEBUG early and note Run console"
}

[2026-01-19 20:06] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Gradle sync failure",
    "EXPECTATION": "They expected the project to sync successfully with a valid JDK path and correctly formatted Gradle properties.",
    "NEW INSTRUCTION": "WHEN Gradle reports invalid org.gradle.java.home value THEN inspect gradle.properties line breaks and fix JDK path"
}

[2026-01-24 16:31] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Approach validated",
    "EXPECTATION": "They liked the initial tile visualization and want a researched plan for a tile factory and a renderer/coordinator to place tiles correctly.",
    "NEW INSTRUCTION": "WHEN discussing tile creation or placement architecture THEN provide a researched plan with concrete Korge APIs"
}

[2026-01-24 16:52] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Tile dimensions constants",
    "EXPECTATION": "They want tile width and height defined once as constants in Tile (companion object) and referenced everywhere, using the current width/height values they set.",
    "NEW INSTRUCTION": "WHEN adding or using tile dimensions THEN define in Tile.companion and reference the constants"
}

[2026-01-24 18:23] - Updated by Junie
{
    "TYPE": "preference",
    "CATEGORY": "Mode and format preference",
    "EXPECTATION": "They want no coding in ASK mode and responses as a .md scratch file containing thoughts/plan.",
    "NEW INSTRUCTION": "WHEN in ASK mode THEN avoid coding; reply as .md scratch with thoughts and plan"
}

[2026-01-24 18:34] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Resource not found (Android)",
    "EXPECTATION": "They expected the atlas image to be packaged and loadable via resourcesVfs on Android without crashing back to the main menu.",
    "NEW INSTRUCTION": "WHEN resourcesVfs readBitmap fails on Android THEN place file in commonMain/resources and load by exact name"
}

[2026-01-24 18:46] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Tile sizing mismatch",
    "EXPECTATION": "They want the face image to fill the tile area correctly so tiles sit closer together without excess padding.",
    "NEW INSTRUCTION": "WHEN tile face looks smaller than container THEN set Tile face/tile dimensions to atlas slice size and scale to fit"
}

[2026-01-24 20:35] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Atlas layout mismatch",
    "EXPECTATION": "They want TileFactory to slice and map the atlas using row order: Bamboo (row 0), Dots (row 1), Characters (row 2), and special tiles on rows 3–4 with different intra-row spacing.",
    "NEW INSTRUCTION": "WHEN slicing current atlas THEN support per-row x-offsets, counts, and steps"
}

[2026-01-24 20:52] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Atlas slicing mismatch",
    "EXPECTATION": "They want the atlas sliced according to its per-row layout so every tile face is fully captured in the correct order.",
    "NEW INSTRUCTION": "WHEN tiles appear mis-cut with uniform grid slicing THEN slice each row with configurable startX, count, step, and size"
}

[2026-01-24 21:06] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Atlas slicing parameters",
    "EXPECTATION": "They want TileFactory to slice using 41x53 tiles, 3px intra-group spacing, and handle special groups with extra gaps: 17px between dragons→winds and 47px between seasons→flowers, preserving group order (dragons, winds, seasons, flowers).",
    "NEW INSTRUCTION": "WHEN atlas has non-uniform spacing THEN configure tile size, per-group step, and extra gaps"
}

[2026-01-24 21:16] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Atlas spacing confirmation",
    "EXPECTATION": "They want the atlas slicing to use 41x53 tiles, 3px horizontal spacing, 8px vertical spacing, with extra gaps: 17px between dragons and winds, and 47px between seasons and flowers.",
    "NEW INSTRUCTION": "WHEN slicing the current mahjong atlas THEN apply 41x53 tiles and specified spacings/gaps"
}

[2026-01-24 21:42] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Resource not found (iOS)",
    "EXPECTATION": "They expected the atlas image to be packaged into the iOS app bundle and loadable via resourcesVfs without crashing when starting the game on iOS.",
    "NEW INSTRUCTION": "WHEN iOS FileNotFound for atlas THEN place file in commonMain/resources and reference exact-case filename"
}

[2026-01-27 09:27] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They confirmed the rules/implementation plan is good and want it implemented now.",
    "NEW INSTRUCTION": "WHEN user approves a proposed plan THEN implement it with code and integration steps"
}

[2026-01-27 10:04] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Free tile side-blocking",
    "EXPECTATION": "A tile with neighbors on both its left and right on the same layer must not be selectable.",
    "NEW INSTRUCTION": "WHEN detecting side neighbors THEN consider footprint overlap with tolerance, not exact x equality"
}

[2026-01-27 20:58] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Menu integration + HUD layout",
    "EXPECTATION": "They want the existing Compose-driven menu flow (AndroidGameActivity/MainViewController) used to start the game, and in-game buttons positioned at screen edges (Undo/Hint bottom-right, Go to Menu top-right) without covering tiles.",
    "NEW INSTRUCTION": "WHEN starting game flow THEN integrate with AndroidGameActivity/MainViewController compose menu"
}

[2026-01-27 21:05] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "HUD layout and navigation",
    "EXPECTATION": "They want large in-game buttons anchored at screen edges (not covering tiles) and the Quit button to return to the main menu.",
    "NEW INSTRUCTION": "WHEN adding in-game HUD buttons THEN anchor to screen edges with padding; wire Quit to showMenu()"
}

[2026-01-27 21:16] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Board scale and HUD",
    "EXPECTATION": "They want the tiles/board to fill most of the screen (not tiny) and the HUD buttons anchored to screen edges, large, and not covering tiles.",
    "NEW INSTRUCTION": "WHEN using DefaultViewport 1280x720 THEN scale BoardView to ~85% stage size with safe padding"
}

[2026-01-28 11:34] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Architecture and performance",
    "EXPECTATION": "They want MahjongGame decoupled (logic vs geometry vs history), layout rules abstracted (not hardcoded in isTileFree), BoardView to reuse views instead of recreating, and main.kt to separate UI flow from initialization.",
    "NEW INSTRUCTION": "WHEN refactoring core game code THEN decouple modules and avoid hardcoded layout rules"
}

[2026-01-28 12:12] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "HUD bound to board",
    "EXPECTATION": "HUD buttons should use full screen space and anchor to screen edges, not overlap tiles or be constrained by the board bounds.",
    "NEW INSTRUCTION": "WHEN adding in-game HUD/buttons THEN attach to stage/root and anchor to stage edges"
}

[2026-01-28 12:24] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "HUD decoupling from board",
    "EXPECTATION": "They want the HUD laid out using the full screen (stage) dimensions, not constrained by the game board/container, so buttons reach the screen edges.",
    "NEW INSTRUCTION": "WHEN HUD aligns short of screen edges THEN add HUD to stage/root and use stage size"
}

[2026-01-28 12:27] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They approved the proposed plan and want it implemented now with code changes.",
    "NEW INSTRUCTION": "WHEN user says to implement the plan THEN proceed to code changes with integration steps"
}

[2026-01-28 12:48] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "HUD layout debugging",
    "EXPECTATION": "They want visible debug backgrounds on game and HUD layers (RED/BLUE) to inspect their bounds and diagnose why the HUD is squashed in the top-left.",
    "NEW INSTRUCTION": "WHEN HUD appears squashed top-left THEN add stage-sized red and blue debug backgrounds"
}

[2026-01-28 13:26] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They confirmed the HUD decoupling and transparent rect sizing plan is good and want it implemented now.",
    "NEW INSTRUCTION": "WHEN user approves the plan and asks to implement THEN provide code changes and integration steps"
}

[2026-01-28 13:49] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Init sequencing + decoupling",
    "EXPECTATION": "They want to avoid lateinit crashes by ensuring GameScene finishes initialization only after it’s attached to a stage, and to remove the GameScene↔HUD circular dependency using an orchestrator/bridge.",
    "NEW INSTRUCTION": "WHEN constructing HUD from GameScene state THEN ensure GameScene initialized and stage attached first"
}

[2026-01-28 13:53] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They approved the HUD decoupling approach and want it implemented now with concrete code changes.",
    "NEW INSTRUCTION": "WHEN user approves a proposed plan THEN implement it with code and integration steps"
}

[2026-01-28 14:35] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They approved the HUD decoupling strategy and want it implemented now.",
    "NEW INSTRUCTION": "WHEN user approves a proposed plan THEN implement it with code and integration steps"
}

[2026-01-28 16:33] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "Plan approved",
    "EXPECTATION": "They confirmed the architecture refactor plan is solid and want it implemented now with code and integration steps.",
    "NEW INSTRUCTION": "WHEN user approves a proposed plan THEN implement it with code and integration steps"
}

[2026-01-28 23:09] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Android branding mismatch",
    "EXPECTATION": "They expected Android to show the custom launcher icon and the app label 'Mish-MashJong' instead of the default icon and 'mish_mash_jong'.",
    "NEW INSTRUCTION": "WHEN configuring Android app branding THEN set launcher icon assets and android:label to 'Mish-MashJong'"
}

[2026-01-29 09:18] - Updated by Junie
{
    "TYPE": "negative",
    "CATEGORY": "Popup layout misaligned",
    "EXPECTATION": "They want the Undo/Shuffle/Quit popup to have buttons centered inside the popup with proper spacing, not overflowing the top, and the title clearly separated and centered above the buttons.",
    "NEW INSTRUCTION": "WHEN creating in-game popup dialogs THEN center buttons and place title top-center with padding"
}

