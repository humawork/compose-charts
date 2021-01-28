import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(Dependencies.Android.gradlePlugin)
    classpath(Dependencies.Kotlin.gradlePlugin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://jitpack.io")
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      // Allow warnings when running from IDE, makes it easier to experiment.
      // allWarningsAsErrors = true

      freeCompilerArgs = freeCompilerArgs + listOf("-Xallow-jvm-ir-dependencies")
      jvmTarget = "1.8"
    }
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
