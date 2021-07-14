plugins {
    kotlin("multiplatform") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
    `maven-publish`
}
group = "com.github.UBotPlatform.KtUBotCommon"
if (version.toString() == "unspecified") {
    version = "0.7.1"
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation("io.ktor:ktor-client-core:1.6.1")
                implementation("com.github.ArcticLampyrid.KtJsonRpcPeer:KtJsonRpcPeer:0.12.6")
                implementation("io.github.microutils:kotlin-logging:2.0.10")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
}
configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("KtUBotCommon")
            description.set("KtUBotCommon, UBot SDK for Kotlin.")
            url.set("https://github.com/UBotPlatform/KtUBotCommon")
            licenses {
                license {
                    name.set("BSD 3-Clause \"New\" or \"Revised\" License")
                    url.set("https://github.com/UBotPlatform/KtUBotCommon/blob/master/LICENSE.md")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("ArcticLampyrid")
                    name.set("ArcticLampyrid")
                    email.set("ArcticLampyrid@outlook.com")
                    timezone.set("Asia/Shanghai")
                }
            }
            scm {
                url.set("https://github.com/UBotPlatform/KtUBotCommon")
                connection.set("scm:git:git://github.com/UBotPlatform/KtUBotCommon.git")
                developerConnection.set("scm:git:ssh://github.com:UBotPlatform/KtUBotCommon.git")
            }
        }
    }
}