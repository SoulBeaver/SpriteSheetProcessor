package com.sbg.rpg.util

import com.sbg.rpg.image.*
import org.jetbrains.spek.api.Spek
import java.nio.file.Paths
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import kotlin.test.assertNotEquals

class ImageUtilsSpec: Spek() { init {
    given("An image") {
        val imageUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")!!

        on("converting to a BufferedImage") {
            val original = readImage(Paths.get(imageUrl.toURI()))

            it("does not perform a conversion if the image is already of type BufferedImage and has the same color type") {
                val expected = original.toBufferedImage()

                assertEquals(original.type, expected.type, "Expected original and expected to be identical.")
            }

            it("converts the image type from ARGB to [something]") {
                val expected = original.toBufferedImage(BufferedImage.TYPE_3BYTE_BGR)

                assertNotEquals(expected.type, original.type, "Expected converted BufferedImage to have a different type than original.")
                assertEquals(BufferedImage.TYPE_3BYTE_BGR, expected.type)
            }
        }

        on("that needs to have a copy made") {
            val original = readImage(Paths.get(imageUrl.toURI()))

            it("should create a deep copy") {
                val copy = original.copy()

                assertFalse(original == copy)
            }

            it("should be an exact replica of the original") {
                val copy = original.copy()

                assertEquals(original.width, copy.width)
                assertEquals(original.height, copy.height)

                for (y in 0..copy.height - 1) {
                    for (x in 0..copy.width - 1) {
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
            val image = readImage(Paths.get(imageUrl.toURI())!!)

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
            val image = readImage(Paths.get(imageUrl.toURI())!!)
            val copy = image.copy()

            it("should be distinct") {
                assertFalse(copy == image)
            }

            it("should contain the same content") {
                for (pixel in copy)
                    assertEquals(pixel.color, Color(image.getRGB(pixel.point.x, pixel.point.y)))
            }
        }

        on("creating a larger copy with black border") {
            val image = readImage(Paths.get(imageUrl.toURI())!!)
            val copyWithBorder = image.copyWithBorder(
                    Dimension(image.width + 10,
                              image.height + 10),
                    Color.BLACK
            )

            it("should be 210x210 large") {
                assertEquals(210, copyWithBorder.width,
                             "Expected width of border copy to be 210, but was ${copyWithBorder.width}")
                assertEquals(210, copyWithBorder.height,
                             "Expected width of border copy to be 210, but was ${copyWithBorder.height}")
            }

            it("should have a 5 pixel wide border on all sides") {
                for (y in 0..4) {
                    for (x in 0..copyWithBorder.width - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (y in 205..209) {
                    for (x in 0..copyWithBorder.width - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (x in 0..4) {
                    for (y in 0..copyWithBorder.height - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }

                for (x in 205..209) {
                    for (y in 0..copyWithBorder.height - 1) {
                        assertEquals(Color.BLACK, Color(copyWithBorder.getRGB(x, y)))
                    }
                }
            }
        }
    }
}}