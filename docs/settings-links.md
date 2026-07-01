# Settings Links

The main product path is opening Android system settings screens instead of trying to control another app's notification behavior directly.

## Current Link Targets

- app notification settings through `Settings.ACTION_APP_NOTIFICATION_SETTINGS`
- app details settings fallback through `Settings.ACTION_APPLICATION_DETAILS_SETTINGS`

## Future Link Targets

- notification listener settings, only if an optional audit mode is added later
- notification policy access settings, only if a future feature truly requires it

## Implementation Notes

Use Android settings intents where available and keep fallbacks for devices that do not support a specific settings screen.

The UI should make it clear that Android system settings are the source of truth and that final changes are made by the user.
