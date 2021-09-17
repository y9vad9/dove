import org.gradle.api.Project
import org.gradle.util.GUtil.loadProperties
import org.hidetake.groovy.ssh.connection.AllowAnyHosts
import org.hidetake.groovy.ssh.core.Remote

fun Project.getRemote(): Remote {
    val properties = loadProperties(file("local.properties"))

    return Remote(
        mapOf(
            "host" to properties["host"],
            "user" to properties["user"],
            "password" to properties["password"],
            "knownHosts" to AllowAnyHosts.instance
        )
    )
}