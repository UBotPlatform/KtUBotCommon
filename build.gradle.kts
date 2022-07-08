@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("com.github.arcticlampyrid.gradle-git-version") version "1.0.4"
    `maven-publish`
}
group = "com.github.UBotPlatform.KtUBotCommon"
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("com.github.ArcticLampyrid.KtJsonRpcPeer:KtJsonRpcPeer:0.12.7")
                implementation("io.github.microutils:kotlin-logging:2.1.21")
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
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
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