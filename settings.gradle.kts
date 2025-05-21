pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "valfi2"
include(":app")
include(":core")
include(":album:ui")
include(":album:data")
include(":settings")
include(":authentication")
include(":lyrics:ui")
include(":lyrics:data")
include(":settings:ui")
include(":settings:data")
include(":rss:ui")
include(":rss:data")
