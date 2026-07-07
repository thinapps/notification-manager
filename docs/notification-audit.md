# Notification Audit

Notification audit is an optional local mode for understanding active app alert behavior.

## Product Rule

The basic settings-helper experience must work without special access.

Audit mode may use Android notification access only after the user enables it in system settings. The app should explain the reason before opening the Android access screen.

## What It Shows

Every app row shows an audit status line so the state is explicit:

- `disabled` when notification access is not enabled
- `waiting for listener connection` when access is enabled but Android has not connected the listener
- `waiting for active notification data` when the listener is connected but no snapshot has returned yet
- `no active visible notification exposed` when a snapshot exists but that app/package has no active visible notification exposed by Android
- channel/ranking evidence when that app/package has active visible notifications

When Android exposes active visible notifications, those active packages are added to the list and shown first, even if they are not normal launcher apps. This is important because system apps, services, carrier apps, or other non-launcher packages can own active notifications.

The app resolves active package labels through Android PackageManager when possible. If Android does not return a label, the package name is shown as the fallback so the active notification package is still visible.

The audit card also shows a trace line with the active notification count returned by Android, the active package count after grouping that snapshot, a short sample of active package names, and the last refresh status.

This trace is local and plain so test builds can distinguish between Android returning zero active notifications and Android returning active packages that do not match the visible app rows.

When enabled and active data exists, audit mode can show active app channel/ranking evidence as:

- `Sound + vibrate`
- `Sound`
- `Vibrate`
- `Alerting`
- `Silent`
- `None`
- `Unknown`

The value is based on active visible notification records and Android ranking/channel information. It is an estimate of active channel behavior, not a promise that every inactive channel on the device has been inspected.

Audit summaries separate `Channel` evidence from `Device` result. `Channel` describes what Android exposes about the active notification's ranking and channel. `Device: not verified` means the app is not claiming what the user actually heard, felt, or saw.

Audit summaries include the strongest active behavior found for that app, the active notification count, and the most useful evidence Android exposed, such as channel importance or whether sound/vibration was configured.

When an app has mixed active notification states, the summary keeps the strongest state first and adds the other exposed states after `also`.

`Alerting` means Android exposed alert-level importance but did not expose sound or vibration details for the active notification. This is more accurate than blindly calling the notification `Sound`.

Apps/packages with no active visible notification should remain `no active visible notification exposed` or `Unknown` rather than being guessed.

## Refresh Behavior

When notification access is enabled, the main screen checks whether Android has connected the notification audit listener.

The main screen checks Android's enabled notification listener list when deciding whether notification access is enabled. This keeps disabled, waiting, and active audit row states visible instead of hiding audit rows entirely.

If the listener is connected, the main screen asks the active listener instance to refresh its in-memory snapshot when the main screen resumes. This avoids depending only on listener push events.

If audit access is enabled but the listener is not connected, the main screen ensures the listener component is enabled and requests a listener rebind.

The listener refreshes the in-memory audit snapshot when Android connects it, when a visible notification is posted or removed, and when Android sends ranking updates.

The main screen listens for local audit update broadcasts while visible, then refreshes the audit card and currently visible app rows. This avoids stale `no active visible notification exposed` rows after returning from Android notification access settings.

Before Android connects the listener or returns the first audit snapshot, the UI should show a waiting state instead of treating every app as having no active visible notification exposed.

## Data Boundaries

Audit mode should stay local-only.

The app should not store notification message text, upload audit data, sync audit data, use accessibility services to work around Android boundaries, or change another app's channels or settings directly.

## Android Behavior Notes

Android notification channels define interruption behavior such as sound, vibration, silent display, and blocked alerts. Users can change channel behavior in Android settings.

Notification access exposes active visible records and ranking data. This is enough for a useful local active-alert audit, but it does not mean the app can safely or completely inspect every inactive channel for every installed app.

## Google Play Notes

Special access should be part of the app's core user-facing purpose and disclosed clearly in the listing and in-app explanation. The feature should request access only when the user chooses audit mode, and the app should respect users who decline it.
