val xmlutil_version: String by extra
val forge_version: String by extra
val curios_forge_version: String by extra
val botania_version: String by extra

withKotlin()

forge {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    if (!env.isCI) {
        //modRuntimeOnly("vazkii.autoreglib:AutoRegLib:${arl_version}")

        //modRuntimeOnly("com.github.glitchfiend:TerraBlender-forge:${mc_version}-${terrablender_version}")
        //modRuntimeOnly("curse.maven:quark-243121:${quark_version}")
        //modRuntimeOnly("curse.maven:bop-220318:${bop_version}")

        //modRuntimeOnly("vazkii.patchouli:Patchouli:${mc_version}-${patchouli_version}")
        //modRuntimeOnly("top.theillusivec4.curios:curios-forge:${mc_version}-${curios_version}")

        modRuntimeOnly("vazkii.botania:Botania:${botania_version}")
    }
}