import org.gradle.api.Plugin
import org.gradle.api.Project

class KJvmConfiguration : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(Deps.Plugins.Kotlin.Jvm)
    }
}