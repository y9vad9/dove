plugins {
    `kotlin-dsl`
    id("dependencies")
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

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(Deps.Plugins.Kotlin.Classpath)
    implementation(Deps.Plugins.Dependencies.Classpath)
    implementation(Deps.Plugins.Android.Classpath)
}