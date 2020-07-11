plugins {
    `maven-publish`
    signing
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("org.jetbrains.dokka") version "0.10.0"
}

group = "com.github.patrick-mc"
version = "1.0"

repositories {
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://dl.bintray.com/kotlin/dokka")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")
    implementation("com.neovisionaries:nv-websocket-client:2.9")
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }

    dokka {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/dokka"

        configuration {
            includeNonPublic = true
            jdkVersion = 8
        }
    }

    create<Jar>("dokkaJar") {
        archiveClassifier.set("javadoc")
        from(dokka)
        dependsOn(dokka)
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    create<Jar>("distJar") {
        from(shadowJar)
        archiveClassifier.set("dist")
    }
}

try {
    publishing {
        publications {
            create<MavenPublication>("webSocketClientAPI") {
                from(components["java"])

                artifact(tasks["sourcesJar"])
                artifact(tasks["dokkaJar"])
                artifact(tasks["distJar"])

                repositories {
                    mavenLocal()

                    maven {
                        name = "central"

                        credentials {
                            username = project.property("centralUsername").toString()
                            password = project.property("centralPassword").toString()
                        }

                        url = if (version.toString().endsWith("SNAPSHOT")) {
                            uri("https://oss.sonatype.org/content/repositories/snapshots/")
                        } else {
                            uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                        }
                    }
                }

                pom {
                    name.set("vector-game")
                    description.set("A Simple WebSocket API for Bukkit")
                    url.set("https://github.com/patrick-mc/websocket-client-api")

                    licenses {
                        license {
                            name.set("GNU General Public License v2.0")
                            url.set("https://opensource.org/licenses/gpl-2.0.php")
                        }
                    }

                    developers {
                        developer {
                            id.set("patrick-mc")
                            name.set("PatrickKR")
                            email.set("mailpatrickkorea@gmail.com")
                            url.set("https://github.com/patrick-mc")
                            roles.addAll("developer")
                            timezone.set("Asia/Seoul")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/patrick-mc/websocket-client-api.git")
                        developerConnection.set("scm:git:ssh://github.com:patrick-mc/websocket-client-api.git")
                        url.set("https://github.com/patrick-mc/websocket-client-api")
                    }
                }
            }
        }
    }

    signing {
        isRequired = true
        sign(tasks["jar"], tasks["sourcesJar"], tasks["dokkaJar"], tasks["shadowJar"])
        sign(publishing.publications["webSocketClientAPI"])
    }
} catch (ignored: groovy.lang.MissingPropertyException) {}