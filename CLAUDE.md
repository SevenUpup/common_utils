# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**common_utils** is an Android library toolkit published to JitPack/Maven Central (`io.github.sevenupup`), providing reusable utilities, UI components, and a custom Gradle ASM plugin for bytecode manipulation. Built with Kotlin 1.7.10, Java 11, Android minSdk 24 / targetSdk 32 / compileSdk 32.

Repository: https://github.com/SevenUpup/common_utils

## Module Structure

This is a multi-module Gradle project with 8 modules:

| Module | Type | Description |
|---|---|---|
| `app` | Android Application | Demo/sandbox app showcasing all features. Entry point is `MainActivity.kt`. Contains examples for AIDL, ASM, Room, coroutines, WebView, JS bridge, privacy hooks, etc. |
| `common_base_util` | Android Library | Core utility classes — JSON (Gson), encryption, DataStore, lifecycle, logging, event bus (`livedata_bus`), design patterns, and Kotlin extensions. Published as `base_util`. |
| `common_base_ui` | Android Library | Reusable UI widgets — custom views, adapters (BRVAH), XPopup, image picker (PictureSelector), wheel picker, banner, immersion bar, Glide integration. Published as `base_ui`. |
| `easy_navigation` | Android Library | Navigation wrapper around AndroidX Navigation Component. Published as `easy_navigation`. |
| `fido_asm` | Java/Groovy Library | Custom Gradle plugin (`com.fido.plugin.asm`) for ASM bytecode manipulation: click debounce, class replacement, method hooking, constant modification, toast replacement, privacy compliance (device info masking). |
| `fido_annotation` | Java Library | Annotation library used by `fido_asm` — e.g., `@AsmMethodReplace` for method replacement targeting. |
| `groovy_lib` | Java/Groovy Library | Groovy-based Gradle plugin development sandbox with incremental build examples. |
| `common_tips` | Documentation | Tips and guides for daily Android development. |

## Key Commands

```bash
# Build debug APK, install and launch (convenience script)
./buildDebug.sh

# Standard Gradle commands
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK (minify + shrink enabled)
./gradlew clean                  # Clean build outputs
./gradlew :common_base_util:assemble  # Build specific module
./gradlew countImagesInApk       # Custom task: count drawable resources in APK

# Run with stacktrace for debugging build issues
./gradlew assembleDebug --stacktrace

# Publish to Maven (Sonatype S01)
# VERSION_NAME in gradle.properties controls the published version
```

## Dependency Management

- **`dependent.gradle`**: Centralized version catalog — all SDK versions and third-party library dependencies are defined here as `deps` maps. Use `deps.android.*` and `deps.support.*` across modules.
- **`utils.gradle`**: Shared Gradle utility functions (e.g., `getpackage()`).
- Maven repos: Aliyun mirrors (primary), JitPack, Maven Central, Google, Alibaba internal.

## ASM Plugin (`fido_asm`)

The custom Gradle plugin provides bytecode-level transformations:

- **ViewClickPluginParameter**: Replace `View.setOnClickListener` with custom click handler (debounce)
- **ReplaceClassPluginParameter**: Globally replace class inheritance (e.g., `AppCompatActivity` → custom base)
- **HookClassParameter**: Modify static/final field values at bytecode level (supports primitives)
- **ReplaceMethodPluginParameters**: Replace specific methods via `@AsmMethodReplace` annotation
- **ToastPluginParameter**: Replace Toast calls with custom implementation
- **fido block**: `printDependencies`, `permissionsToRemove`, `analysisSo`, `checkSnapshot`

Configuration in root `build.gradle` / module `build.gradle`:
```groovy
ext.fido_asm_replace_method_enable = true  // Enable method replacement
ext.fido_asm_replace_method_class = ["com.xxx.ClassName"]  // Classes to scan

fido {
    printDependencies = true
    permissionsToRemove = ['android.permission.XXX']
}
```

Toggle local vs remote plugin via `gradle.properties`: `USE_LOCAL=false` (remote from JitPack) or `true` (local Maven at `./local_maven`).

## Architecture Notes

- **Kotlin-first**: All source is Kotlin with `-Xjvm-default=all` compiler flag for default interface methods.
- **AndroidX + Jetifier**: `android.useAndroidX=true`, `android.enableJetifier=true` in `gradle.properties`.
- **ViewBinding + DataBinding**: Enabled in `app` and `common_base_ui` modules.
- **AIDL**: Enabled in `app` module (`aidl true` in buildFeatures), sources in `app/src/main/aidl/`.
- **Room**: Uses KSP for code generation (`kapt` for Room compiler), schema location at `app/schemas/`.
- **Custom KSP annotations**: Uses `com.github.SevenUpup.annotation:compiler:0.0.13` for additional code generation.
- **Two layout source dirs**: `app/src/main/res` and `app/src/main/res/layout-new` (configured in sourceSets).
- **Signing**: Release builds signed with `common_utils.jks` (included in repo, password: `123456`).
- **ProGuard/R8**: Release builds have `minifyEnabled true` and `shrinkResources true`.

## Publishing

Library modules (`common_base_util`, `common_base_ui`, `easy_navigation`) use `com.vanniktech.maven.publish` plugin v0.23.1, publishing to `https://s01.oss.sonatype.org` (Sonatype S01). POM metadata is configured in `gradle.properties`.

Consumed via JitPack: `com.github.SevenUpup.common_utils:base_util:tag` / `base_ui:tag`.