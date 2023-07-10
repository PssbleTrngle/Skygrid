import com.possible_triangle.gradle.features.curseMaven
import com.possible_triangle.gradle.features.enableKotlin
import com.possible_triangle.gradle.features.modrinthMaven
import com.possible_triangle.gradle.features.publishing.enablePublishing
import com.possible_triangle.gradle.features.publishing.githubPackages

val mod_id: String by extra
val mod_version: String by extra

plugins {
    idea
    id("org.jetbrains.kotlin.plugin.serialization") version ("1.8.21") apply (false)
    id("org.sonarqube") version "4.2.0.3129"
    id("com.possible_triangle.gradle") version ("0.0.0-dev")
}

enableKotlin()

subprojects {
    repositories {
        modrinthMaven()
        curseMaven()

        maven {
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
            content {
                includeGroup("org.spongepowered")
            }
        }

        maven {
            url = uri("https://maven.theillusivec4.top/")
            content {
                includeGroup("top.theillusivec4.curios")
            }
        }
    }

    enablePublishing {
        repositories {
            githubPackages(this@subprojects)
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectVersion", mod_version)
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

idea {
    module.excludeDirs.add(file("web"))
    module.excludeDirs.add(file("datagen"))
}