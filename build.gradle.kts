plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    `java-library`
    `maven-publish`
}
kotlin {
    target {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}
repositories {
    jcenter()
}
dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")
    implementation("io.ktor:ktor-client-core:1.4.1")
    implementation("twitter.qiqiworld1.ktjsonrpcpeer:ktjsonrpcpeer:0.6.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ubot"
            artifactId = "common"
            version = "0.4.5"
            from(components["java"])
        }
    }
}