plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Features.JsonRpc))
    implementation(project(Deps.Modules.Backend.Server.EventDispatcher))
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(project(Deps.Modules.Backend.Server.Api))
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(project(Deps.Modules.Root))
}