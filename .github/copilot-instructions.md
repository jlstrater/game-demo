# Copilot instructions for Game Demo

This file helps Copilot-based sessions understand how to build, run, test, and reason about this repository.

## Build / run / test / lint commands
- Use the included Gradle wrapper from the repo root: `./gradlew` (Windows: `gradlew.bat`).
- Full build: `./gradlew build`  
- Run desktop app (LWJGL3): `./gradlew :lwjgl3:run`  (working directory is set to the project's `assets/` folder)
- Create runnable jar: `./gradlew :lwjgl3:jar` (output: `lwjgl3/build/libs/`)
- Platform-specific jars: `./gradlew :lwjgl3:jarMac`, `:lwjgl3:jarLinux`, `:lwjgl3:jarWin`
- Run tests (all modules): `./gradlew test` or per-module `./gradlew :core:test`
- Run a single test class or method:
  - Class: `./gradlew :core:test --tests "com.example.MyTestClass"`
  - Method: `./gradlew :core:test --tests "com.example.MyTestClass.myTestMethod"`
- No dedicated lint/format task configured in the repo; run `./gradlew check` to run verification tasks if added.

## High-level architecture (big picture)
- Multi-module libGDX project (Gradle):
  - `core` — shared application code and game logic (Java/Groovy dependencies declared here).
  - `lwjgl3` — desktop launcher and platform-specific wiring; depends on `:core` and packages the runnable artifacts.
- Entry points:
  - Shared application: `io.github.jlstrater.camping.Main` (extends ApplicationAdapter).
  - Desktop launcher: `io.github.jlstrater.gamedemo.lwjgl3.Lwjgl3Launcher` (starts the LWJGL3 application and sets config).
- Assets are stored at repository root `assets/` and are included into `lwjgl3` resource path via `sourceSets.main.resources.srcDirs`.
- Packaging and distribution: `construo` plugin is used (in `lwjgl3` build.gradle) for native image/jlink-style packaging; optional Graal/native-image hooks exist behind `enableGraalNative` property.
- `generateAssetList` task (registered in root build) generates `assets/assets.txt` used by some tools.

## Key conventions and repo-specific patterns
- Module-prefixed Gradle tasks: target specific projects with `:core:`, `:lwjgl3:` prefixes (e.g., `:core:clean`).
- JDK/compatibility: build scripts set `sourceCompatibility`/`targetCompatibility` = 11; prefer using the Gradle wrapper and JDK 11 (toolchain plugin present in settings).
- Runtime args / platform quirks:
  - `lwjgl3` run task sets `workingDir` to `assets/` and adds `--enable-native-access=ALL-UNNAMED` to JVM args.
  - On macOS, `-XstartOnFirstThread` is added when required.
  - `StartupHelper` may restart the JVM to set environment variables for compatibility (see `lwjgl3` sources).
- Native/Graal builds: controlled by `gradle.properties` flag `enableGraalNative`; when `true` additional classpath/dependencies and `nativeimage.gradle` are applied.
- Dependency/version pins live in `gradle.properties` (gdxVersion, groovyVersion, ashleyVersion, lwjgl3Version, etc.); constraints for LWJGL are enforced in `lwjgl3/build.gradle`.
- Generated IDE tasks: `./gradlew idea` and `./gradlew eclipse` are supported; some project metadata is customized in root build to make IDE "Build and run using IDE" work reliably.

## Where to look for more detail
- README.md (project overview and common Gradle tasks) — keep this in sync if project structure changes.
- `lwjgl3/build.gradle` and `core/build.gradle` for platform-specific wiring and packaging details.
- `gradle.properties` for toggles and pinned versions.

---

(If you want the file changed or extended to include additional commands, tests, or CI specifics, say what to add.)
