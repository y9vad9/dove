plugins {
    id(Deps.Plugins.Configuration.Kotlin.Js)
}

kotlin {
    js {
        browser()
    }
}

dependencies {
    implementation(project(Deps.Modules.Root))
    implementation(Deps.Libs.KVision.Core)
    implementation(Deps.Libs.KVision.Bootstrap.Core)
    implementation(Deps.Libs.KVision.Bootstrap.Css)
    implementation(Deps.Libs.KVision.Bootstrap.Dialog)
    implementation(Deps.Libs.KVision.Bootstrap.Spinner)
    implementation(Deps.Libs.KVision.Bootstrap.Upload)
    implementation(Deps.Libs.KVision.Bootstrap.DateTime)
    implementation(Deps.Libs.KVision.Toast)
    implementation(Deps.Libs.KVision.State)
    implementation(Deps.Libs.KVision.i18n)
}