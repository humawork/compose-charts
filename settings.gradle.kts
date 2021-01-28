import de.fayard.refreshVersions.bootstrapRefreshVersions

rootProject.name = "HumaCharts"

include(":app")
include(":charts")

buildscript {
  repositories { gradlePluginPortal() }
  dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()
