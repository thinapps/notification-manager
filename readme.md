# Notification Manager

Privacy-friendly Android utility for reviewing app notification settings in one place and opening the relevant Android system settings screens to adjust them.

The app is intended as a notification settings overview, not a notification content reader or background notification controller.

## Documentation

| Document | Description |
| --- | --- |
| [Build](docs/build.md) | Explains the basic Gradle project, debug and release workflows, signing, R8, and ProGuard choices. |
| [Scope](docs/scope.md) | Defines the settings-helper product scope and what the app should not attempt to do. |
| [Privacy](docs/privacy.md) | Explains the local-only privacy model and notification-content boundaries. |
| [Permissions](docs/permissions.md) | Explains the initial no-sensitive-permissions setup and future permission review rules. |
| [Settings Links](docs/settings-links.md) | Explains the Android system settings deep-link approach. |

## Current Scope

- list launchable apps on the device
- search apps by name or package name
- open Android notification settings for a selected app
- open Android app info for a selected app
- open system notification settings
- avoid notification listener, accessibility, internet, storage, analytics, ads, account, and sync features

## Planned Scope

- show app notification status where Android exposes it safely
- explain common notification states such as allowed, blocked, silent, sound, vibration, badges, lock screen, and pop-up behavior
- link users to Android system settings for each app or notification area
- optionally add local-only notification frequency auditing later if the user explicitly grants notification access

## Project Status

The repository has a basic Android/Kotlin app that lists launchable apps, searches them locally, and opens Android notification settings or app info for each app. The UI is intentionally minimal and the project does not include notification reading, notification listener services, account features, analytics, ads, or network access.

## Changelog

### 0.1.0
- added a minimal Android/Kotlin app shell for Notification Manager
- added local launchable-app discovery without `QUERY_ALL_PACKAGES`
- added app search by name or package name
- added per-app notification settings and app-info shortcuts
- added a system notification settings shortcut
- added manual debug APK and signed release AAB workflows
- documented the privacy, permission, scope, settings-link, and build setup
