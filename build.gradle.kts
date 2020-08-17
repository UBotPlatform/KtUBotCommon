plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
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
    api("com.github.1354092549:ktjsonrpcpeer:v0.2.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ubot"
            artifactId = "common"
            version = "0.1"
            from(components["java"])
        }
    }
}