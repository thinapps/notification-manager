# Notification Manager

Privacy-friendly Android utility for reviewing device notification status and app notification settings in one place.

The app is intended as a notification settings overview with an optional local notification audit mode, not a notification content reader or background notification controller.

## Documentation

| Document | Description |
| --- | --- |
| [Build](docs/build.md) | Explains the basic Gradle project, release workflow, signing, and R8 choices. |
| [Scope](docs/scope.md) | Defines the settings-helper and optional audit product scope. |
| [Android Device Status](docs/android-device-status.md) | Explains sound mode, Do Not Disturb, volume streams, and why the app keeps them read-only. |
| [Notification Audit](docs/notification-audit.md) | Explains the optional local-only active notification behavior audit. |
| [Privacy](docs/privacy.md) | Explains the local-only privacy model and notification-content boundaries. |
| [Permissions](docs/permissions.md) | Explains the permission setup and future permission review rules. |
| [Settings Links](docs/settings-links.md) | Explains the Android system settings deep-link approach. |

## Changelog

### 0.3.0
- added an optional local notification audit service for active visible notifications after the user enables Android notification access
- added a notification audit card with a link to Android notification access settings
- shows per-app active notification behavior as Sound, Vibrate, Silent, None, or Unknown when audit access is enabled
- refreshes visible audit rows from listener updates after Android connects the audit service or notification state changes
- shows a waiting state before the first audit snapshot instead of treating every app as having no active notification
- keeps notification audit data in memory only and does not store notification text

### 0.2.3
- extended the device status divider lines so they align with the row text edges
- made device status labels secondary gray and values primary medium-weight text for clearer scanning
- added standard haptic feedback and standard ripple feedback to settings and app action buttons

### 0.2.2
- split device status rows into left labels and right-aligned values for easier scanning
- added subtle inset divider lines between device status rows without turning the section into a table
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
