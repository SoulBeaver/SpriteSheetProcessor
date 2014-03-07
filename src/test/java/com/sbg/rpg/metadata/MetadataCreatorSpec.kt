package com.sbg.rpg.metadata

import org.spek.Spek
import com.sbg.rpg.packer.SpriteBounds
import kotlin.test.assertTrue
import java.awt.Rectangle
import kotlin.test.assertEquals

class MetadataCreatorSpec: Spek() {{
    given("An empty list of SpriteBounds") {
        val spriteBoundsList = listOf<SpriteBounds>()

        on("converting to yaml") {
            it("returns an empty string") {
                val result = createYamlMetadata(spriteBoundsList)

                assertTrue(result.isEmpty(),
                           "Expected an empty yaml string, but was $result")
            }
        }

        on("converting to json") {
            it("returns an empty string") {
                val result = createJsonMetadata(spriteBoundsList)

                assertTrue(result.isEmpty(),
                           "Expected an empty json string, but was $result")
            }
        }

        on("converting to text") {
            it("returns an empty string") {
                val result = createTextMetadata(spriteBoundsList)

                assertTrue(result.isEmpty(),
                           "Expected an empty text string, but was $result")
            }
        }
    }

    given("A single SpriteBound") {
        val spriteBoundsList = listOf(SpriteBounds(0, Rectangle(0, 0, 50, 50)))

        on("converting to yaml") {
            it("returns a valid yaml 1.1 string") {
                val expected = "Frames:" +System.lineSeparator() +
                               "  - Index: 0" +System.lineSeparator() +
                               "    Bounds: [0, 0, 50, 50]" +System.lineSeparator()

                val result = createYamlMetadata(spriteBoundsList)

                assertTrue(result.isNotEmpty(),
                           "Expected a proper yaml string for single SpriteBounds entry")
                assertEquals(expected, result,
                             "Expected ${expected}, but was $result")
            }
        }

        on("converting to json") {
            it("returns a valid json string") {
                val expected = "[{\"frame\":0,\"bounds\":{\"x\":0,\"y\":0,\"width\":50,\"height\":50}}]"

                val result = createJsonMetadata(spriteBoundsList)

                assertTrue(result.isNotEmpty(),
                           "Expected a proper json string for single SpriteBounds entry")
                assertEquals(expected, result,
                             "Expected ${expected}, but was $result")
            }
        }

        on("converting to text") {
            it("returns a plain text string") {
                val expected = "0=0 0 50 50"

                val result = createTextMetadata(spriteBoundsList)

                assertTrue(result.isNotEmpty(),
                           "Expected a plan text string for single SpriteBounds entry")
                assertEquals(expected, result,
                             "Expected ${expected}, but was $result")
            }
        }

        given("Multiple SpriteBounds") {
            val spriteBoundsList = listOf(SpriteBounds(0, Rectangle(0, 0, 50, 50)),
                                          SpriteBounds(1, Rectangle(51, 51, 50, 50)))

            on("converting to yaml") {
                it("returns a valid yaml 1.1 string") {
                    val expected = "Frames:" +System.lineSeparator() +
                    "  - Index: 0" +System.lineSeparator() +
                    "    Bounds: [0, 0, 50, 50]" +System.lineSeparator() +
                    "  - Index: 1" +System.lineSeparator() +
                    "    Bounds: [51, 51, 50, 50]"

                    val result = createYamlMetadata(spriteBoundsList)

                    assertTrue(result.isNotEmpty(),
                            "Expected a valid yaml 1.1 String")
                    assertEquals(expected, result,
                            "Expected ${expected}, but was $result")
                }
            }

            on("converting to json") {
                it("returns a valid json string") {
                    val expected = "[{\"frame\":0,\"bounds\":{\"x\":0,\"y\":0,\"width\":50,\"height\":50},{\"frame\":1,\"bounds\":{\"x\":51,\"y\":51,\"width\":50,\"height\":50}}]"

                    val result = createJsonMetadata(spriteBoundsList)

                    assertTrue(result.isNotEmpty(),
                               "Expected a non-empty json string")
                    assertEquals(expected, result,
                                 "Expected ${expected}, but was $result")
                }
            }

            on("converting to text") {
                it("returns a plain text string string") {
                    val expected = "0=0 0 50 50" +System.lineSeparator() +
                                   "1=51 51 50 50"

                    val result = createTextMetadata(spriteBoundsList)

                    assertTrue(result.isNotEmpty(),
                               "Exepcted a non-empty plain text string")
                    assertEquals(expected, result,
                                 "Expected ${expected}, but was $result")
                }
            }
        }
    }
}}