import com.possible_triangle.gradle.features.enableKotlin
import com.possible_triangle.gradle.features.loaders.fabric

val xmlutil_version: String by extra
val create_fabric_version: String by extra

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

enableKotlin()

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))

    includesLibrary("io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}")
    includesLibrary("io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}")
}

dependencies {
    //modRuntimeOnly("maven.modrinth:create-fabric:${create_fabric_version}")
}