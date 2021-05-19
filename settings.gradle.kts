
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "HumaCharts"

include(":app")
include(":charts")

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}
