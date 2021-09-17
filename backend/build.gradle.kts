import org.gradle.util.GUtil.loadProperties

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("org.hidetake.ssh")
    id("deploy")
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

val service = "dove"

val propertiesFile = file("deploy.properties")

deploy {
    if (propertiesFile.exists()) {
        val properties = loadProperties(propertiesFile)

        host = properties.getProperty("host")
        user = properties.getProperty("user")
        password = properties.getProperty("password")
        deployPath = properties.getProperty("deployPath")
        knownHostsFile = properties.getProperty("knownHosts")

        mainClass = "com.dove.server.MainKt"
        serviceName = service
    } else {
        ignore = true
    }
}

tasks.create("stop") {
    group = "deploy"
    doLast {
        the<SshSessionExtension>().invoke {
            execute("systemctl stop $service")
        }
    }
}