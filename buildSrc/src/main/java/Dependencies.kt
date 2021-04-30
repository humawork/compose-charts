object Config {
  const val minSdk = 21
  const val targetSdk = 30

  const val buildTools = "30.0.3"
}

object Dependencies {
  object Android {
    const val gradlePlugin = "com.android.tools.build:gradle:_"
  }

  object Kotlin {
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:_"
  }

  object Compose {
    const val version = "1.0.0-beta05"

    const val activity = "androidx.activity:activity-compose:_"
    const val runtime = "androidx.compose.runtime:runtime:_"
    const val livedata = "androidx.compose.runtime:runtime-livedata:_"
    const val savedInstanceState = "androidx.compose.runtime:runtime-saved-instance-state:_"

    const val foundation = "androidx.compose.foundation:foundation:_"
    const val layout = "androidx.compose.foundation:foundation-layout:_"

    const val ui = "androidx.compose.ui:ui:_"
    const val material = "androidx.compose.material:material:_"
    const val iconsExtended = "androidx.compose.material:material-icons-extended:_"

    const val animation = "androidx.compose.animation:animation:_"

    const val tooling = "androidx.compose.ui:ui-tooling:_"
    const val util = "androidx.compose.ui:ui-util:_"
    const val test = "androidx.compose:ui:ui-test:_"

    object Navigation {
      const val core = "androidx.navigation:navigation-compose:_"
    }
  }

  object Accompanist {
    const val flow = "com.google.accompanist:accompanist-flowlayout:_"
  }

  object Ktlint {
    const val plugin = "org.jlleitschuh.gradle:ktlint-gradle:_"
  }

  object VannikTech {
    const val mavenPublish = "com.vanniktech:gradle-maven-publish-plugin:0.15.0"
  }
}
