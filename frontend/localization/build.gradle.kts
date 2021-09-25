plugins {
    id(Deps.Plugins.Configuration.Kotlin.Mpp)
}

kotlin {
    jvm()
    js {
        browser()
    }
}