package com.sbg.rpg.util

import org.spek.Spek
import java.nio.file.Paths
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import java.awt.Image

class ImageUtilsSpec: Spek() {{
    given("An image") {
        val imageUrl = javaClass<ImageUtilsSpec>().getClassLoader()!!.getResource("unpacker/SingleSprite.png")!!

        on("that is a BufferedImage") {
            val image = readImage(Paths.get(imageUrl.toURI())!!)

            it("should preserve identity") {
                val actual = image.toBufferedImage()

                assertEquals(image, actual)
            }
        }
    }

    given("An image") {
        val imageUrl = javaClass<ImageUtilsSpec>().getClassLoader()!!.getResource("unpacker/SingleSprite.png")!!

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
        val imageUrl = javaClass<ImageUtilsSpec>().getClassLoader()!!.getResource("unpacker/200x200.png")!!

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
        on("creating a simple copy") {
            it("should preserve identity") {

            }
        }

        on("creating a larger copy with black border") {
            it("should have a 5 pixel wide border on all sides") {

            }
        }

        on("creating 50x50 sub-image copy") {
            it("") {

            }
        }
    }
}}