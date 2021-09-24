import org.gradle.api.Plugin
import org.gradle.api.Project

class KAndroidAppConfiguration : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(Deps.Plugins.Android.Application)
        plugins.apply(Deps.Plugins.Kotlin.Android)
    }
}