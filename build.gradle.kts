val mod_id: String by extra
val mod_version: String by extra
val xmlutil_version: String by extra

plugins {
    idea
    id("net.somethingcatchy.gradle") version ("0.0.0-dev")
}

withKotlin()

mod {
    includedLibraries.set(
        setOf(
            "io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}",
            "io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}",
        )
    )
}

subprojects {
    repositories {
        modrinthMaven()
        curseMaven()

        maven {
            url = uri("https://maven.theillusivec4.top")
            content {
                includeGroup("top.theillusivec4.curios")
            }
        }

        maven {
            url = uri("https://maven.blamejared.com")
            content {
                includeGroup("mezz.jei")
                includeGroup("vazkii.botania")
                includeGroup("vazkii.autoreglib")
                includeGroup("vazkii.patchouli")
            }
        }

        maven {
            url = uri("https://maven.minecraftforge.net")
            content {
                includeGroup("com.github.glitchfiend")
            }
        }
    }

    enablePublishing {
        repositories {
            githubPackages(this@subprojects)
        }
    }

    tasks.withType<Jar> {
        exclude("datapacks")
    }
}

enableSonarQube()

idea {
    module.excludeDirs.add(file("web"))
    module.excludeDirs.add(file("datagen"))
}

tasks.create<Copy>("copyGeneratedDatapacks") {
    from(project(":common").file("src/generated/resources/datapacks"))
    into(file("datapacks"))
}