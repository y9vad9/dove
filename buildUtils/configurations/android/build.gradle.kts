plugins {
    `kotlin-dsl`
    id("dependencies")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(Deps.Plugins.Dependencies.Classpath)
    implementation(Deps.Plugins.Android.Classpath)
    implementation(Deps.Plugins.Kotlin.Classpath)
}

gradlePlugin {
    plugins.register("k-android-app") {
        id = "k-android-app"
        implementationClass = "KAndroidAppConfiguration"
    }
    plugins.register("k-android-library") {
        id = "k-android-library"
        implementationClass = "KAndroidLibConfiguration"
    }
}