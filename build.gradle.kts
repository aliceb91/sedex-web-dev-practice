plugins {
    kotlin("jvm") version "1.9.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(platform("org.http4k:http4k-bom:5.12.0.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")
    implementation("org.http4k:http4k-format-argo")
    implementation("org.http4k:http4k-format-gson")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-format-klaxon")
    implementation("org.http4k:http4k-format-kondor-json")
    implementation("org.http4k:http4k-format-moshi")
    implementation("org.http4k:http4k-format-kotlinx-serialization")
    implementation("org.http4k:http4k-testing-hamkrest")
    testImplementation("org.mockito:mockito-core:3.3.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}