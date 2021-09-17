plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins.register("deploy") {
        id = "deploy"
        implementationClass = "me.y9neon.deploy.ssh.DeployPlugin"
    }
}

dependencies {
    implementation("org.hidetake:gradle-ssh-plugin:2.10.1")
}