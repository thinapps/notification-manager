# Permissions

The initial Android project does not request sensitive runtime permissions.

## Current State

- no notification listener service
- no accessibility service
- no `QUERY_ALL_PACKAGES`
- no internet permission
- no storage permission
- manifest package visibility is limited to launchable apps and the specific Android settings intents the app opens

## Future Permission Review

If the app later needs broader package visibility, notification listener access, or notification policy access, the feature should be added with a clear product reason and matching user-facing explanation.

Notification listener access should remain optional and should not be required for the basic settings-helper experience unless Android exposes too little information without it.
