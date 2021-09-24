@file:Suppress("unused")

object Deps {
    private const val kotlinVersion = "1.5.31"
    private const val coroutinesVersion = "1.5.0"
    private const val serializationVersion = "1.2.2"
    private const val nodejsExternalsVersion = "0.0.7"
    private const val ktorVersion = "1.6.0"
    private const val ktorOpenapiVersion = "0.2-beta.20"
    private const val exposedVersion = "0.32.1"
    private const val datetimeVersion = "0.2.1"

    private const val postgresqlVersion = "42.2.23"
    private const val slf4jJVersion = "1.7.31"
    private const val logbackVersion = "0.9.26"
    private const val sshVersion = "2.10.1"

    private const val materialVersion = "1.2.1"
    private const val recyclerViewVersion = "1.1.0"
    private const val swipeRefreshLayoutVersion = "1.1.0"
    private const val constraintLayoutVersion = "2.0.0"
    private const val lifecycleVersion = "2.2.0"
    private const val glideVersion = "4.12.0"
    private const val androidAppCompatVersion = "1.1.0"
    private const val androidComposeVersion = "1.0.1"

    private const val androidGradlePluginVersion = "4.2.0"

    object Modules {
        const val Root = ":"

        object Backend {
            object Features {
                const val Time = ":backend:features:time"
                const val Mailer = ":backend:features:mailer"
                const val JsonRpc = ":backend:features:jsonrpc"
                const val Hashing = ":backend:features:hashing"
            }

            object Storage {
                const val Core = ":backend:storage:core"
                const val Database = ":backend:storage:database"
                const val Mocked = ":backend:storage:mocked"
                const val Files = ":backend:storage:files"
            }

            object Server {
                const val Routing = ":backend:server:routing"
                const val Api = ":backend:server:api"
                const val Models = ":backend:server:models"
                const val EventDispatcher = ":backend:server:event-dispatcher"

                object Sockets {
                    const val Routing = ":backend:server:sockets:routing"
                }
            }

            object Extensions {
                const val OpenApi = ":backend:extensions:openapi"
                const val Random = ":backend:extensions:random"
            }

            object Middlewares {
                const val Authorization = ":backend:middlewares:authorization"
            }
        }
    }

    object Libs {
        object Apache {
            const val CommonMail = "org.apache.commons:commons-email:1.5"
        }

        const val JUnit = "org.jetbrains.kotlin:kotlin-test-junit5:1.5.30"
        const val Guava = "com.google.guava:guava:30.1.1-jre"

        object Kotlinx {
            const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val Serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"
            const val Datetime = "org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion"
            const val Nodejs = "org.jetbrains.kotlinx:kotlinx-nodejs:$nodejsExternalsVersion"
        }

        object Ktor {
            object Client {
                const val Core = "io.ktor:ktor-client-core:$ktorVersion"
                const val Cio = "io.ktor:ktor-client-cio:$ktorVersion"
                const val Serialization = "io.ktor:ktor-client-serialization:$ktorVersion"
            }

            object Server {
                const val Core = "io.ktor:ktor-server-core:$ktorVersion"
                const val Cio = "io.ktor:ktor-server-cio:$ktorVersion"
                const val Serialization = "io.ktor:ktor-serialization:$ktorVersion"
                const val Openapi = "com.github.papsign:Ktor-OpenAPI-Generator:$ktorOpenapiVersion"
                const val Sockets = "io.ktor:ktor-network:$ktorVersion"
                const val WebSockets = "io.ktor:ktor-websockets:$ktorVersion"
            }
        }

        object Exposed {
            const val Core = "org.jetbrains.exposed:exposed-core:$exposedVersion"
            const val Jdbc = "org.jetbrains.exposed:exposed-jdbc:$exposedVersion"
            const val Time = "org.jetbrains.exposed:exposed-java-time:$exposedVersion"
        }

        object Postgres {
            const val Jdbc = "org.postgresql:postgresql:$postgresqlVersion"
        }

        object Logback {
            const val Classic = "ch.qos.logback:logback-classic:$logbackVersion"
        }

        object Slf4j {
            const val Simple = "org.slf4j:slf4j-simple:$slf4jJVersion"
        }

        object Androidx {
            const val AppCompat =
                "androidx.appcompat:appcompat:$androidAppCompatVersion"
            const val Material =
                "com.google.android.material:material:$materialVersion"
            const val RecyclerView =
                "androidx.recyclerview:recyclerview:$recyclerViewVersion"
            const val SwipeRefreshLayout =
                "androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion"
            const val ConstraintLayout =
                "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
            const val Lifecycle =
                "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
            const val LifecycleKtx =
                "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"

            object Compose {
                const val UI = "androidx.compose.ui:ui:$androidComposeVersion"
                const val UITooling = "androidx.compose.ui:ui-tooling:$androidComposeVersion"
                const val Foundation = "androidx.compose.foundation:foundation:$androidComposeVersion"
                const val Material = "androidx.compose.material:material:$androidComposeVersion"
                const val Icons = "androidx.compose.material:material-icons-core:$androidComposeVersion"
                const val ExtendedIcons = "androidx.compose.material:material-icons-extended:$androidComposeVersion"
                const val JUnitTests = "androidx.compose.ui:ui-test-junit4:$androidComposeVersion"
            }

        }

        object Bumtech {
            const val Glide =
                "com.github.bumptech.glide:glide:$glideVersion"
        }
    }

    object Kapt {
        object Bumtech {
            const val Glide = "com.github.bumptech.glide:compiler:$glideVersion"
        }
    }

    object Plugins {
        object Deploy {
            const val Id = "deploy"
        }

        object Configuration {
            object Kotlin {
                const val Mpp = "k-mpp"
                const val Jvm = "k-jvm"
                const val Js = "k-js"

                object Android {
                    const val App = "k-android-app"
                    const val Library = "k-android-library"
                }
            }
        }

        object Dependencies {
            const val Id = "dependencies"
            const val Classpath = "dependencies:dependencies:SNAPSHOT"
        }

        object Kotlin {
            const val Multiplatform = "org.jetbrains.kotlin.multiplatform"
            const val Jvm = "org.jetbrains.kotlin.jvm"
            const val Js = "org.jetbrains.kotlin.js"
            const val Android = "org.jetbrains.kotlin.android"
            const val Classpath = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        }

        object Android {
            const val Application = "com.android.application"
            const val Library = "com.android.library"
            const val Classpath = "android.tools.build:gradle:$androidGradlePluginVersion"
        }

        object Serialization {
            const val Id = "org.jetbrains.kotlin.plugin.serialization"
            const val Classpath = "org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion"
        }

        object Ssh {
            const val Id = "org.hidetake.ssh"
            const val Classpath = "org.hidetake:gradle-ssh-plugin:$sshVersion"
        }

        object Publish {
            const val Id = "publish"
            const val Classpath = "publish:publish:SNAPSHOT"
        }

        object MavenPublish {
            const val Id = "maven-publish"
        }

        object Application {
            const val Id = "application"
        }
    }
}