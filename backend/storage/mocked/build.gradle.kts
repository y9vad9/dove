plugins {
    id(kJvm)
}

dependencies {
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(project(Deps.Modules.Root))
}