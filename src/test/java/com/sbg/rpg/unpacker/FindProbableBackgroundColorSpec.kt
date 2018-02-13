package com.sbg.rpg.unpacker

import com.sbg.rpg.image.probableBackgroundColor
import com.sbg.rpg.image.readImage
import java.nio.file.Paths
import java.awt.Color
import kotlin.test.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class FindProbableBackgroundColorSpec: Spek({
    given("An image with no sprites and a white background") {
        val emptyUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

        on("finding the probably background color") {
            it("finds the color white") {
                val image = readImage(Paths.get(emptyUrl.toURI()))
                val expected = Color(255, 255, 255, 255)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }

    given("An image with a single sprite and a white background") {
        val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")

        on("finding the probably background color") {
            it("finds the color white") {
                val image = readImage(Paths.get(singleSpriteUrl.toURI()))
                val expected = Color(255, 255, 255, 255)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }

    given ("An image with many sprites and a purple background") {
        val manySpritesUrl = this.javaClass.classLoader.getResource("unpacker/ManySprites.png")

        on("finding the probably background color") {
            it("finds the color purple") {
                val image = readImage(Paths.get(manySpritesUrl.toURI()))
                val expected = Color(109, 73, 138, 255)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }

    given ("A densely packed image with many sprite and a pink background") {
        val transparentUrl = this.javaClass.classLoader.getResource("unpacker/DensePink.png")

        on("finding the probably background color") {
            it("finds the color pink") {
                val image = readImage(Paths.get(transparentUrl.toURI()))
                val expected = Color(255, 0, 255, 255)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }

    given ("An image with many sprites and a transparent background") {
        val transparentUrl = this.javaClass.classLoader.getResource("unpacker/Pixel.png")

        on("finding the probably background color") {
            it("finds the color transparent") {
                val image = readImage(Paths.get(transparentUrl.toURI()))
                val expected = Color(0, 0, 0, 0)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }
})