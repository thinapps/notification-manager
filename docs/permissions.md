# Permissions

Notification Manager does not request sensitive runtime permissions by default.

## Current State

- no notification listener service
- no accessibility service
- no Notification Policy access request
- no `QUERY_ALL_PACKAGES`
- no internet permission
- no storage permission
- manifest package visibility is limited to launchable apps and the specific Android settings intents the app opens

The device status card reads Android-provided sound mode, Do Not Disturb interruption filter, and supported volume streams: ring, notification, alarm, call, media, system, accessibility, and keypad tone. These are read-only status checks and do not require notification listener access or Notification Policy access.

## Future Permission Review

If the app later needs broader package visibility, notification listener access, or Notification Policy access, the feature should be added with a clear product reason and matching user-facing explanation.

Notification listener access should remain optional and should not be required for the basic settings-helper experience unless Android exposes too little information without it.

Notification Policy access should not be requested merely to display Do Not Disturb status. It should only be considered if the product explicitly adds DND control or schedule management, and that would require a separate review.
