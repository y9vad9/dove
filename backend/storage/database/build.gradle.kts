plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(Deps.Libs.Exposed.Core)
    api(Deps.Libs.Exposed.Jdbc)
    api(Deps.Libs.Postgres.Jdbc)
}