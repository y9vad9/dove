plugins {
    kotlin("android")
    id("dependencies")
}

android {
    compileSdkVersion(AppInfo.Android.TargetSdk)
    defaultConfig {
        applicationId = "com.notes.android"
        minSdkVersion(AppInfo.Android.MinSdk)
        targetSdkVersion(AppInfo.Android.TargetSdk)
        versionCode = AppInfo.VersionCode
        versionName = AppInfo.VersionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Deps.Libs.Androidx.Compose.UI)
    implementation(Deps.Libs.Androidx.Compose.Foundation)
    implementation(Deps.Libs.Androidx.Compose.Icons)
    implementation(Deps.Libs.Androidx.Compose.Material)
    implementation(Deps.Libs.Androidx.Compose.UITooling)
    implementation(Deps.Libs.Androidx.Compose.JUnitTests)
}