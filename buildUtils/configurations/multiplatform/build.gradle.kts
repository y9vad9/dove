plugins {
    `kotlin-dsl`
    id("dependencies")
}

gradlePlugin {
    plugins.register("k-multiplatform") {
        id = "k-mpp"
        implementationClass = "KMultiplatformConfiguration"
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