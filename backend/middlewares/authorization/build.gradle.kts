plugins {
    id(kJvm)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Openapi)
}