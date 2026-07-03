# Android Device Status

Notification Manager shows a small read-only device status section because Android has several global controls that sound similar but do different things.

This document explains the concepts so future app changes stay clear, restrained, and privacy-friendly.

## Product Rule

The app may display device-wide sound, volume, and Do Not Disturb status.

The app should not directly toggle these settings by default. Android system settings remain the source of truth, and the user should make changes there.

## Sound Mode

Android sound mode is the broad ringer mode for the device.

Notification Manager reads it from `AudioManager.getRingerMode()` and displays one of these plain-English states:

- `Ring` for `AudioManager.RINGER_MODE_NORMAL`
- `Vibrate` for `AudioManager.RINGER_MODE_VIBRATE`
- `Silent` for `AudioManager.RINGER_MODE_SILENT`
- `Unknown` if Android returns an unexpected value

Android's API docs describe normal mode as potentially audible, vibrate mode as silent with vibration, and silent mode as silent without vibration.

Reference: https://developer.android.com/reference/android/media/AudioManager

## Do Not Disturb

Do Not Disturb is separate from sound mode.

Android exposes it as the current notification interruption filter through `NotificationManager.getCurrentInterruptionFilter()`.

Notification Manager displays:

- `Off` for `NotificationManager.INTERRUPTION_FILTER_ALL`
- `Priority only` for `NotificationManager.INTERRUPTION_FILTER_PRIORITY`
- `Alarms only` for `NotificationManager.INTERRUPTION_FILTER_ALARMS`
- `Total silence` for `NotificationManager.INTERRUPTION_FILTER_NONE`
- `Unknown` for `NotificationManager.INTERRUPTION_FILTER_UNKNOWN` or any unexpected value

Android's API docs describe the interruption filter as a global rule for which notifications may interrupt the user through sound or vibration.

Reference: https://developer.android.com/reference/android/app/NotificationManager

## Volume Levels

Volume is another layer.

Notification Manager currently reads:

- ring volume from `AudioManager.STREAM_RING`
- notification volume from `AudioManager.STREAM_NOTIFICATION`
- alarm volume from `AudioManager.STREAM_ALARM`
- call volume from `AudioManager.STREAM_VOICE_CALL`

The app shows each as a percentage plus the raw stream value, such as `60% (9/15)`.

Android devices and OEM skins may link or separate some volume streams. The app should display what Android exposes, not pretend all devices behave the same.

Call volume may be more meaningful while a call-capable route is active, but Android still exposes the stream value. The app labels it plainly as call volume and keeps it read-only.

## Settings Links

The status card links each status row to the closest relevant Android settings screen:

- sound mode opens Android sound settings via `android.settings.SOUND_SETTINGS`
- ring volume opens Android sound settings via `android.settings.SOUND_SETTINGS`
- notification volume opens Android sound settings via `android.settings.SOUND_SETTINGS`
- alarm volume opens Android sound settings via `android.settings.SOUND_SETTINGS`
- call volume opens Android sound settings via `android.settings.SOUND_SETTINGS`
- Do Not Disturb opens Android DND / Modes settings via `android.settings.ZEN_MODE_SETTINGS`, falling back to notification settings if unavailable

The card also includes Sound settings and Notification settings buttons for clarity.

These are shortcuts only. The app does not change device status itself.

## Notification Policy Access

Do Not Disturb control requires Notification Policy access.

Notification Manager should not request Notification Policy access just to show status or link to DND settings. If a future version considers DND toggles or schedule management, that should be treated as a separate product decision with new docs, permissions review, and clear user-facing explanation.

For now, DND is read-only.
