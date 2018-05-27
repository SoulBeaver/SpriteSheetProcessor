package com.sbg.rpg.packing.unpacker

import com.sbg.rpg.packing.common.extensions.probableBackgroundColor
import com.sbg.rpg.packing.common.extensions.readImage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.awt.Color
import java.nio.file.Paths
import kotlin.test.assertEquals

object FindProbableBackgroundColorSpec: Spek({
    given("An image with no sprites and a white background") {
        val emptyUrl = this.javaClass.classLoader.getResource("unpacker/Empty.png")

        on("finding the probably background color") {
            it("finds the color white") {
                val image = Paths.get(emptyUrl.toURI()).readImage()
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
                val image = Paths.get(singleSpriteUrl.toURI()).readImage()
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
                val image = Paths.get(manySpritesUrl.toURI()).readImage()
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
                val image = Paths.get(transparentUrl.toURI()).readImage()
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
                val image = Paths.get(transparentUrl.toURI()).readImage()
                val expected = Color(0, 0, 0, 0)

                val probableBackgroundColor = image.probableBackgroundColor()

                assertEquals(expected, probableBackgroundColor)
            }
        }
    }
})