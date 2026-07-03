# Scope

Notification Manager is intended to help users understand and clean up Android notification settings.

The app should focus on settings visibility, explanations, and links into Android system settings. It should not promise to directly rewrite another app's notification behavior.

## Current Scope

- show read-only device sound mode, Do Not Disturb mode, ring volume, and notification volume
- list launchable apps on the device
- search apps by display name or package name
- open each app's Android notification settings
- open each app's Android app info screen
- open system notification settings
- open system sound settings
- fall back to Android app details if a notification settings screen is unavailable

## Planned Scope

- app notification status overview where Android exposes it safely
- plain-English explanations of notification states
- links to relevant Android notification settings screens
- optional local-only auditing of notification frequency if the user explicitly grants notification access later
- no account, sync, cloud storage, ads SDK, or analytics by default

## Out of Scope

- changing device sound mode directly by default
- toggling Do Not Disturb directly by default
- requesting Notification Policy access just to show status
- reading notification message contents by default
- uploading notification data
- storing OTPs, private messages, or other sensitive notification text
- changing another app's notification channel settings directly
- acting as an accessibility-based notification controller
