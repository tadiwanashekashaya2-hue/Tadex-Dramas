# Tadex AI Drama Studio

An Android starter project inspired by the supplied drama-creator screenshot. It includes a dark creator dashboard, tool selection, prompt composer, genre and aspect-ratio controls, saved project list, and AI provider settings.

## Included tools
- AI Drama
- AI Video
- AI Photo
- AI Effects
- AI Voice
- AI Story Writer
- AI Poster Maker
- AI Photo Animator

## Important: AI generation
This project is a working Android UI and prompt/project manager. It does **not** claim to generate AI videos by itself. Real image/video/voice generation requires an AI provider account, API access, and provider-specific integration. The Settings screen is a placeholder for provider configuration; entering a key alone does not connect an arbitrary provider.

For production, use a secure backend. Do not embed a private provider API key in a public APK. Add a backend endpoint that authenticates your app, validates prompts, calls the chosen AI provider, and returns job IDs/media URLs. Each provider has its own request format and pricing.

## Build an APK using only your Android phone

### Option A: GitHub Actions (recommended)
1. Extract this ZIP on your phone.
2. Create/sign in to GitHub in your mobile browser.
3. Create a new repository, for example `tadex-ai-drama-studio`.
4. Upload the extracted project files and folders to the repository (not the ZIP file itself).
5. Open the repository's **Actions** tab.
6. Select **Build Android APK** and tap **Run workflow**.
7. Wait for the workflow to finish successfully.
8. Open the completed workflow run, scroll to **Artifacts**, and download `Tadex-AI-Drama-Studio-debug-apk`.
9. Extract the downloaded artifact ZIP and install `app-debug.apk` on your Android phone. If Android asks, allow installation from that browser/file manager.

### Option B: Android Studio
Open the project folder in Android Studio, wait for Gradle sync, then choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**. The debug APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

## Project details
- Package: `com.tadex.dramastudio`
- Minimum Android: Android 6.0 (API 23)
- Compile SDK: 35
- Version: 1.0.0

## Next development steps for a real AI product
1. Choose a video/image/voice provider that offers an API.
2. Build a secure backend and keep provider secrets on the server.
3. Implement that provider's documented job-create and job-status endpoints.
4. Add media preview, download, progress, usage limits, and error handling.
5. Add privacy policy, terms, and signing before publishing.
