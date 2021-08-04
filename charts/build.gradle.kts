import com.vanniktech.maven.publish.SonatypeHost

plugins {
  id("com.android.library")
  kotlin("android")
  id("com.vanniktech.maven.publish")
}

android {
  compileSdk = Config.targetSdk

  defaultConfig {
    minSdk = Config.minSdk
    targetSdk = Config.targetSdk
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
    Dependencies.Compose.toolingPreview,
    Dependencies.Compose.util,
    Dependencies.Accompanist.flow
  ).forEach {
    implementation(it)
  }
}

mavenPublish {
  sonatypeHost = SonatypeHost.S01
}
