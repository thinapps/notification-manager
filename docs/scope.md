# Scope

Notification Manager is intended to help users understand and clean up Android notification settings.

The app should focus on settings visibility, explanations, local behavior visibility, and links into Android system settings. It should not promise to directly rewrite another app's notification behavior.

## Current Implementation Status

The current repository has a basic Android/Kotlin app that shows read-only device notification status, lists launchable apps, searches them locally, opens Android notification settings or app info for each app, and includes an optional local audit mode. The audit mode only runs after the user enables Android notification access, uses active visible notification ranking and channel data, keeps audit state in memory, and shows active notification packages first even when they are not normal launcher apps.

The UI is intentionally minimal and does not include Notification Policy access, account features, analytics, ads, network access, accessibility services, or message text storage.

## Current Scope

- show read-only device sound mode, Do Not Disturb mode, ring volume, notification volume, alarm volume, call volume, media volume, system volume, accessibility volume, and keypad tone volume
- list launchable apps on the device
- add Android-exposed active notification packages to the app list, including non-launcher packages when audit access is enabled and active data exists
- show active notification packages before inactive launchable apps
- search apps by display name or package name
- open each app's Android notification settings
- open each app's Android app info screen
- open system sound and volume settings
- open system Do Not Disturb settings
- open system notification settings
- open Android notification access settings for the optional audit mode
- show active behavior by app/package as Sound, Vibrate, Silent, None, or Unknown where Android exposes enough active-notification data
- fall back to Android app details if a notification settings screen is unavailable

## Planned Scope

- improve the app behavior overview where Android exposes it safely
- fixed-volume-policy status, if it proves useful and reliable enough for a later version
- plain-English explanations of notification states
- links to relevant Android notification settings screens
- optional local-only frequency summaries if they can be done without storing message text
- no account, sync, cloud storage, ads SDK, or analytics by default

## Out of Scope

- changing device sound mode directly by default
- toggling Do Not Disturb directly by default
- requesting Notification Policy access just to show status
- auto-muting, auto-blocking, or rewriting another app's notification channels
- reading message contents by default
- uploading notification data
- storing sensitive message text
- changing another app's notification channel settings directly
- acting as an accessibility-based notification controller
