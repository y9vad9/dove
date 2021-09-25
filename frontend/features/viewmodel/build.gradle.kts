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
                implementation(Deps.Libs.Kotlinx.Coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Libs.Androidx.ViewModel)
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