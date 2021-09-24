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
include(":backend:server:sockets:routing")
// storage
include(":backend:storage:core")
include(":backend:storage:database")
include(":backend:storage:mocked")
include(":backend:storage:files")
// extensions
include(":backend:extensions:openapi")
include(":backend:extensions:random")
// middlewares
include(":backend:middlewares:authorization")
// com.dove.server.features
include(":backend:features:mailer")
include(":backend:features:jsonrpc")
include(":backend:features:hashing")
include(":backend:features:time")
// api
include(":backend:apis")
