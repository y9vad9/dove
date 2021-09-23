open class DeployExtension {
    var ignore = true

    // Required
    var host: String? = null
    var mainClass: String? = null
    var serviceName: String? = null
    var deployPath: String? = null

    // Optional
    var user: String? = null
    var password: String? = null
    var knownHostsFile: String? = null
    var implementationTitle: String? = mainClass
}