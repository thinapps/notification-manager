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

## Changelog

### 0.2.2
- split device status rows into left labels and right-aligned values for easier scanning
- added subtle divider lines aligned with the status text without turning the section into a table
- added a shared dark button style for settings and app action buttons
- kept device status rows informational only, with settings opened from the three stacked buttons

### 0.2.1
- added read-only alarm volume, call volume, media volume, system volume, accessibility volume, and keypad tone volume rows to the device status card
- kept device status text rows informational only, with settings opened from the stacked buttons instead of row taps
- renamed and stacked the settings shortcuts as Sound & volume settings, Do Not Disturb settings, and System notification settings
- kept all device-wide controls read-only with no new permissions, no Notification Policy access request, and no toggles

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
