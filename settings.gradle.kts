pluginManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://jitpack.io")
        gradlePluginPortal()
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
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
}

includeBuild("buildUtils/dependencies")
includeBuild("buildUtils/configurations/android")
includeBuild("buildUtils/configurations/jvm")
includeBuild("buildUtils/configurations/js")
includeBuild("buildUtils/configurations/multiplatform")
includeBuild("buildUtils/deploy")

/* backend */
// server
include(":backend:server:routing")
include(":backend:server:api")
include(":backend:server:main")
include(":backend:server:models")
include(":backend:server:event-dispatcher")
// sockets
include(":backend:server:socket")
// storage
include(":backend:server:storage:core")
include(":backend:server:storage:database")
include(":backend:server:storage:mocked")
include(":backend:server:storage:files")
// extensions
include(":backend:extensions:openapi")
include(":backend:extensions:random")
// middlewares
include(":backend:middlewares:authorization")
// features
include(":backend:features:mailer")
include(":backend:features:jsonrpc")
include(":backend:features:hashing")
include(":backend:features:time")
// api
include(":backend:apis")

/* frontend */
include(":frontend:android")
include(":frontend:web")
include(":frontend:features:viewmodel")
include(":frontend:features:localization")
include(":frontend:common")
