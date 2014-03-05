package com.sbg.rpg.unpacker

import org.spek.Spek
import java.nio.file.Paths
import java.awt.Color
import kotlin.test.assertEquals
import com.sbg.rpg.util.readImage
import com.sbg.rpg.util.toBufferedImage
import com.sbg.rpg.util.determineProbableBackgroundColor

class FindProbableBackgroundColorSpec: Spek() {{
    given("A function to determine probable background colors") {
        on("an image with a white background") {
            val emptyUrl = javaClass<FindProbableBackgroundColorSpec>().getClassLoader()!!.getResource("unpacker/Empty.png")!!

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(emptyUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = determineProbableBackgroundColor(image)

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing a single sprite") {
            val singleSpriteUrl = javaClass<FindProbableBackgroundColorSpec>().getClassLoader()!!.getResource("unpacker/SingleSprite.png")!!

            it("finds white as the probable background color") {
                val image = readImage(Paths.get(singleSpriteUrl.toURI())!!).toBufferedImage()
                val expected = Color(255, 255, 255)

                val probableBackgroundColor = determineProbableBackgroundColor(image)

                assertEquals(expected, probableBackgroundColor,
                             "Expected $expected as background color, but was $probableBackgroundColor")
            }
        }

        on("an image containing many sprites") {
            val manySpritesUrl = javaClass<FindProbableBackgroundColorSpec>().getClassLoader()!!.getResource("unpacker/ManySprites.gif")!!

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