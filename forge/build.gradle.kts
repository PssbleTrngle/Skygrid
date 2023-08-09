val kotlin_forge_version: String by extra
val curios_forge_version: String by extra
val botania_version: String by extra

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
        //modRuntimeOnly("vazkii.autoreglib:AutoRegLib:${arl_version}")

        //modRuntimeOnly("com.github.glitchfiend:TerraBlender-forge:${mc_version}-${terrablender_version}")
        //modRuntimeOnly("curse.maven:quark-243121:${quark_version}")
        //modRuntimeOnly("curse.maven:bop-220318:${bop_version}")

        //modRuntimeOnly("vazkii.patchouli:Patchouli:${mc_version}-${patchouli_version}")
        //modRuntimeOnly("top.theillusivec4.curios:curios-forge:${mc_version}-${curios_version}")

        //modRuntimeOnly("vazkii.botania:Botania:${botania_version}")
    }
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}