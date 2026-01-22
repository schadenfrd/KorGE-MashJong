[2026-01-04 01:33] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "working result acknowledged",
    "EXPECTATION": "They wanted a runnable example that shows a background and text on screen.",
    "NEW INSTRUCTION": "WHEN providing Korge example code THEN ensure it renders background and visible text."
}

[2026-01-04 01:55] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "architecture separation praised",
    "EXPECTATION": "They wanted a clear separation between the Android app and the shared game module.",
    "NEW INSTRUCTION": "WHEN structuring Korge multiplatform projects THEN split shared game and platform-specific app modules."
}

[2026-01-04 02:04] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "platform click handling",
    "EXPECTATION": "They want onPlayClick implemented per platform so clicks do something, especially on iOS.",
    "NEW INSTRUCTION": "WHEN a platform’s click does nothing THEN provide per-platform onPlayClick overrides with actions."
}

[2026-01-04 02:27] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "iOS tap no-action",
    "EXPECTATION": "They expected the Play button tap on iOS to trigger an immediate, visible action.",
    "NEW INSTRUCTION": "WHEN running on iOS and play tapped THEN implement onPlayClick to trigger a visible action immediately"
}

[2026-01-04 12:33] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "iOS modal style",
    "EXPECTATION": "They expected a full-screen view that cannot be dismissed by pulling down.",
    "NEW INSTRUCTION": "WHEN iOS view appears as bottom sheet THEN set modalPresentationStyle=fullScreen and isModalInPresentation=true"
}

[2026-01-04 16:02] - Updated by Junie
{
    "TYPE": "positive",
    "CATEGORY": "platform start success",
    "EXPECTATION": "They wanted both Android and iOS builds to start the game successfully.",
    "NEW INSTRUCTION": "WHEN setting up multiplatform launchers THEN verify Android and iOS start to a visible screen"
}

[2026-01-04 16:08] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "exit and text visibility",
    "EXPECTATION": "They expected an obvious way to exit the game and for on-screen text to be visible at runtime.",
    "NEW INSTRUCTION": "WHEN building playable Korge sample THEN add Exit control and verify visible on-screen text"
}

[2026-01-04 16:19] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "window auto-closing",
    "EXPECTATION": "They wanted the game window to remain open and playable until the user closes it.",
    "NEW INSTRUCTION": "WHEN Korge window opens then closes immediately THEN keep game loop running until explicit user exit"
}

[2026-01-04 16:26] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "play click no-action",
    "EXPECTATION": "They expected clicking Play to immediately perform a visible action and not regress from previously working behavior.",
    "NEW INSTRUCTION": "WHEN Play button is clicked THEN trigger a visible action and confirm on all platforms"
}

[2026-01-04 16:55] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "duplicate windows",
    "EXPECTATION": "They expected a single game window with visible text and a working close action.",
    "NEW INSTRUCTION": "WHEN two windows appear at runtime THEN launch only one window and remove extra entry points"
}

[2026-01-04 18:01] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "text visibility and closing",
    "EXPECTATION": "They expected the JVM build to show visible text from Stage.myGame and the window close button to actually close the app windows.",
    "NEW INSTRUCTION": "WHEN window close button is clicked THEN close game window and terminate process"
}

[2026-01-05 20:15] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "iOS rendering mismatch",
    "EXPECTATION": "They expected iOS to render the same scene as Android: DARKOLIVEGREEN background with centered CYAN text visible.",
    "NEW INSTRUCTION": "WHEN iOS shows MAGENTA background or missing CYAN text THEN render DARKOLIVEGREEN background and centered CYAN text like Android"
}

[2026-01-05 22:12] - Updated by Junie
{
    "TYPE": "preference",
    "CATEGORY": "cross-platform UX parity",
    "EXPECTATION": "They want Android to use the same full-screen, cross-dissolve style transition as iOS.",
    "NEW INSTRUCTION": "WHEN iOS uses full-screen cross-dissolve to open game THEN apply Android full-screen and fade transition on Activity start"
}

[2026-01-05 22:25] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "Android transition no-effect",
    "EXPECTATION": "They expected Android to show a full-screen fade/cross-dissolve when opening and closing the game Activity, matching iOS.",
    "NEW INSTRUCTION": "WHEN starting AndroidGameActivity THEN use overrideActivityTransition fade_in/fade_out and verify visible effect"
}

[2026-01-05 22:53] - Updated by Junie
{
    "TYPE": "preference",
    "CATEGORY": "ask mode response format",
    "EXPECTATION": "They want suggestions only as .md files and no coding when in ASK mode.",
    "NEW INSTRUCTION": "WHEN in ASK mode THEN respond with .md answer and do not modify code or run tools"
}

[2026-01-05 22:57] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "redundant fix proposed",
    "EXPECTATION": "They wanted a different solution because the suggested change already exists and still fails.",
    "NEW INSTRUCTION": "WHEN user says our fix matches their code THEN propose alternative fixes and request key files"
}

[2026-01-06 15:03] - Updated by Junie
{
    "TYPE": "correction",
    "CATEGORY": "duplicate windows and close",
    "EXPECTATION": "They want a single JVM window that switches between menu and game, and the window close button should work.",
    "NEW INSTRUCTION": "WHEN JVM shows two windows or close is ignored THEN use one window and handle window close to exit or return to menu"
}

