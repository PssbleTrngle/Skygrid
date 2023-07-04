val minecraft_version: String by extra
val mod_version: String by extra
val mod_id: String by extra
val fabric_loader_version: String by extra
val fabric_version: String by extra
val xmlutil_version: String by extra
val create_fabric_version: String by extra

plugins {
    id("fabric-loom") version ("1.0-SNAPSHOT")
    id("idea")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

base {
    archivesName.set("${mod_id}-fabric")
}

val dependencyProjects = listOf(
    project(":api"),
    project(":common"),
)

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

    implementation(include("io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}")!!)
    implementation(include("io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}")!!)

    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")

    modRuntimeOnly("maven.modrinth:create-fabric:${create_fabric_version}")

    dependencyProjects.forEach { implementation(it) }
}

loom {
    //accessWidenerPath.set(file("src/main/resources/${mod_id}.accesswidener"))
    mixin {
        defaultRefmapName.set("${mod_id}.refmap.json")
    }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            runDir("run/server")
        }
        create("data ") {
            client()
            configName = "Fabric Datagen"
            runDir("run/datagen")

            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${project(":common").file("src/generated/resources")}")
            vmArg("-Dfabric-api.datagen.modid=${mod_id}")
        }
        forEach { run ->
            run.ideConfigGenerated(true)
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
            dependencyProjects.forEach {
                sourceSet(it.sourceSets.main.get())
            }
        }
    }
}

tasks.withType<JavaCompile> {
    dependencyProjects.forEach {
        source(it.sourceSets["main"].allSource)
    }
}

tasks.jar {
    from(sourceSets.main.get().output)
    dependencyProjects.forEach {
        from(it.sourceSets.main.get().output)
    }
}