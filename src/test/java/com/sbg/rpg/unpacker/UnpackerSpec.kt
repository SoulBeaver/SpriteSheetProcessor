package com.sbg.rpg.unpacker

import com.sbg.rpg.image.ImageReadException
import java.nio.file.Paths
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import java.awt.Rectangle
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.skip
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
                assertEquals(2, sprites.size, "Expected to have found exactly four sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensionsList = arrayOf(
                            Rectangle(0, 0, 30, 40),
                            Rectangle(0, 0, 31, 39)
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

        on("a file with a single diagonal line of individual pixels") {
            val diagonalUrl = this.javaClass.classLoader.getResource("unpacker/Diagonal.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(diagonalUrl.toURI()))

            it("returns a list of exactly one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 5, 5)
                val actualDimensions = Rectangle(0, 0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }

        on("a file with a single pixel") {
            val pixelUrl = this.javaClass.classLoader.getResource("unpacker/Pixel.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(pixelUrl.toURI()))

            it("returns a list of exactly one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 1, 1)
                val actualDimensions = Rectangle(0, 0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }

        on("a rectangle with a width of one pixel and no corners") {
            val rectangleNoCornersUrl = this.javaClass.classLoader.getResource("unpacker/Rectangle_NoCorners.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(rectangleNoCornersUrl.toURI()))

            it("returns a list of exactly one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 10, 16)
                val actualDimensions = Rectangle(0, 0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of image not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }
    }
}}