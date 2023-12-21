val kotlin_forge_version: String by extra
val jei_version: String by extra
val curios_forge_version: String by extra
val botania_version: String by extra
val twilight_version: String by extra
// val quark_version: String by extra
// val arl_version: String by extra
val mc_version: String by extra
val terrablender_version: String by extra
val bop_version: String by extra
val patchouli_version: String by extra

forge {
    enableMixins()

    kotlinForgeVersion = null

    dependOn(project(":api"))
    dependOn(project(":common"))
}

// required because of duplicate package export
configurations.named("minecraftLibrary") {
    exclude(group = "org.jetbrains", module = "annotations")
}

dependencies {
    // required because of duplicate package export by thedarkcolour:kotlinforforge:all
    implementation("thedarkcolour:kffmod:${kotlin_forge_version}")
    implementation("thedarkcolour:kfflang:${kotlin_forge_version}")
    implementation("thedarkcolour:kfflib:${kotlin_forge_version}")

    add("minecraftLibrary", "org.jetbrains.kotlin:kotlin-reflect:${kotlin.coreLibrariesVersion}")

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")

        // modRuntimeOnly("vazkii.autoreglib:AutoRegLib:${arl_version}")
        // modRuntimeOnly("curse.maven:quark-243121:${quark_version}")

        modRuntimeOnly("com.github.glitchfiend:TerraBlender-forge:${mc_version}-${terrablender_version}")
        modRuntimeOnly("curse.maven:bop-220318:${bop_version}")

        modRuntimeOnly("vazkii.patchouli:Patchouli:${mc_version}-${patchouli_version}-FORGE")
        modRuntimeOnly("top.theillusivec4.curios:curios-forge:${curios_forge_version}+${mc_version}")

        modRuntimeOnly("vazkii.botania:Botania:${mc_version}-${botania_version}")

        modRuntimeOnly("curse.maven:the-twilight-forest-227639:${twilight_version}")
    }
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}