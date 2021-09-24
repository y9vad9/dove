plugins {
    `kotlin-dsl`
    id("dependencies")
}

gradlePlugin {
    plugins.register("k-js") {
        id = "k-js"
        implementationClass = "KJsConfiguration"
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