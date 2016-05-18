package com.sbg.rpg.util

import org.jetbrains.spek.api.Spek
import java.nio.file.Paths
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import java.awt.Color
import java.awt.Dimension

class ImageUtilsSpec: Spek() { init {
    given("An image") {
        val imageUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")!!

        on("that is a BufferedImage") {
            val image = readImage(Paths.get(imageUrl.toURI())!!)

            it("should preserve identity") {
                val actual = image.toBufferedImage()

                assertEquals(image, actual)
            }
        }
    }

    given("An image") {
        val imageUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")!!

        on("that needs to have a copy made") {
            val original = readImage(Paths.get(imageUrl.toURI())!!).toBufferedImage()

            it("should create a deep copy") {
                val copy = copy(original)

                assertFalse(original == copy)
            }

            it("should be an exact replica of the original") {
                val copy = copy(original)

                assertEquals(original.getWidth(), copy.getWidth())
                assertEquals(original.getHeight(), copy.getHeight())

                for (y in 0..copy.getHeight() - 1) {
                    for (x in 0..copy.getWidth() - 1) {
                        val originalAtXY = original.getRGB(x, y)
                        val copyAtXY = copy.getRGB(x, y)

                        assertEquals(originalAtXY, copyAtXY,
                                     "The copied image is not identical to the original.")
                    }
                }

            }
        }
    }

    given("A 200x200 image") {
        val imageUrl = this.javaClass.classLoader.getResource("unpacker/200x200.png")!!

        on("looping through it") {
            val image = readImage(Paths.get(imageUrl.toURI())!!).toBufferedImage()

            it("should loop fourty-thousand times") {
                var counter = 0

                for (pixel in image)
                    counter += 1

                assertEquals(40000, counter, "Expected to loop through 40k pixels, but was $counter")
            }
        }
    }

    given("A 200x200 image") {
        val imageUrl = this.javaClass.classLoader.getResource("unpacker/200x200.png")!!

        on("creating a simple copy") {
            val image = readImage(Paths.get(imageUrl.toURI())!!).toBufferedImage()
            val copy = copy(image)

            it("should be distinct") {
                assertFalse(copy == image)
            }

            it("should contain the same content") {
                for (pixel in copy)
                    assertEquals(pixel.color, Color(image.getRGB(pixel.point.x, pixel.point.y)))
            }
        }

        on("creating a larger copy with black border") {
            val image = readImage(Paths.get(imageUrl.toURI())!!).toBufferedImage()
            val copyWithBorder = copyWithBorder(image,
                                                Dimension(image.getWidth() + 10,
                                                          image.getHeight() + 10),
                                                Color.BLACK)

            it("should be 210x210 large") {
                assertEquals(210, copyWithBorder.getWidth(),
                             "Expected width of border copy to be 210, but was ${copyWithBorder.getWidth()}")
                assertEquals(210, copyWithBorder.getHeight(),
                             "Expected width of border copy to be 210, but was ${copyWithBorder.getHeight()}")
            }

            it("should have a 5 pixel wide border on all sides") {
                for (y in 0..4) {
                    for (x in 0..copyWithBorder.getWidth() - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (y in 205..209) {
                    for (x in 0..copyWithBorder.getWidth() - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (x in 0..4) {
                    for (y in 0..copyWithBorder.getHeight() - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (x in 205..209) {
                    for (y in 0..copyWithBorder.getHeight() - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }
            }
        }

        on("creating 50x50 sub-image copy") {
            val image = readImage(Paths.get(imageUrl.toURI())!!).toBufferedImage()
            val subImage = copySubImage(image, java.awt.Rectangle(0, 0, 50, 50))

            it("should be exactly 50x50") {
                assertEquals(50, subImage.getWidth(),
                             "Expected sub-image width of 50, but was ${subImage.getWidth()}")
                assertEquals(50, subImage.getHeight(),
                             "Expected sub-image height of 50, but was ${subImage.getHeight()}")
            }
        }
    }
}}