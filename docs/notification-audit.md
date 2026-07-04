# Notification Audit

Notification audit is an optional local mode for understanding active app alert behavior.

## Product Rule

The basic settings-helper experience must work without special access.

Audit mode may use Android notification access only after the user enables it in system settings. The app should explain the reason before opening the Android access screen.

## What It Shows

When enabled, audit mode can show active app behavior as:

- `Sound`
- `Vibrate`
- `Silent`
- `None`
- `Unknown`

The value is based on active visible notification records and Android ranking/channel information. It is an estimate of active behavior, not a promise that every inactive channel on the device has been inspected.

Apps with no active visible notification should remain `no active notification` or `Unknown` rather than being guessed.

## Data Boundaries

Audit mode should stay local-only.

The app should not:

- store message text
- upload app alert data
- sync app alert data
- sell or share derived data
- use accessibility services to work around Android boundaries
- change another app's channels or settings directly

## Future Consideration

The app currently refreshes visible audit rows when the main screen is created or resumed and when the app list is re-rendered. A future patch may add a lightweight live refresh while the main screen remains open, but only if testing shows the audit rows feel stale. This is a UX polish consideration, not a required Android best practice.

## Android Behavior Notes

Android notification channels define interruption behavior such as sound, vibration, silent display, and blocked alerts. Users can change channel behavior in Android settings.

Notification access exposes active visible records and ranking data. This is enough for a useful local active-alert audit, but it does not mean the app can safely or completely inspect every inactive channel for every installed app.

## Google Play Notes

Special access should be part of the app's core user-facing purpose and disclosed clearly in the listing and in-app explanation. The feature should request access only when the user chooses audit mode, and the app should respect users who decline it.
