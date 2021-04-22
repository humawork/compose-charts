plugins {
  id("com.android.library")
  kotlin("android")
  `maven-publish`
}

apply(from = rootProject.file(".buildscript/configure-publishing.gradle"))

android {
  compileSdkVersion(Config.targetSdk)
  buildToolsVersion = Config.buildTools

  defaultConfig {
    minSdkVersion(Config.minSdk)
    targetSdkVersion(Config.targetSdk)
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
  arrayOf(
    Dependencies.Compose.runtime,
    Dependencies.Compose.ui,
    Dependencies.Compose.foundation,
    Dependencies.Compose.layout,
    Dependencies.Compose.material,
    Dependencies.Compose.iconsExtended,
    Dependencies.Compose.animation,
    Dependencies.Compose.tooling,
    Dependencies.Compose.util,
    Dependencies.Accompanist.flow
  ).forEach {
    implementation(it)
  }
}
