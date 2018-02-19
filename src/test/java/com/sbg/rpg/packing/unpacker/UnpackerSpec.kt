package com.sbg.rpg.packing.unpacker

import com.sbg.rpg.packing.common.SpriteCutter
import com.sbg.rpg.packing.common.SpriteDrawer
import com.sbg.rpg.packing.common.extensions.readImage
import java.nio.file.Paths
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import java.awt.Rectangle
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import javax.imageio.ImageIO

object UnpackerSpec : Spek({
    given("A SpriteSheet Unpacker") {
        val spriteSheetUnpacker = SpriteSheetUnpacker(SpriteCutter(SpriteDrawer()))

        on("a file without sprites") {
            val emptyPngUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

            it("returns an empty list") {
                val sprites = spriteSheetUnpacker.unpack(Paths.get(emptyPngUrl.toURI()).readImage())

                assertTrue(sprites.isEmpty(),
                        "Loading an empty file should produce an empty list of Sprites")
            }
        }

        on("a file with one sprite") {
            val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(singleSpriteUrl.toURI()).readImage())

            it("returns a list of one sprite common") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }

            it("returns a list of one sprite common with the correct dimensions") {
                val expectedDimensions = Rectangle(0, 0, 108, 129)
                val actualDimensions = Rectangle(0,
                        0,
                        sprites.first().getWidth(null),
                        sprites.first().getHeight(null))

                assertEquals(
                        expectedDimensions,
                        actualDimensions,
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with an already cropped sprite") {
            val alreadyCroppedUrl = this.javaClass.classLoader.getResource("unpacker/AlreadyCropped.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(alreadyCroppedUrl.toURI()).readImage())

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
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with a non-white background and one common") {
            val coloredBackgroundUrl = this.javaClass.classLoader.getResource("unpacker/ColoredBackground.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(coloredBackgroundUrl.toURI()).readImage())

            it("returns a list of one sprite common") {
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
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})"
                )
            }
        }

        on("a file with a transparent background") {
            val transparentBackgroundUrl = this.javaClass.classLoader.getResource("unpacker/MultipleSprites_TransparentBackground.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(transparentBackgroundUrl.toURI()).readImage())

            it("returns a list of two sprites") {
                assertEquals(2, sprites.size, "Expected to have found exactly two sprites")
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
            val sprites = spriteSheetUnpacker.unpack(Paths.get(diagonalUrl.toURI()).readImage())

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
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }

        on("a file with a single pixel") {
            val pixelUrl = this.javaClass.classLoader.getResource("unpacker/Pixel.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(pixelUrl.toURI()).readImage())

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
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }

        on("a rectangle with a width of one pixel and no corners") {
            val rectangleNoCornersUrl = this.javaClass.classLoader.getResource("unpacker/Rectangle_NoCorners.png")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(rectangleNoCornersUrl.toURI()).readImage())

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
                        "Size of common not as expected. Expected (${expectedDimensions.width}, ${expectedDimensions.height}) but was (${actualDimensions.width}, ${actualDimensions.height})")
            }
        }

        on("a large png with transparent background") {
            val rectangleNoCornersUrl = this.javaClass.classLoader.getResource("unpacker/Slow.png")

            it("returns a list of 10 sprites in a timely manner") {
                /*
                 * Spek does not have support for timeouts just yet. We have to manually interrupt the tests
                 * in case this function is taking too long.
                 */
                val sprites = spriteSheetUnpacker.unpack(Paths.get(rectangleNoCornersUrl.toURI()).readImage())

                assertEquals(10, sprites.size)
            }
        }

        on("a .jpg file") {
            val jpeg = this.javaClass.classLoader.getResource("unpacker/Jpeg.jpg")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(jpeg.toURI()).readImage())

            it("returns a list of exactly one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }
        }

        on("a .jpg file of poor quality") {
            val badJpeg = this.javaClass.classLoader.getResource("unpacker/BadJpeg.jpg")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(badJpeg.toURI()).readImage())

            it("still returns only one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }
        }

        // It's pronounced .gif like Jeff. Feel free to unstar me now :)
        on("a .gif file") {
            val gif = this.javaClass.classLoader.getResource("unpacker/Gif.gif")
            val sprites = spriteSheetUnpacker.unpack(Paths.get(gif.toURI()).readImage())

            it("returns a list of exactly one sprite") {
                assertEquals(1, sprites.size, "Expected to have found exactly one sprite")
            }
        }
    }
})