package com.sbg.rpg.unpacker

import com.sbg.rpg.image.ImageReadException
import java.nio.file.Paths
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import java.awt.Rectangle
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFailsWith

class UnpackerSpec: Spek() { init {
    given("A SpriteSheet Unpacker") {
        val spriteSheetUnpacker = SpriteSheetUnpacker()

        on("a missing file") {
            it("throws an IllegalArgumentException") {
                assertFailsWith<IllegalArgumentException> {
                    spriteSheetUnpacker.unpack(Paths.get("missing"))
                }
            }
        }

        on("an empty file") {
            val emptyFileUrl = this.javaClass.classLoader.getResource("unpacker/Empty.txt")

            it("throws an ImageReadException") {
                assertFailsWith<ImageReadException> {
                    spriteSheetUnpacker.unpack(Paths.get(emptyFileUrl.toURI())!!)
                }
            }
        }

        on("a file without sprites") {
            val emptyPngUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

            it("returns an empty list") {
                val sprites = spriteSheetUnpacker.unpack(Paths.get(emptyPngUrl.toURI()))

                assertTrue(sprites.isEmpty(),
                           "Loading an empty file should produce an empty list of Sprites")
            }
        }

        on("a file with one sprite") {
            val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(singleSpriteUrl.toURI()))

            it("returns a list of one sprite image") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns a list of one sprite image with the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 108, 129)
                val actualDimensions = Rectangle(0,
                        0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with an already cropped sprite") {
            val alreadyCroppedUrl = this.javaClass.classLoader.getResource("unpacker/AlreadyCropped.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(alreadyCroppedUrl.toURI()))

            it("returns a list of the sprite unaltered") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns a list of sprites with correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 107, 128)
                val actualDimensions = Rectangle(0, 0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with many sprites") {
            val manySpritesUrl = this.javaClass.classLoader.getResource("unpacker/ManySprites.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(manySpritesUrl.toURI())!!)

            it("returns a list of multiple sprite images") {
                assertEquals(14, sprites.size, "Expected to have found exactly 14 sprites.")
            }

            it("returns a list of sprites with correct dimensions") {
                val expectedDimensionsList = arrayOf(
                        Rectangle(0, 0, 123, 189),
                        Rectangle(0, 0, 177, 183),
                        Rectangle(0, 0, 111, 180),
                        Rectangle(0, 0, 105, 189),
                        Rectangle(0, 0, 102, 171),
                        Rectangle(0, 0, 108, 129),
                        Rectangle(0, 0, 129, 135),
                        Rectangle(0, 0, 186, 132),
                        Rectangle(0, 0, 87, 186),
                        Rectangle(0, 0, 156, 186),
                        Rectangle(0, 0, 93, 183),
                        Rectangle(0, 0, 120, 183),
                        Rectangle(0, 0, 135, 186),
                        Rectangle(0, 0, 102, 189)
                )

                sprites.forEach {
                    val actualDimensions = Rectangle(0, 0, it.getWidth(null), it.getHeight(null))
                    assertTrue(
                            expectedDimensionsList.contains(actualDimensions),
                            "Did not find a sprite of size (${actualDimensions.width}, ${actualDimensions.height}) in list of expected dimensions."
                    )
                }
            }
        }

        on("a file with a non-white background and one image") {
            val coloredBackgroundUrl = this.javaClass.classLoader.getResource("unpacker/ColoredBackground.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(coloredBackgroundUrl.toURI()))

            it("returns a list of one sprite image") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 186, 132)
                val actualDimensions = Rectangle(0, 0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with a transparent background") {
            val transparentBackgroundUrl = this.javaClass.classLoader.getResource("unpacker/MultipleSprites_TransparentBackground.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(transparentBackgroundUrl.toURI()))

            it("returns a list of four sprites") {
                assertEquals(4, sprites.size, "Expected to have found exactly four sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensionsList = arrayOf(
                            Rectangle(0, 0, 30, 40),
                            Rectangle(0, 0, 31, 39),
                            Rectangle(0, 0, 40, 42),
                            Rectangle(0, 0, 36, 38)
                )

                sprites.forEach {
                    val actualDimensions = Rectangle(0, 0, it.getWidth(null), it.getHeight(null))
                    assertTrue(
                            expectedDimensionsList.contains(actualDimensions),
                            "Did not find a sprite of size (${actualDimensions.width}, ${actualDimensions.height}) in list of expected dimensions."
                    )
                }
            }
        }
    }
}}