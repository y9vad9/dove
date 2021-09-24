plugins {
    id(kJvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(Deps.Libs.Kotlinx.Coroutines)
}