plugins {
    id(kJvm)
}

dependencies {
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(project(Deps.Modules.Root))
}