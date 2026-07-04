# Permissions

Notification Manager does not request normal runtime permissions by default.

## Current State

- optional notification listener service for local audit mode
- no accessibility service
- no Notification Policy access request
- no `QUERY_ALL_PACKAGES`
- no internet permission
- no storage permission
- manifest package visibility is limited to launchable apps and the specific Android settings intents the app opens

The device status card reads Android-provided sound mode, Do Not Disturb interruption filter, and supported volume streams: ring, notification, alarm, call, media, system, accessibility, and keypad tone. These are read-only status checks and do not require Notification Policy access.

The notification audit mode uses Android notification access only after the user enables it in system settings. It is optional, local-only, and used to estimate active app notification behavior from visible active notifications and ranking/channel information. The app should not store message text or upload audit data.

## Permission Review Rules

Notification access must stay tied to the app's user-facing notification behavior overview. The app should explain why access is useful before sending the user to Android notification access settings.

If a future feature needs broader package visibility, Notification Policy access, internet access, or storage access, it should be added with a clear product reason and matching user-facing explanation.

Notification Policy access should not be requested merely to display Do Not Disturb status. It should only be considered if the product explicitly adds DND control or schedule management, and that would require a separate review.
