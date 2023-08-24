val gravity_changer_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
    dependOn(project(":fabric"))
}

dependencies {
    if(!env.isCI) {
        modRuntimeOnly("maven.modrinth:gravity-api:${gravity_changer_version}")
    }
}

//uploadToCurseforge()
//uploadToModrinth()