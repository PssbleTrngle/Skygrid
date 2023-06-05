import org.spongepowered.asm.gradle.plugins.MixinExtension

val minecraft_version: String by extra
val mod_version: String by extra
val mod_id: String by extra
val xmlutil_version: String by extra
val forge_version: String by extra
val curios_forge_version: String by extra

buildscript {
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    id("net.minecraftforge.gradle") version ("5.1.+")
    id("idea")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

base {
    archivesName.set("${mod_id}-forge-${mod_version}")
}

apply(plugin = "org.spongepowered.mixin")

configure<MixinExtension> {
    add(sourceSets.main.get(), "${mod_id}.refmap.json")
    config("${mod_id}.mixins.json")
}

val dependencyProjects = listOf(
    project(":common"),
)

minecraft {
    mappings("official", minecraft_version)

    val ideaModule = "${rootProject.name.replace(" ", "_")}.${project.name}.main"

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            ideaModule(ideaModule)
            taskName("Client")
            property("test", ideaModule)
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                    dependencyProjects.forEach {
                        source(it.sourceSets.main.get())
                    }
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run/server"))
            ideaModule(ideaModule)
            taskName("Server")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                    dependencyProjects.forEach {
                        source(it.sourceSets.main.get())
                    }
                }
            }
        }
    }
}

jarJar.enable()
dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    dependencyProjects.forEach { implementation(it) }

    implementation("thedarkcolour:kotlinforforge:3.9.1")

    implementation("io.github.pdvrieze.xmlutil:core:${xmlutil_version}") {
        jarJar.ranged(this, "[${xmlutil_version},)")
    }
    implementation("io.github.pdvrieze.xmlutil:serialization:${xmlutil_version}") {
        jarJar.ranged(this, "[${xmlutil_version},)")
    }

    //runtimeOnly fg.deobf("vazkii.autoreglib:AutoRegLib:${arl_version}")
    //compileOnly fg.deobf("vazkii.autoreglib:AutoRegLib:${arl_version}")

    //implementation fg.deobf("com.github.glitchfiend:TerraBlender-forge:${minecraft_version}-${terrablender_version}")
    //implementation fg.deobf("curse.maven:quark-243121:${quark_version}")
    //implementation fg.deobf("curse.maven:bop-220318:${bop_version}")

    //implementation fg.deobf("vazkii.patchouli:Patchouli:${minecraft_version}-${patchouli_version}")
    //compileOnly fg.deobf("top.theillusivec4.curios:curios-forge:${minecraft_version}-${curios_version}:api")
    //runtimeOnly fg.deobf("top.theillusivec4.curios:curios-forge:${minecraft_version}-${curios_version}")

    //compileOnly fg.deobf("vazkii.botania:Botania:${minecraft_version}-${botania_version}:api")
    //runtimeOnly fg.deobf("vazkii.botania:Botania:${minecraft_version}-${botania_version}")


    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.withType<JavaCompile> {
    source(project(":common").sourceSets["main"].allSource)
}

tasks.jar {
    finalizedBy("reobfJar")

    from(sourceSets.main.get().output)
    dependencyProjects.forEach {
        from(it.sourceSets.main.get().output)
    }
}

val sourcesJar = tasks.named<Jar>("sourcesJar")

artifacts {
    archives(tasks.jar.get())
    archives(sourcesJar.get())
}