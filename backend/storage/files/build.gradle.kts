plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(Deps.Libs.Kotlinx.Coroutines)
}