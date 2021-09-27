import org.gradle.util.GUtil.loadProperties

plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
    id(Deps.Plugins.Deploy.Id)
}

dependencies {
    implementation(Deps.Libs.Slf4j.Simple)
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Ktor.Server.Core)
    implementation(Deps.Libs.Ktor.Server.Cio)
    implementation(Deps.Libs.Ktor.Server.Openapi)
    implementation(Deps.Libs.Ktor.Server.WebSockets)
    implementation(Deps.Libs.Ktor.Server.Serialization)
    implementation(project(Deps.Modules.Backend.Server.Routing))
    implementation(project(Deps.Modules.Backend.Server.Api))
    implementation(project(Deps.Modules.Backend.Features.Mailer))
    implementation(project(Deps.Modules.Backend.Server.Sockets.Routing))
    implementation(project(Deps.Modules.Backend.Server.Storage.Core))
    implementation(project(Deps.Modules.Backend.Server.Storage.Database))
    implementation(project(Deps.Modules.Backend.Server.Storage.Files))
    implementation(project(Deps.Modules.Backend.Middlewares.Authorization))
    implementation(project(Deps.Modules.Backend.Extensions.OpenApi))
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INHERIT
}

val service = "dove"

val propertiesFile = rootProject.file("deploy.properties")

deploy {
    if (propertiesFile.exists()) {
        ignore = false
        val properties = loadProperties(propertiesFile)

        host = properties.getProperty("host")
        user = properties.getProperty("user")
        password = properties.getProperty("password")
        deployPath = properties.getProperty("deployPath")
        knownHostsFile = properties.getProperty("knownHosts")

        mainClass = "com.dove.backend.server.main.MainKt"
        serviceName = service
    } else {
        ignore = true
    }
}

application {
    mainClass.set("com.dove.backend.server.main.MainKt")
}

tasks.create("stop") {
    group = "deploy"
    doLast {
        the<SshSessionExtension>().invoke {
            execute("systemctl stop $service")
        }
    }
}