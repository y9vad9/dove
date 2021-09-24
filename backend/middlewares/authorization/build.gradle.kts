plugins {
    id(Deps.Plugins.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Openapi)
}