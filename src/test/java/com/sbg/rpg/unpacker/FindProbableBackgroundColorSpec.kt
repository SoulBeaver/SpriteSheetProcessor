package com.sbg.rpg.unpacker

import java.nio.file.Paths
import java.awt.Color
import kotlin.test.assertEquals
import com.sbg.rpg.util.readImage
import com.sbg.rpg.util.toBufferedImage
import com.sbg.rpg.util.determineProbableBackgroundColor
import org.jetbrains.spek.api.Spek

class FindProbableBackgroundColorSpec: Spek() { init {
    given("A function to determine probable background colors") {
        on("an image with a white background") {
            val emptyUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(emptyUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = determineProbableBackgroundColor(image)

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing a single sprite") {
            val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(singleSpriteUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = determineProbableBackgroundColor(image)

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing many sprites") {
            val manySpritesUrl = this.javaClass.classLoader.getResource("unpacker/ManySprites.gif")

            it("finds purple-ish as the probable background color") {
                val image = readImage(Paths.get(manySpritesUrl.toURI())!!).toBufferedImage()
                val expected = Color(109, 73, 138)

                val probableBackgroundColor = determineProbableBackgroundColor(image)

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }
    }
}}