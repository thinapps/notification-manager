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
| [Settings Links](docs/settings-links.md) | Explains the Android system settings deep-link approach. |

## Current Scope

- list launchable apps on the device
- open Android notification settings for a selected app
- fall back to Android app details if direct notification settings are unavailable
- avoid notification listener, accessibility, internet, storage, analytics, ads, account, and sync features

## Planned Scope

- show app notification status where Android exposes it safely
- explain common notification states such as allowed, blocked, silent, sound, vibration, badges, lock screen, and pop-up behavior
- link users to Android system settings for each app or notification area
- optionally add local-only notification frequency auditing later if the user explicitly grants notification access

## Project Status

The repository has a basic Android/Kotlin app that lists launchable apps and opens Android notification settings for each app. The UI is intentionally minimal and the project does not include notification reading, notification listener services, account features, analytics, ads, or network access.

## Changelog

### 0.1.0
- initialized the repository with basic project notes
- documented the intended notification-settings-helper scope
- documented that notification contents are intentionally out of scope
- added a minimal Android/Kotlin project shell
- added basic app metadata, resources, backup rules, and launcher placeholders
- added build, scope, privacy, permissions, and settings-link docs
- added a basic debug build workflow
- added launchable-app discovery without `QUERY_ALL_PACKAGES`
- added app notification settings deep links with app-details fallback
- replaced the setup placeholder with a minimal app list screen
