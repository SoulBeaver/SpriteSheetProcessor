package com.sbg.rpg.unpacker

import com.sbg.rpg.image.determineProbableBackgroundColor
import com.sbg.rpg.image.readImage
import com.sbg.rpg.image.toBufferedImage
import java.nio.file.Paths
import java.awt.Color
import kotlin.test.assertEquals
import org.jetbrains.spek.api.Spek

class FindProbableBackgroundColorSpec: Spek() { init {
    given("A function to determine probable background colors") {
        on("an image with a white background") {
            val emptyUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(emptyUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = image.determineProbableBackgroundColor()

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing a single sprite") {
            val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(singleSpriteUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = image.determineProbableBackgroundColor()

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing many sprites") {
            val manySpritesUrl = this.javaClass.classLoader.getResource("unpacker/ManySprites.gif")

            it("finds purple-ish as the probable background color") {
                val image = readImage(Paths.get(manySpritesUrl.toURI())!!).toBufferedImage()
                val expected = Color(109, 73, 138)

                val probableBackgroundColor = image.determineProbableBackgroundColor()

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }
    }
}}