import com.possible_triangle.gradle.features.enableKotlin
import com.possible_triangle.gradle.features.loaders.common

val xmlutil_version: String by extra

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

enableKotlin()

common {
    dependOn(project(":api"))

    includesLibrary("io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}")
    includesLibrary("io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.github.origin-energy:java-snapshot-testing-junit5:4.0.6")
}

sourceSets["main"].resources {
    srcDir(project.file("src/generated/resources"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    workingDir = project.file("run")
}