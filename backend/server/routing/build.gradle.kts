plugins {
    id(Deps.Plugins.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Middlewares.Authorization))
    implementation(project(Deps.Modules.Backend.Extensions.OpenApi))
    implementation(project(Deps.Modules.Backend.Server.Api))
    implementation(project(Deps.Modules.Backend.Server.Models))
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(project(Deps.Modules.Backend.Features.Mailer))
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Openapi)
}