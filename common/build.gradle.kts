val minecraft_version: String by extra
val mod_version: String by extra
val mod_id: String by extra
val xmlutil_version: String by extra

plugins {
    id("org.spongepowered.gradle.vanilla") version ("0.2.1-SNAPSHOT")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

base {
    archivesName.set("${mod_id}-common")
}

minecraft {
    version(minecraft_version)
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")

    implementation("io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.github.origin-energy:java-snapshot-testing-junit5:4.0.6")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    workingDir = project.file("run")
}