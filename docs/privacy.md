# Privacy

Notification Manager should be designed as a local-only utility.

## Default Privacy Model

- no account
- no cloud sync
- no analytics SDK
- no advertising SDK
- no network permission unless a future feature explicitly requires it
- no notification content access by default

## Notification Access

Notification listener access should stay optional. If it is added later, the app should use it only for clearly explained local features, such as counting how often apps notify the user.

Notification contents should not be stored, synced, uploaded, or shown as a core feature of this app.
