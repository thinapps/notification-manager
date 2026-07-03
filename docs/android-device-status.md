# Android Device Status

Notification Manager shows a small read-only device status section because Android has several global controls that sound similar but do different things.

This document explains the concepts so future app changes stay clear, restrained, and privacy-friendly.

## Product Rule

The app may display device-wide sound, volume, and Do Not Disturb status.

The app should not directly change these settings by default. Android system settings remain the source of truth, and the user should make changes there.

## Sound Mode

Android sound mode is the broad ringer mode for the device.

Notification Manager reads it from `AudioManager.getRingerMode()` and displays one of these plain-English states:

- `Ring` for `AudioManager.RINGER_MODE_NORMAL`
- `Vibrate` for `AudioManager.RINGER_MODE_VIBRATE`
- `Silent` for `AudioManager.RINGER_MODE_SILENT`
- `Unknown` if Android returns an unexpected value

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

Reference: https://developer.android.com/reference/android/app/NotificationManager

## Volume Levels

Volume is another layer.

Notification Manager currently reads:

- ring volume from `AudioManager.STREAM_RING`
- notification volume from `AudioManager.STREAM_NOTIFICATION`
- alarm volume from `AudioManager.STREAM_ALARM`
- call volume from `AudioManager.STREAM_VOICE_CALL`
- media volume from `AudioManager.STREAM_MUSIC`
- system volume from `AudioManager.STREAM_SYSTEM`
- accessibility volume from `AudioManager.STREAM_ACCESSIBILITY`
- keypad tone volume from `AudioManager.STREAM_DTMF`

The app shows each as a percentage plus the raw stream value, such as `60% (9/15)`.

Android devices and OEM skins may link or separate some volume streams. The app should display what Android exposes, not pretend all devices behave the same.

Call volume may be more meaningful while a call-capable route is active, but Android still exposes the stream value. The app labels it plainly as call volume and keeps it read-only.

Keypad tone volume maps to Android's DTMF stream. It is labeled in plain English because most users do not know the DTMF acronym.

Assistant volume is not included because `AudioManager.STREAM_ASSISTANT` is newer than this app's current compile SDK target. Fixed-volume-policy status is intentionally saved for a later version.

## Device Status Layout

Device status rows use a simple label and value layout. Labels stay on the left and values are right-aligned so percentages and raw stream values are easier to scan.

Rows are separated by subtle divider lines aligned to the status text width. The section should still feel like a soft list, not a table or grid.

## Settings Links

The device status text rows are informational only and are not clickable.

The card includes three stacked settings buttons:

- Sound & volume settings opens Android sound settings via `android.settings.SOUND_SETTINGS`
- Do Not Disturb settings opens Android DND / Modes settings via `android.settings.ZEN_MODE_SETTINGS`, falling back to notification settings if unavailable
- System notification settings opens Android notification settings via `android.settings.NOTIFICATION_SETTINGS`

Android does not expose reliable public deep links to each individual OEM volume slider. The app uses the closest stable public settings destination instead of private or OEM-specific sub-settings.

These are shortcuts only. The app does not change device status itself.

## Notification Policy Access

Notification Manager should not request Notification Policy access just to show status or link to Do Not Disturb settings. If a future version considers Do Not Disturb toggles or schedule management, that should be treated as a separate product decision with new docs, permissions review, and clear user-facing explanation.

For now, Do Not Disturb is read-only.
