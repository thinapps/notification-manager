# Scope

Notification Manager is intended to help users understand and clean up Android notification settings.

The app should focus on settings visibility, explanations, and links into Android system settings. It should not promise to directly rewrite another app's notification behavior.

## In Scope

- app notification status overview
- plain-English explanations of notification states
- links to relevant Android notification settings screens
- optional local-only auditing of notification frequency if the user explicitly grants notification access later
- no account, sync, cloud storage, ads SDK, or analytics by default

## Out of Scope

- reading notification message contents by default
- uploading notification data
- storing OTPs, private messages, or other sensitive notification text
- changing another app's notification channel settings directly
- acting as an accessibility-based notification controller
