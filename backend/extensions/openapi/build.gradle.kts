plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(project(Deps.Modules.Backend.Middlewares.Authorization))
    api(Deps.Libs.Kotlinx.Serialization)
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Openapi)
}