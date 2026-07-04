# Privacy

Notification Manager should be designed as a local-only utility.

## Default Privacy Model

- no account
- no cloud sync
- no analytics SDK
- no advertising SDK
- no network permission unless a future feature explicitly requires it
- no notification access required for the basic settings-helper experience

## Device Status

The device status card reads local Android system state only:

- sound mode
- Do Not Disturb interruption filter
- ring volume
- notification volume
- alarm volume
- call volume
- media volume
- system volume
- accessibility volume
- keypad tone volume

This status is displayed on screen and is not stored, synced, uploaded, or sent anywhere.

The app does not request Notification Policy access to show Do Not Disturb status and does not toggle DND by default.

## Optional Notification Audit

Notification access is optional and only used for the local audit mode.

When enabled, the app reads active visible notification metadata exposed by Android so it can estimate whether an app's active notification behavior is Sound, Vibrate, Silent, None, or Unknown.

Audit state is kept in memory only. The app should not persist message text, sync notification data, upload notification data, or sell/share notification-derived data.

The audit mode should remain explainable from the UI before the user is sent to Android notification access settings.
