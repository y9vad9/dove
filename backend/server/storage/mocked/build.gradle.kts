plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Server.Storage.Core))
    implementation(project(Deps.Modules.Root))
}