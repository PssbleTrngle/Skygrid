val create_fabric_version: String by extra

fabric {
    enableMixins()

    dataGen()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    //modRuntimeOnly("maven.modrinth:create-fabric:${create_fabric_version}")
}

uploadToCurseforge()
uploadToModrinth()

tasks.named("runData") {
    finalizedBy(rootProject.tasks.getByName("copyGeneratedDatapacks"))
}