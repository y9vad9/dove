plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.hidetake.ssh")
    id("com.github.johnrengelman.shadow")
}


kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Libs.Kotlinx.Serialization)
                implementation(Deps.Libs.Kotlinx.Coroutines)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//android {
//    compileSdk = 31
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        applicationId = AppInfo.Package
//        versionCode = AppInfo.VersionCode
//        versionName = AppInfo.VersionName
//
//        minSdkVersion(21)
//        targetSdkVersion(AppInfo.Android.TargetSdk)
//    }
//}

//tasks.create(name = "Deploy") {
//    group = "deploy"
//
//    dependsOn(tasks["shadowJar"])
//
//    doLast {
//        ssh.run(delegateClosureOf<org.hidetake.groovy.ssh.core.RunHandler> {
//            session(
//                getRemote(),
//                delegateClosureOf<org.hidetake.groovy.ssh.session.SessionHandler> {
//                    put(
//                        hashMapOf(
//                            "from" to tasks.getByName<Jar>("shadowJar").archiveFile.get().asFile,
//                            "into" to properties["destination"]!!,
//                        ),
//                    )
//                    execute("systemctl restart dove")
//                },
//            )
//        })
//    }
//}