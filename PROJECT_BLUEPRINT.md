# Project Blueprint: Vian Greenhouse (AI Hub Kotlin)

This file serves as a comprehensive map and architectural overview of the project. It is intended for both developers and AI agents to understand the project structure, tech stack, and build processes.

## 1. Project Overview
- **Name**: Vian Greenhouse (AI Hub)
- **Type**: Full-stack Android Application (Kotlin/Jetpack Compose)
- **Primary Goal**: A multi-assistant AI dashboard that integrates various AI services (ChatGPT, Claude, Gemini, etc.) into a single, unified mobile interface using optimized WebViews and Material Design 3.

## 2. Tech Stack
- **Language**: Kotlin 2.0.0
- **UI Framework**: Jetpack Compose with Material Design 3
- **Build System**: Gradle (Kotlin DSL) with Version Catalogs (`libs.versions.toml`)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Storage**: Room Database (for history/settings)
- **CI/CD**: GitHub Actions (Android CI)

## 3. Directory Structure
```text
/ (Root)
├── .github/workflows/      # CI/CD automation
│   └── android.yml         # APK build pipeline
├── kotlin-app/             # Main Android project directory
│   ├── app/                # Application module
│   │   ├── src/main/
│   │   │   ├── java/       # Kotlin source code
│   │   │   │   └── com/foss/aihub/
│   │   │   │       ├── MainActivity.kt    # Main UI entry point
│   │   │   │       ├── db/                # Room Database
│   │   │   │       ├── models/            # Data entities
│   │   │   │       └── viewmodels/        # UI Logic
│   │   │   ├── res/        # App resources (Themes, Strings, Drawables)
│   │   │   │   ├── values/ # themes.xml, strings.xml, colors.xml
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml        # App manifest & permissions
│   │   ├── build.gradle.kts               # Module-level build config
│   │   └── proguard-rules.pro             # ProGuard/R8 rules
│   ├── gradle/
│   │   ├── wrapper/        # Gradle wrapper properties
│   │   └── libs.versions.toml             # Dependency Version Catalog
│   ├── build.gradle.kts    # Root-level build config
│   ├── settings.gradle.kts # Project settings & module inclusion
│   ├── gradle.properties   # Project-wide Gradle settings (AndroidX, JVM)
│   └── gradlew             # Gradle wrapper script
├── .gitignore              # Combined root and Android ignore rules
└── README.md               # Project documentation
```

## 4. Key Components
- **MainActivity.kt**: Manages the Navigation Drawer, Tabbed interface, and the WebView lifecycle.
- **Assistant.kt**: Defines the AI assistant model (Name, URL, Icon, User-Agent).
- **VGHDatabase.kt**: Handles local persistence using Room.
- **WebView Optimization**: Custom `WebViewClient` and `WebChromeClient` implementations to handle AI-specific site behaviors.

## 5. Build & Deployment
- **Local Build**: Open the `kotlin-app` folder in Android Studio.
- **GitHub Build**:
  - Push to `main` or `master` to trigger `Android CI`.
  - Manual trigger available via `workflow_dispatch` in the Actions tab.
  - APK is stored as a build artifact named `app-debug`.

## 6. Development Guidelines
- **Dependencies**: Always add new dependencies to `gradle/libs.versions.toml` first, then reference them in `app/build.gradle.kts`.
- **Styling**: Use Material 3 `ColorScheme` and `Typography` for consistency.
- **Permissions**: Internet permission is required and declared in the manifest.
