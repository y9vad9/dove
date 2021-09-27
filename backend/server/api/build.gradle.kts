plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(project(Deps.Modules.Backend.Server.Storage.Core))
    implementation(project(Deps.Modules.Backend.Features.Mailer))
    implementation(project(Deps.Modules.Backend.Features.Time))
    implementation(project(Deps.Modules.Backend.Extensions.Random))
    implementation(project(Deps.Modules.Backend.Features.Hashing))
    implementation(Deps.Libs.Kotlinx.Coroutines)
    testImplementation(Deps.Libs.JUnit)
    testImplementation(project(Deps.Modules.Backend.Server.Storage.Mocked))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
