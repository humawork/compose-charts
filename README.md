# compose-charts
[![Build](https://github.com/humawork/compose-charts/workflows/Build/badge.svg)](https://github.com/humawork/compose-charts/actions)
[![Version](https://api.bintray.com/packages/humawork/maven/compose-charts/images/download.svg) ](https://bintray.com/humawork/maven/compose-charts/_latestVersion)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# What's this?
Jetpack Compose charts library for Android

# Disclaimer
Current supported Jetpack Compose version: `1.0.0-alpha12`

This library is WIP, and at the moment only contains the following charts:
- `PieChart`
- `Table`
- `HorizontalBarsChart`

## Download

Available through bintray.

Add the maven repo to your root `build.gradle`

```groovy
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/humawork/maven' }
    }
}
```

Add the dependency:

```groovy
implementation 'hu.ma.compose:charts:{latest-version}'
```

# Samples
Charts samples can be found in the [app](app) module
