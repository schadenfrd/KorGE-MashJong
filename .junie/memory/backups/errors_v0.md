[2026-01-04 00:37] - Updated by Junie - Error analysis
{
    "TYPE": "env/setup",
    "TOOL": "gradle",
    "ERROR": "Requires Java 21 but build uses Java 17",
    "ROOT CAUSE": "KorGE Gradle plugin 6.0.0 mandates JDK 21 while Gradle ran on JDK 17.",
    "PROJECT NOTE": "Set Gradle JVM to JDK 21 (via IDE Gradle JVM, JAVA_HOME, org.gradle.java.home, or Gradle toolchains).",
    "NEW INSTRUCTION": "WHEN Gradle error mentions \"requires at least JVM runtime version\" THEN configure Gradle to use the required JDK version"
}

[2026-01-04 01:05] - Updated by Junie - Error analysis
{
    "TYPE": "build/compile",
    "TOOL": "kotlinc (Gradle)",
    "ERROR": "Unresolved reference 'main' in MainActivity.kt",
    "ROOT CAUSE": "Android MainActivity references a top-level main() that does not exist in common code.",
    "PROJECT NOTE": "KorGE expects a suspend fun main() in commonMain or MainActivity must call the actual entrypoint’s FQN.",
    "NEW INSTRUCTION": "WHEN Android compilation reports \"Unresolved reference 'main'\" in MainActivity.kt THEN add suspend fun main() in commonMain matching MainActivity call"
}

[2026-01-04 01:37] - Updated by Junie - Error analysis
{
    "TYPE": "build/config",
    "TOOL": "Gradle (KorGE plugin)",
    "ERROR": "lateinit property projectType has not been initialized",
    "ROOT CAUSE": "KorGE targets are configured before initializing projectType (e.g., missing korge.id).",
    "PROJECT NOTE": "In build.gradle.kts, set korge { id = \"your.package.id\" } before calling targetJvm/targets.",
    "NEW INSTRUCTION": "WHEN Gradle error mentions uninitialized projectType in KorgeExtension THEN set korge.id before configuring targets"
}

[2026-01-04 01:56] - Updated by Junie - Error analysis
{
    "TYPE": "build/config",
    "TOOL": "Gradle",
    "ERROR": "Configuration cache cannot serialize Project in :game:prepareKotlinNativeBootstrapIos task",
    "ROOT CAUSE": "KorGE/Kotlin/Native task captures a Gradle Project instance, which configuration cache forbids.",
    "PROJECT NOTE": "Set org.gradle.configuration-cache=false in gradle.properties until KorGE/Kotlin plugins are cache-compatible.",
    "NEW INSTRUCTION": "WHEN Gradle configuration cache report cites disallowed Project serialization THEN Disable configuration cache or run with --no-configuration-cache for the build"
}

[2026-01-04 02:15] - Updated by Junie - Error analysis
{
    "TYPE": "runtime/init",
    "TOOL": "iOS app (KorGE)",
    "ERROR": "MyIosGameWindow lateinit property was not initialized on iOS",
    "ROOT CAUSE": "The iOS entrypoint didn't set up the KorGE GameWindow before starting Korge.",
    "PROJECT NOTE": "On iOS, launch KorGE using the provided UIKit/SwiftUI bridge (e.g., KorgeUIViewController/KorgeSwiftUIApp or AppDelegate helper) so it assigns MyIosGameWindow before CreateDefaultGameWindow runs.",
    "NEW INSTRUCTION": "WHEN iOS crash mentions MyIosGameWindow uninitialized THEN initialize KorGE iOS window via UIKit/SwiftUI launcher before calling main"
}

[2026-01-04 12:16] - Updated by Junie - Error analysis
{
    "TYPE": "runtime/linkage",
    "TOOL": "iOS app (Kotlin/Native)",
    "ERROR": "IrLinkageError: missing setter symbol for MyIosGameWindow",
    "ROOT CAUSE": "The iOS binary and korlibs/KorGE runtime are version-mismatched or stale, missing the expected setter symbol.",
    "PROJECT NOTE": "Ensure all modules use the same KorGE/korlibs and Kotlin versions; clean Gradle and Kotlin/Native caches and Xcode DerivedData, then regenerate the iOS framework before running from Xcode.",
    "NEW INSTRUCTION": "WHEN Kotlin/Native IrLinkageError reports missing symbol at runtime THEN align KorGE/Kotlin versions and rebuild, clearing K/N and Xcode caches"
}

[2026-01-04 16:05] - Updated by Junie - Error analysis
{
    "TYPE": "runtime/init",
    "TOOL": ":composeApp:jvmRun",
    "ERROR": "JVM process aborted with exit code 134",
    "ROOT CAUSE": "On macOS, the app likely started without -XstartOnFirstThread causing a SIGABRT.",
    "PROJECT NOTE": "For Compose Desktop on macOS, configure JVM args: compose.desktop.application { jvmArgs(\"-XstartOnFirstThread\") } or application { applicationDefaultJvmArgs = listOf(\"-XstartOnFirstThread\") }.",
    "NEW INSTRUCTION": "WHEN jvmRun on macOS exits 134 without stacktrace THEN add -XstartOnFirstThread to JVM args"
}

[2026-01-04 18:03] - Updated by Junie - Error analysis
{
    "TYPE": "tool failure",
    "TOOL": "create",
    "ERROR": "File already exists; create refused",
    "ROOT CAUSE": "Tried to create a file that already exists instead of editing it.",
    "PROJECT NOTE": "The common KorGE entry file (game/src/commonMain/kotlin/main.kt) already exists; modify it with an edit/search-replace action.",
    "NEW INSTRUCTION": "WHEN target file already exists THEN use search_replace or edit instead of create"
}

[2026-01-05 19:22] - Updated by Junie - Error analysis
{
    "TYPE": "build/config",
    "TOOL": "Gradle",
    "ERROR": "No matching variant for ios_simulator_arm64",
    "ROOT CAUSE": "Module :game does not publish a Kotlin/Native iosSimulatorArm64 klib variant required by :composeApp.",
    "PROJECT NOTE": "Ensure :game defines iOS native targets (e.g., iosSimulatorArm64) and publishes them; with KorGE use korge { androidLibrary = true; targetIos() } or explicitly add kotlin { iosSimulatorArm64() } so a native apiElements variant exists.",
    "NEW INSTRUCTION": "WHEN Gradle reports \"No matching variant\" for ios_simulator_arm64 THEN define iosSimulatorArm64 target in the dependency module and publish its klib"
}

[2026-01-05 19:22] - Updated by Junie - Error analysis
{
    "TYPE": "build/compile",
    "TOOL": "Gradle (Kotlin/Native compile)",
    "ERROR": "Unresolved reference 'startGame' in composeApp iOS source",
    "ROOT CAUSE": "composeApp's iOS code calls startGame, but :game doesn't expose that symbol for iOS.",
    "PROJECT NOTE": "Expose a top-level fun startGame() in game/src/iosMain with package com.creature.mashjong, or adjust composeApp/src/iosMain/.../MainViewController.kt to call the actual iOS entrypoint defined in :game.",
    "NEW INSTRUCTION": "WHEN compile error mentions \"Unresolved reference 'startGame'\" in composeApp iosMain THEN add matching startGame() in :game iosMain or fix the call"
}

[2026-01-06 15:05] - Updated by Junie - Error analysis
{
    "TYPE": "tool failure",
    "TOOL": "bash",
    "ERROR": "Here-doc upload aborted; file not written",
    "ROOT CAUSE": "The here-document wasn’t properly quoted/terminated, leaving the shell waiting and then interrupted.",
    "PROJECT NOTE": "Target file is composeApp/src/jvmMain/kotlin/com/creature/mashjong/mish_mash_jong/main.kt.",
    "NEW INSTRUCTION": "WHEN writing multi-line file with bash heredoc THEN use <<'EOF' and close EOF"
}

