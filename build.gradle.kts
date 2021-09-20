plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("dependencies")
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Libs.Kotlinx.Serialization)
                implementation(Deps.Libs.Kotlinx.Coroutines)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}