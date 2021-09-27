plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation(project(Deps.Modules.Root))
}