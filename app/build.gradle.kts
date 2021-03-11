plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdkVersion(Config.targetSdk)
  buildToolsVersion = Config.buildTools

  defaultConfig {
    applicationId = "hu.ma.charts.sample"
    minSdkVersion(Config.minSdk)
    targetSdkVersion(Config.targetSdk)
    versionCode = 1
    versionName = "1.0"
  }

  buildFeatures {
    compose = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
    useIR = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = Dependencies.Compose.version
  }
}

dependencies {
  implementation(project(":charts"))

  arrayOf(
    AndroidX.activityKtx,
    Google.android.material,
    Dependencies.Compose.runtime,
    Dependencies.Compose.activity,
    Dependencies.Compose.ui,
    Dependencies.Compose.foundation,
    Dependencies.Compose.layout,
    Dependencies.Compose.material,
    Dependencies.Compose.iconsExtended,
    Dependencies.Compose.animation,
    Dependencies.Compose.util,
    Dependencies.Compose.Navigation.core
  ).forEach {
    implementation(it)
  }
}
