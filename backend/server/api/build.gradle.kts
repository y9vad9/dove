plugins {
    id(kJvm)
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(project(Deps.Modules.Backend.Storage.Core))
    implementation(project(Deps.Modules.Backend.Features.Mailer))
    implementation(project(Deps.Modules.Backend.Features.Time))
    implementation(project(Deps.Modules.Backend.Extensions.Random))
    implementation(project(Deps.Modules.Backend.Features.Hashing))
    implementation(Deps.Libs.Kotlinx.Coroutines)
    testImplementation(Deps.Libs.JUnit)
    testImplementation(project(Deps.Modules.Backend.Storage.Mocked))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
