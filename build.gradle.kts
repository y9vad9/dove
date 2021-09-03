plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.application")
}

kotlin {
    jvm("backend") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Libs.Kotlinx.Serialization)
            }
        }
        val backendMain by getting {
            dependencies {
                implementation(Deps.Libs.Exposed.Core)
                implementation(Deps.Libs.Exposed.Jdbc)
                implementation(Deps.Libs.Exposed.Time)
                implementation(Deps.Libs.Ktor.Server.Core)
                implementation(Deps.Libs.Ktor.Server.Cio)
                implementation(Deps.Libs.Ktor.Server.Openapi)
                implementation(Deps.Libs.Ktor.Server.Serialization)
                implementation(project(Deps.Modules.Mailer))
                implementation(Deps.Libs.Guava)
            }
        }
        val backendTest by getting {
            dependencies {
                implementation(Deps.Libs.Guava)
                implementation(Deps.Libs.JUnit)
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        applicationId = AppInfo.Package
        versionCode = AppInfo.VersionCode
        versionName = AppInfo.VersionName

        minSdkVersion(21)
        targetSdkVersion(AppInfo.Android.TargetSdk)
    }
}