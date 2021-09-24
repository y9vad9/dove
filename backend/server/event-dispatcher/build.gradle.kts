plugins {
    id(Deps.Plugins.Kotlin.Jvm)
}

dependencies {
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(project(Deps.Modules.Root))
}