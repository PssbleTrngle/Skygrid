package com.possible_triangle.skygrid.test.tests

import com.possible_triangle.skygrid.datagen.DatagenContext
import com.possible_triangle.skygrid.datagen.builder.DimensionConfigBuilder
import com.possible_triangle.skygrid.extensions.weights
import com.possible_triangle.skygrid.test.TestExtension
import com.possible_triangle.skygrid.test.assertApproximately
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.block.Blocks
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestExtension::class)
class WeightTest {

    private val context = DatagenContext(registries = RegistryAccess.BUILTIN.get())

    @Test
    fun flattensCorrectly() {
        val config = DimensionConfigBuilder.create(context) {
            blocks {
                block(Blocks.AMETHYST_BLOCK)
                block(Blocks.HONEYCOMB_BLOCK, weight = 0.5)
                list {
                    block(Blocks.OAK_LOG)
                    block(Blocks.ACACIA_LOG, weight = 3.0)
                }
            }
        }

        val weights = config.blocks.weights()

        assertApproximately(0.4, weights[Blocks.AMETHYST_BLOCK])
        assertApproximately(0.3, weights[Blocks.ACACIA_LOG])
        assertApproximately(0.1, weights[Blocks.OAK_LOG])
        assertApproximately(0.2, weights[Blocks.HONEYCOMB_BLOCK])
    }

}