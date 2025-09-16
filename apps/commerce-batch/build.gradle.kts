plugins {
    val kotlinVersion = "2.0.20"

    id("org.jetbrains.kotlin.jvm") version (kotlinVersion)
    id("org.jetbrains.kotlin.kapt") version (kotlinVersion)
    id("org.jetbrains.kotlin.plugin.spring") version (kotlinVersion)
}

dependencies {
    // Spring Batch
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // JPA
    implementation(project(":modules:jpa"))

    // supports
    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    // tests
    testImplementation("org.springframework.batch:spring-batch-test")
}
