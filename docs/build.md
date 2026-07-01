# Build

Notification Manager keeps the Android build setup intentionally small because it is a single-module utility app built through GitHub Actions.

## Gradle

The project keeps the simple root `build.gradle` and `settings.gradle` layout because the repository has only one app module.

The repository does not commit Gradle wrapper files. The GitHub Actions workflows download Gradle 8.9 and generate the wrapper during the build, matching the current ThinApps utility app pattern.

## Debug APK

`.github/workflows/android-debug.yml` builds a debug APK through a manual `workflow_dispatch` run. Use this workflow for first install testing because it does not need release signing values.

## Release bundle

`.github/workflows/android-release.yml` builds a signed release AAB through a manual `workflow_dispatch` run.

The release workflow expects the same ThinApps release signing secrets used by the other Android utility apps.

## R8 and ProGuard

R8 and ProGuard rules are not currently needed because release minification and resource shrinking are disabled in `app/build.gradle`.

The app is small, has no reflection-heavy third-party SDKs, and does not need custom keep rules right now. Keeping an empty `proguard-rules.pro` file only adds confusion, so the file is intentionally absent.

If release minification is enabled later, add a new `app/proguard-rules.pro` file only when there are real keep rules to maintain.
