plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("org.hidetake.ssh")
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Exposed.Jdbc)
    implementation(Deps.Libs.Exposed.Time)
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Cio)
    implementation(Deps.Libs.Ktor.Server.Openapi)
    implementation(Deps.Libs.Ktor.Server.Serialization)
    implementation(project(Deps.Modules.Mailer))
    implementation(project(Deps.Modules.JsonRpc))
    implementation(Deps.Libs.Ktor.Server.WebSockets)
    testImplementation(Deps.Libs.JUnit)
}

tasks.withType<Test> {
    useJUnitPlatform()
}