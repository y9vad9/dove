plugins {
    kotlin("jvm")
    id("dependencies")
}

group = "${AppInfo.Package}.mailer"
version = AppInfo.VersionName

dependencies {
    implementation(Deps.Libs.Kotlinx.Coroutines)
    implementation(Deps.Libs.Apache.CommonMail)
}