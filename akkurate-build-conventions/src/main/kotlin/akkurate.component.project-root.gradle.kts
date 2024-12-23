import dev.nesk.akkurate.gradle.libs

plugins {
    id("akkurate.base.repositories")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "examples",
            "ktor-server",
        )
    )

    @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
    klib {
        enabled = true
    }
}

tasks.named<Wrapper>("wrapper") {
    val expectedGradleVersion = libs.versions.gradle.wrapper.get()
    val actualGradleVersion = project.gradle.gradleVersion
    validateDistributionUrl = expectedGradleVersion != actualGradleVersion
    gradleVersion = expectedGradleVersion
}
