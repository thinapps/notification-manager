# Notification Audit

Notification audit is an optional local mode for understanding active app alert behavior.

## Product Rule

The basic settings-helper experience must work without special access.

Audit mode may use Android notification access only after the user enables it in system settings. The app should explain the reason before opening the Android access screen.

## What It Shows

When enabled, audit mode can show active app behavior as:

- `Sound + vibrate`
- `Sound`
- `Vibrate`
- `Alerting`
- `Silent`
- `None`
- `Unknown`

The value is based on active visible notification records and Android ranking/channel information. It is an estimate of active behavior, not a promise that every inactive channel on the device has been inspected.

Audit summaries include the strongest active behavior found for that app, the active notification count, and the most useful evidence Android exposed, such as channel importance or whether sound/vibration was configured.

When an app has mixed active notification states, the summary keeps the strongest state first and adds the other exposed states after `also`.

`Alerting` means Android exposed alert-level importance but did not expose sound or vibration details for the active notification. This is more accurate than blindly calling the notification `Sound`.

Apps with no active visible notification should remain `no active notification` or `Unknown` rather than being guessed.

## Refresh Behavior

When notification access is enabled, the main screen checks whether Android has connected the notification audit listener.

If the listener is connected, the main screen asks the active listener instance to refresh its in-memory snapshot when the main screen resumes. This avoids depending only on listener push events.

If audit access is enabled but the listener is not connected, the main screen ensures the listener component is enabled and requests a listener rebind.

The listener refreshes the in-memory audit snapshot when Android connects it, when a visible notification is posted or removed, and when Android sends ranking updates.

The main screen listens for local audit update broadcasts while visible, then refreshes the audit card and currently visible app rows. This avoids stale `no active notification` rows after returning from Android notification access settings.

Before Android connects the listener or returns the first audit snapshot, the UI should show a waiting state instead of treating every app as having no active notification.

## Data Boundaries

Audit mode should stay local-only.

The app should not:

- store message text
- upload app alert data
- sync app alert data
- sell or share derived data
- use accessibility services to work around Android boundaries
- change another app's channels or settings directly

## Android Behavior Notes

Android notification channels define interruption behavior such as sound, vibration, silent display, and blocked alerts. Users can change channel behavior in Android settings.

Notification access exposes active visible records and ranking data. This is enough for a useful local active-alert audit, but it does not mean the app can safely or completely inspect every inactive channel for every installed app.

## Google Play Notes

Special access should be part of the app's core user-facing purpose and disclosed clearly in the listing and in-app explanation. The feature should request access only when the user chooses audit mode, and the app should respect users who decline it.
