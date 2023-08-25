val gravity_changer_version: String by extra
val nbt_serialization_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
    dependOn(project(":fabric"))

    includesMod("io.github.natanfudge:kotlinx-serialization-minecraft:$nbt_serialization_version")
}

dependencies {

    if(!env.isCI) {
        modRuntimeOnly("maven.modrinth:gravity-api:${gravity_changer_version}")
    }
}

//uploadToCurseforge()
//uploadToModrinth()