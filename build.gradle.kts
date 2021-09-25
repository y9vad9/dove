plugins {
    id(Deps.Plugins.Kotlin.Multiplatform)
    id(Deps.Plugins.Serialization.Id)
    id(Deps.Plugins.Configuration.Kotlin.Android.App) apply false
}

kotlin {
    jvm()
    js {
        browser()
    }

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