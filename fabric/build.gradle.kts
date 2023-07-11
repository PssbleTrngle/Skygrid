val xmlutil_version: String by extra
val create_fabric_version: String by extra

withKotlin()

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    modRuntimeOnly("maven.modrinth:create-fabric:${create_fabric_version}")
}