plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(Deps.Libs.Kotlinx.Coroutines)
}