import com.possible_triangle.gradle.features.enableKotlin
import com.possible_triangle.gradle.features.loaders.forge

val xmlutil_version: String by extra
val forge_version: String by extra
val curios_forge_version: String by extra

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

enableKotlin()

forge {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))

    includesLibrary("io.github.pdvrieze.xmlutil:core-jvm:${xmlutil_version}")
    includesLibrary("io.github.pdvrieze.xmlutil:serialization-jvm:${xmlutil_version}")
}

dependencies {
    //runtimeOnly fg.deobf("vazkii.autoreglib:AutoRegLib:${arl_version}")

    //runtimeOnly fg.deobf("com.github.glitchfiend:TerraBlender-forge:${mc_version}-${terrablender_version}")
    //runtimeOnly fg.deobf("curse.maven:quark-243121:${quark_version}")
    //runtimeOnly fg.deobf("curse.maven:bop-220318:${bop_version}")

    //runtimeOnly fg.deobf("vazkii.patchouli:Patchouli:${mc_version}-${patchouli_version}")
    //runtimeOnly fg.deobf("top.theillusivec4.curios:curios-forge:${mc_version}-${curios_version}")
    //runtimeOnly fg.deobf("vazkii.botania:Botania:${mc_version}-${botania_version}")
}