package com.possible_triangle.skygrid.test.tests

import au.com.origin.snapshots.Expect
import au.com.origin.snapshots.junit5.SnapshotExtension
import com.possible_triangle.skygrid.test.TestExtension
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.*

@ExperimentalSerializationApi
@ExperimentalXmlUtilApi
@ExtendWith(TestExtension::class, SnapshotExtension::class)
class XMLTest {

    private lateinit var expect: Expect

    @Test
    fun encodesCorrectly() {
        val config = DimensionConfig(
            blocks = ListWrapper(
                SingleBlock(
                    "some_block", "some_mod", extras = listOf(
                        Side(
                            "down", listOf(
                                SingleBlock("something_below"),
                            )
                        )
                    )
                ),
                SingleBlock("another_block", weight = 0.1),
                Fallback(
                    children = listOf(
                        Tag("type/material", "forge"),
                        Reference("a_preset", weight = 12.5),
                    ),
                    filters = listOf(
                        ExceptFilter(
                            listOf(
                                TagFilter("exclude_this", "a_mod"),
                                TagFilter("exclude_that"),
                                NameFilter("_stripped"),
                                ModFilter("another_mod"),
                            )
                        )
                    )
                ),
                BlockList(
                    children = listOf(
                        Tag("stuff"),
                        BlockList(
                            children = listOf(
                                SingleBlock("yet_another_block", weight = 1.0),
                                SingleBlock(
                                    "maybe_a_crafting_table", weight = 24.2,
                                    transformers = listOf(
                                        SetPropertyTransformer("numerical_property", "23"),
                                    )
                                ),
                            )
                        )
                    ),
                    transformers = listOf(
                        SetPropertyTransformer("the_key", "some_value"),
                        CyclePropertyTransformer("axis_or_something"),
                    )
                )
            )
        )
        val encoded = XMLResource.LOADER.encodeToString(config)
        expect.toMatchSnapshot(encoded)
    }

}