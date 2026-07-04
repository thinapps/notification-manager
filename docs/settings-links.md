# Settings Links

The main product path is opening Android system settings screens instead of trying to adjust another app's notification behavior directly.

## Current Link Targets

- system notification settings through the `android.settings.NOTIFICATION_SETTINGS` action string
- system sound and volume settings through the `android.settings.SOUND_SETTINGS` action string
- system Do Not Disturb / Modes settings through `android.settings.ZEN_MODE_SETTINGS`, with notification settings fallback
- Android access settings for optional audit mode through `android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS`, with general Android settings fallback
- app notification settings through `Settings.ACTION_APP_NOTIFICATION_SETTINGS`
- app details settings through `Settings.ACTION_APPLICATION_DETAILS_SETTINGS`
- general Android settings fallback through `Settings.ACTION_SETTINGS`

## Link Restraint

The app should link users to normal Android settings screens by default. Special access screens should only be linked when tied to a clear feature, such as the optional audit card.

Do Not Disturb status and the Do Not Disturb settings shortcut are read-only from the app's perspective. The app intentionally does not link to Notification Policy access as a default path because that screen is for granting apps management of Do Not Disturb / Modes configuration, not merely helping users understand or adjust their current state in Android settings.

Device status text rows are informational only. Sound, Do Not Disturb, and notification settings are opened from the three stacked buttons in the device status card. Android does not expose stable public deep links for each individual OEM volume slider, so the app should avoid private activity names and manufacturer-specific routes.

## Button Feedback

Settings and app action buttons use standard Android haptic feedback and standard ripple feedback on tap. The feedback is small, local, and does not require any permission.

## Future Link Targets

- Notification Policy access settings, only if a future feature truly requires Do Not Disturb or Modes management

## Implementation Notes

Use Android settings intents where available and keep fallbacks for devices that do not support a specific settings screen.

Some Android settings action constants are used as raw action strings when that is more compatible with the current build setup. The raw strings still represent the Android platform settings actions.

The UI should make it clear that Android system settings are the source of truth and that final changes are made by the user.
