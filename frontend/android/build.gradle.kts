plugins {
    id(Deps.Plugins.Configuration.Kotlin.Android.App)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(project(Deps.Modules.Frontend.Common))
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(project(Deps.Modules.Frontend.Localization))
    implementation(Deps.Libs.Androidx.Compose.UI)
    implementation(Deps.Libs.Androidx.Compose.UITooling)
    implementation(Deps.Libs.Androidx.Compose.Activity)
    implementation(Deps.Libs.Androidx.Material)
    implementation(Deps.Libs.Androidx.Compose.JUnitTests)
    implementation(Deps.Libs.Androidx.Compose.Icons)
    implementation(Deps.Libs.Androidx.Compose.Material)
    implementation(Deps.Libs.Androidx.Compose.Foundation)
    implementation(Deps.Libs.Androidx.Compose.ExtendedIcons)
}

android {
    compileSdk = AppInfo.Android.TargetSdk

    defaultConfig {
        minSdkVersion(AppInfo.Android.MinSdk)
        targetSdk = AppInfo.Android.TargetSdk

        applicationId = "${AppInfo.Package}.android"
        versionName = AppInfo.VersionName
        versionCode = AppInfo.VersionCode
    }

    buildFeatures {
        compose = true
    }
}