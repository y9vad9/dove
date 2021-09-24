plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

group = "${AppInfo.Package}.mailer"
version = AppInfo.VersionName

dependencies {
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(Deps.Libs.Apache.CommonMail)
}