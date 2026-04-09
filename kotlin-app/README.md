# AI Hub Kotlin Project

This is a pure Kotlin (Jetpack Compose) re-implementation of the AI Hub application.

## Features
- **Tabbed Interface:** Switch between multiple AI assistants (ChatGPT, Claude, Gemini, etc.) using a navigation drawer.
- **WebView Integration:** Each assistant is loaded in a high-performance WebView with optimized settings (JavaScript, DOM storage, custom User-Agent).
- **Material Design 3:** Built with the latest Android design standards.
- **Edge-to-Edge:** Modern immersive UI.

## GitHub & CI/CD
This project is configured for **GitHub Actions**. When you push this code to a GitHub repository:
1.  **Auto-Build**: GitHub will automatically trigger a build.
2.  **APK Generation**: An Android APK (`app-debug.apk`) will be generated as a build artifact.
3.  **Download**: You can download the APK from the "Actions" tab in your GitHub repository.

## How to Use
1.  **Move to GitHub**: Initialize a Git repository in the `kotlin-app` directory and push to GitHub.
2.  **Open in Android Studio**: Alternatively, open the `kotlin-app` directory in Android Studio for local development.
3.  **Build**: Let Gradle sync and build the project.
4.  **Run**: Deploy to an emulator or physical device.

## File Structure
- `app/build.gradle.kts`: Project dependencies and configuration.
- `app/src/main/AndroidManifest.xml`: App permissions and activity declaration.
- `app/src/main/java/com/foss/viangreenhouse/MainActivity.kt`: The main entry point and UI logic.
- `app/src/main/java/com/foss/viangreenhouse/models/Assistant.kt`: Data model for the AI assistants.
- `gradle/libs.versions.toml`: Version catalog for dependencies.

## Note
This is a re-implementation based on the core features of the AI Hub project. Since the original source code could not be directly fetched from the repository, this version provides a clean, modern starting point that mirrors the original's functionality.
