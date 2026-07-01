# Build

Notification Manager keeps the Android build setup intentionally small because it is a single-module utility app built through GitHub Actions.

## Gradle

The project keeps the simple root `build.gradle` and `settings.gradle` layout because the repository has only one app module.

The repository does not commit Gradle wrapper files. The release workflow downloads Gradle 8.9 and generates the wrapper during the build, matching the current ThinApps utility app pattern.

## Release bundle

`.github/workflows/android-release.yml` builds a signed release AAB through a manual `workflow_dispatch` run.

The release workflow expects the same ThinApps release signing setup used by the other Android utility apps.

## R8

Release minification and resource shrinking are currently disabled in `app/build.gradle`.

If release minification is enabled later, add project-specific rules only when there are real keep rules to maintain.
