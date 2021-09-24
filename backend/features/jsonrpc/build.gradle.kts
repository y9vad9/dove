plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

version = AppInfo.VersionName

dependencies {
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.WebSockets)
    implementation(Deps.Libs.Kotlinx.Serialization)
}

kotlin {
    explicitApi()
}