# Notification Manager

Privacy-friendly Android utility for reviewing device notification status and app notification settings in one place.

The app is intended as a notification settings overview, not a notification content reader or background notification controller.

## Documentation

| Document | Description |
| --- | --- |
| [Build](docs/build.md) | Explains the basic Gradle project, release workflow, signing, and R8 choices. |
| [Scope](docs/scope.md) | Defines the settings-helper product scope and what the app should not attempt to do. |
| [Android Device Status](docs/android-device-status.md) | Explains sound mode, Do Not Disturb, volume streams, and why the app keeps them read-only. |
| [Privacy](docs/privacy.md) | Explains the local-only privacy model and notification-content boundaries. |
| [Permissions](docs/permissions.md) | Explains the no-sensitive-permissions setup and future permission review rules. |
| [Settings Links](docs/settings-links.md) | Explains the Android system settings deep-link approach. |

## Current Scope

- show read-only device sound mode, Do Not Disturb mode, ring volume, notification volume, alarm volume, call volume, media volume, system volume, accessibility volume, and keypad tone volume
- list launchable apps on the device
- search apps by name or package name
- open Android notification settings for a selected app
- open Android app info for a selected app
- open system sound and volume settings
- open system Do Not Disturb settings
- open system notification settings
- avoid notification listener, Notification Policy access, internet, storage, analytics, ads, account, and sync features

## Planned Scope

- show app notification status where Android exposes it safely
- explain common notification states such as allowed, blocked, silent, sound, vibration, badges, lock screen, and pop-up behavior
- link users to Android system settings for each app or notification area
- optionally add local-only notification frequency auditing later if the user explicitly grants notification access

## Project Status

The repository has a basic Android/Kotlin app that shows read-only device notification status, lists launchable apps, searches them locally, and opens Android notification settings or app info for each app. The UI is intentionally minimal and the project does not include notification reading, notification listener services, Notification Policy access, account features, analytics, ads, or network access.

## Changelog

### 0.2.2
- adds a third stacked device-status shortcut labeled Do Not Disturb Settings
- routes the new Do Not Disturb Settings button to the same Android DND / Modes settings destination as the DND status row
- keeps Do Not Disturb read-only with no Notification Policy access request or toggle

### 0.2.1
- added read-only alarm volume, call volume, media volume, system volume, accessibility volume, and keypad tone volume rows to the device status card
- made the added volume rows clickable and routed them to Android sound settings
- renamed the settings shortcuts to Sound & volume settings and System notification settings, stacked in that order
- kept all device-wide controls read-only with no new permissions or toggles

### 0.2.0
- added a read-only device status card for sound mode, Do Not Disturb mode, ring volume, and notification volume
- made all device status rows clickable and routed them to the closest relevant Android settings screen
- added a sound settings shortcut beside the existing notification settings shortcut
- kept device-wide controls read-only with no DND toggles or Notification Policy access request

### 0.1.0
- added a minimal Android/Kotlin app shell for Notification Manager
- added local launchable-app discovery without `QUERY_ALL_PACKAGES`
- added app search by name or package name
- added per-app notification settings and app-info shortcuts
- added a system notification settings shortcut
- added a manual signed release AAB workflow
- documented the privacy, permission, scope, settings-link, and build setup
