plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(project(Deps.Modules.Root))
}