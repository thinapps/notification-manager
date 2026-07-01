# Settings Links

The main product path should be opening Android system settings screens instead of trying to control another app's notification behavior directly.

## Planned Link Targets

- app notification settings
- app details settings
- notification listener settings, only if an optional audit mode is added later
- notification policy access settings, only if a future feature truly requires it

## Implementation Notes

Use Android settings intents where available and keep fallbacks for devices that do not support a specific settings screen.

The UI should make it clear that Android system settings are the source of truth and that final changes are made by the user.
