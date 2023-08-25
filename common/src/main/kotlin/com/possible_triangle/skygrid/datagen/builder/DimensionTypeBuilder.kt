package com.possible_triangle.skygrid.datagen.builder

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.DimensionType.MonsterSettings
import java.util.*

class DimensionTypeBuilder {

    var fixedTime: Long? = null
    var hasSkyLight = true
    var hasCeiling = false
    var ultraWarm = false
    var natural = true
    var coordinateScale = 1.0
    var bedWorks = true
    var respawnAnchorWorks = false
    var minY = -64
    var height = 384
    var logicalHeight: Int? = null
    var infiniburn = BlockTags.INFINIBURN_OVERWORLD
    var effects = ResourceLocation("overworld")
    var ambientLight = 0.0F
    var piglinSafe = false
    var hasRaids = false
    var monsterSpawnLightLevel = UniformInt.of(0, 7)
    var monsterSpawnLightLevelLimit = 0

    fun build(): DimensionType {
        return DimensionType(
            fixedTime?.let(OptionalLong::of) ?: OptionalLong.empty(),
            hasSkyLight,
            hasCeiling,
            ultraWarm,
            natural,
            coordinateScale,
            bedWorks,
            respawnAnchorWorks,
            minY,
            height,
            logicalHeight ?: height,
            infiniburn,
            effects,
            ambientLight,
            MonsterSettings(
                piglinSafe,
                hasRaids,
                monsterSpawnLightLevel,
                monsterSpawnLightLevelLimit,
            ),
        )
    }

}
