plugins {
    id(Deps.Plugins.Configuration.Kotlin.Mpp)
    id(Deps.Plugins.Android.Application)
}

kotlin {
    android()
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Deps.Modules.Root))
                implementation(Deps.Libs.Kotlinx.Coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Libs.Androidx.Lifecycle)
                implementation(Deps.Libs.Androidx.LifecycleKtx)
            }
        }
    }
}

android {
    compileSdk = AppInfo.Android.TargetSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = AppInfo.Android.MinSdk
        targetSdk = AppInfo.Android.TargetSdk
    }
}

