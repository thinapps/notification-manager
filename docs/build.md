# Build

Notification Manager is a small classic Android project using Gradle, Kotlin, and Android XML/resources.

## Requirements

- Android Studio or command-line Android SDK
- JDK 17
- Android SDK platform for the configured `compileSdk`

## Local Build

```bash
gradle assembleDebug
```

## Release Build

Release signing is configured through environment variables and a local keystore path that is not committed to the repository.

Required environment variables:

- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The release keystore path is:

```text
keystore/release.keystore
```

## Notes

The repository does not include generated build outputs, local SDK config, keystores, or IDE metadata.
