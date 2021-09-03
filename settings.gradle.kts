pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://jitpack.io")
    }
    plugins {
        id("com.android.application") version "4.2.2"
        id("org.jetbrains.kotlin.android") version "1.5.30-RC"
        id("org.jetbrains.kotlin.multiplatform") version "1.5.30"
        id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
    }
}
rootProject.name = "dove"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include("mailer")
