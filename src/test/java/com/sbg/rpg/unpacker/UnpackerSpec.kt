package com.sbg.rpg.unpacker

import org.spek.Spek
import kotlin.test.failsWith
import java.nio.file.Paths
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import java.awt.Rectangle
import java.awt.Color
import java.awt.image.BufferedImage
import com.sbg.rpg.util.ImageReadException

class UnpackerSpec: Spek() {{
    given("A SpriteSheet Unpacker") {
        on("a missing file") {
            it("throws an IllegalArgumentException") {
                failsWith(javaClass<IllegalArgumentException>()) {
                    unpack(Paths.get("missing")!!)
                }
            }
        }

        on("an empty file") {
            val emptyFileUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/Empty.txt")!!

            it("throws an ImageReadException") {
                failsWith(javaClass<ImageReadException>()) {
                    unpack(Paths.get(emptyFileUrl.toURI())!!)
                }
            }
        }

        on("a file without sprites") {
            val emptyPngUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/Empty.png")!!

            it("returns an empty list") {
                val sprites = unpack(Paths.get(emptyPngUrl.toURI())!!)

                assertTrue(sprites.isEmpty(),
                           "Loading an empty file should produce an empty list of Sprites")
            }
        }

        on("a file with one sprite") {
            val singleSpriteUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/SingleSprite.png")!!

            it("returns a list of one sprite image") {
                val sprites = unpack(Paths.get(singleSpriteUrl.toURI())!!)

                assertEquals(1, sprites.size(),
                             "Expected to have found exactly one sprite")

                val expectedBounds = Rectangle(0, 0, 107, 128)
                val actualBounds = Rectangle(0,
                                             0,
                                             sprites.first!!.getWidth(null),
                                             sprites.first!!.getHeight(null))

                assertEquals(expectedBounds, actualBounds,
                             "Size of image not as expected. Expected $expectedBounds but was $actualBounds")
            }
        }

        on("a file with an already cropped sprite") {
            val alreadyCroppedUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/AlreadyCropped.png")!!

            it("returns a list of the sprite unaltered") {
                val sprites = unpack(Paths.get(alreadyCroppedUrl.toURI())!!)

                assertEquals(1, sprites.size(),
                             "Expected to have found exactly one sprite")

                val expectedBounds = Rectangle(0, 0, 107, 128)
                val actualBounds = Rectangle(0,
                                             0,
                                             sprites.first!!.getWidth(null),
                                             sprites.first!!.getHeight(null))

                assertEquals(expectedBounds, actualBounds,
                             "Size of image not as expected. Expected $expectedBounds but was $actualBounds")
            }
        }

        on("a file with many sprites") {
            val manySpritesUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/ManySprites.gif")!!

            it("returns a list of multiple sprite images") {
                val sprites = unpack(Paths.get(manySpritesUrl.toURI())!!)

                assertEquals(14, sprites.size())
            }
        }

        /*
        on("a file with a non-white background and one image") {
            val coloredBackgroundUrl = javaClass<UnpackerSpec>().getClassLoader()!!.getResource("unpacker/ColoredBackground.png")!!

            it("returns a list of one sprite image whose background is transparent") {
                val sprites = unpack(Paths.get(coloredBackgroundUrl.toURI())!!)

                assertEquals(1, sprites.size())

                val sprite = sprites.first
                assertEquals(Color(0, 0, 0, 0), Color((sprite as BufferedImage).getRGB(0, 0)))
            }
        }
        */

        on("a file with non-white background and multiple images") {
            it("returns a list of multiple sprite images whose backgrounds are transparent") {

            }
        }

        on("a file with a non-contiguous sprite") {
            it("returns a list of one sprite image") {

            }
        }

        on("a file with many non-contiguous sprites") {
            it("returns a list of many sprite images") {

            }
        }

        on("a file with a sprite containing a color similar to the background") {
            it("returns a list of one sprite whose colors are not modified") {

            }
        }

        on("a file with non-white background whose sprite also contains the background color") {
            it("returns a list of one sprite whose colors are not modified") {

            }
        }
    }
}}