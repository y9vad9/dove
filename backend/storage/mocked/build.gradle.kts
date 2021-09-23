plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(project(Deps.Modules.Root))
}