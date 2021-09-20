pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://jitpack.io")
    }
    plugins {
        id("com.android.application") version "4.2.2"
        id("org.jetbrains.kotlin.android") version "1.5.31"
        id("org.jetbrains.kotlin.multiplatform") version "1.5.31"
        id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
        id("org.hidetake.ssh") version "2.10.1"
    }
}
rootProject.name = "dove"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
}

includeBuild("buildUtils/dependencies")
includeBuild("buildUtils/deploy")

include("mailer")
include("jsonrpc")
include("backend")
//include("android")
