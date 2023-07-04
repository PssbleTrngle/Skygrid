import groovy.util.Node
import groovy.util.NodeList
import java.time.LocalDateTime

val mod_version: String by extra
val mod_name: String by extra
val mod_id: String by extra
val mod_author: String by extra
val minecraft_version: String by extra
val repository: String by extra
val artifactGroup: String by extra

plugins {
    java
    idea
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version ("1.8.21") apply (false)
    id("org.jetbrains.kotlin.plugin.serialization") version ("1.8.21") apply (false)
    id("org.sonarqube") version "4.2.0.3129"
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<Jar> {
        val now = LocalDateTime.now().toString()

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${mod_name}" }
        }

        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to mod_name,
                    "Specification-Vendor" to mod_author,
                    "Specification-Version" to mod_version,
                    "Implementation-Title" to name,
                    "Implementation-Version" to archiveVersion,
                    "Implementation-Vendor" to mod_author,
                    "Implementation-Timestamp" to now,
                )
            )
        }
    }

    //tasks.named<Jar>("sourcesJar") {
    //    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    //    from(rootProject.file("LICENSE")) {
    //        rename { "${it}_${mod_name}" }
    //    }
    //}

    repositories {
        mavenCentral()

        maven {
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
            content {
                includeGroup("org.spongepowered")
            }
        }

        maven {
            url = uri("https://api.modrinth.com/maven")
            content {
                includeGroup("maven.modrinth")
            }
        }

        maven {
            url = uri("https://www.cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }

        maven {
            url = uri("https://maven.theillusivec4.top/")
            content {
                includeGroup("top.theillusivec4.curios")
            }
        }

        maven {
            url = uri("https://thedarkcolour.github.io/KotlinForForge/")
            content {
                includeGroup("thedarkcolour")
            }
        }
    }

    // Disables Gradle's custom module metadata from being published to maven. The
    // metadata includes mapped dependencies which are not reasonably consumable by
    // other mod developers.
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }

    tasks.withType<ProcessResources> {
        // this will ensure that this task is redone when the versions change.
        inputs.property("version", version)

        filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "fabric.mod.json", "${mod_id}.mixins.json")) {
            expand(
                mapOf(
                    "mod_version" to mod_version,
                    "mod_name" to mod_name,
                    "mod_id" to mod_id,
                    "mod_author" to mod_author,
                    "repository" to repository,
                )
            )
        }
    }

    val env = System.getenv()

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${repository}")
                version = mod_version
                credentials {
                    username = env["GITHUB_ACTOR"]
                    password = env["GITHUB_TOKEN"]
                }
            }
        }
        publications {
            create<MavenPublication>("gpr") {
                groupId = artifactGroup
                artifactId = "${mod_id}-${project.name}"
                version = mod_version
                from(components["java"])

                pom.withXml {
                    val node = asNode()
                    val list = node.get("dependencies") as NodeList
                    list.forEach { node.remove(it as Node) }
                }
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectVersion", version)
        property("sonar.projectKey", mod_id)
    }
}

subprojects {
    sonarqube {
        properties {
            property("sonar.branch", this@subprojects.name)
        }
    }
}