plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    `java-library`
    `maven-publish`
}
repositories {
    maven("https://www.jitpack.io")
    jcenter()
}
dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")
    api("com.github.1354092549:ktjsonrpcpeer:v0.3.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ubot"
            artifactId = "common"
            version = "0.4.2"
            from(components["java"])
        }
    }
}