plugins {
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "examples",
            "ktor-server",
        )
    )
}