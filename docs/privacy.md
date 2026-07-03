# Privacy

Notification Manager should be designed as a local-only utility.

## Default Privacy Model

- no account
- no cloud sync
- no analytics SDK
- no advertising SDK
- no network permission unless a future feature explicitly requires it
- no notification content access by default

## Device Status

The device status card reads local Android system state only:

- sound mode
- Do Not Disturb interruption filter
- ring volume
- notification volume
- alarm volume
- call volume

This status is displayed on screen and is not stored, synced, uploaded, or sent anywhere.

The app does not request Notification Policy access to show Do Not Disturb status and does not toggle DND by default.

## Notification Access

Notification listener access should stay optional. If it is added later, the app should use it only for clearly explained local features, such as counting how often apps notify the user.

Notification contents should not be stored, synced, uploaded, or shown as a core feature of this app.
