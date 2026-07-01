# Notification Manager

Privacy-friendly Android utility for reviewing app notification settings in one place and opening the relevant Android system settings screens to adjust them.

The app is intended as a notification settings overview, not a notification content reader or background notification controller.

## Documentation

| Document | Description |
| --- | --- |
| [Build](docs/build.md) | Explains the basic Gradle project, local build command, and release signing assumptions. |
| [Scope](docs/scope.md) | Defines the settings-helper product scope and what the app should not attempt to do. |
| [Privacy](docs/privacy.md) | Explains the local-only privacy model and notification-content boundaries. |
| [Permissions](docs/permissions.md) | Explains the initial no-sensitive-permissions setup and future permission review rules. |
| [Settings Links](docs/settings-links.md) | Explains the planned Android system settings deep-link approach. |

## Planned Scope

- show installed apps with their notification access/status where Android exposes it
- explain common notification states such as allowed, blocked, silent, sound, vibration, badges, lock screen, and pop-up behavior
- link users to Android system settings for each app or notification area
- avoid reading, storing, syncing, or uploading notification contents
- keep the project local-only and lightweight

## Project Status

The repository now has a basic Android/Kotlin project shell with a placeholder launcher activity, basic resources, documentation, and a debug build workflow. Real notification-settings logic and app design have not been added yet.

## Changelog

### 0.1.0
- initialized the repository with basic project notes
- documented the intended notification-settings-helper scope
- documented that notification contents are intentionally out of scope
- added a minimal Android/Kotlin project shell
- added basic app metadata, resources, backup rules, and launcher placeholders
- added build, scope, privacy, permissions, and settings-link docs
- added a basic debug build workflow
