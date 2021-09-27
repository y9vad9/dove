plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Server.Storage.Core))
    implementation(Deps.Libs.Kotlinx.Coroutines)
}