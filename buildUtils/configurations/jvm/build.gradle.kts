plugins {
    `kotlin-dsl`
    id("dependencies")
}

gradlePlugin {
    plugins.register("k-jvm") {
        id = "k-jvm"
        implementationClass = "KJvmConfiguration"
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(Deps.Plugins.Kotlin.Classpath)
    implementation(Deps.Plugins.Dependencies.Classpath)
}