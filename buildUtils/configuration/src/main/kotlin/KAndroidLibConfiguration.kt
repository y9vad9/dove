import org.gradle.api.Plugin
import org.gradle.api.Project

class KAndroidLibConfiguration : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(Deps.Plugins.Android.Library)
        plugins.apply(Deps.Plugins.Kotlin.Android)
    }
}