package com.sbg.rpg.unpacker

import com.sbg.rpg.image.readImage
import org.jetbrains.spek.api.Spek
import java.awt.Color
import java.awt.Rectangle
import java.nio.file.Paths
import kotlin.test.assertEquals

class SpriteDrawerSpec: Spek() { init {
    given("A sprite drawer") {
        val spriteDrawer = SpriteDrawer()

        on("copying a smaller sprite from source") {
            val singleSpriteUrl = this.javaClass.classLoader.getResource("unpacker/SingleSprite.png")
            val singleSpriteImage = readImage(Paths.get(singleSpriteUrl.toURI()))

            it("creates a new BufferedImage of said sprite") {
                val sprite = spriteDrawer.draw(singleSpriteImage, Rectangle(443, 200, 108, 129), Color.WHITE)

                assertEquals(Color(0, 0, 0, 0).rgb, sprite.getRGB(0, 0), "Expected top-left corner to be transparent.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(0, 119), "Expected right foot to be brown.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(106, 116), "Expected left foot to be brown.")
                assertEquals(Color(247, 247, 247).rgb, sprite.getRGB(85, 1), "Expected left foot to be brown.")
            }
        }

        on("copying a perfectly-cut sprite") {
            val croppedUrl = this.javaClass.classLoader.getResource("unpacker/AlreadyCropped.png")
            val croppedImage = readImage(Paths.get(croppedUrl.toURI()))

            it("has identical output to the previous test") {
                val sprite = spriteDrawer.draw(croppedImage, Rectangle(0, 0, 108, 129), Color.WHITE)

                assertEquals(Color(0, 0, 0, 0).rgb, sprite.getRGB(0, 0), "Expected top-left corner to be transparent.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(0, 119), "Expected right foot to be brown.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(106, 116), "Expected left foot to be brown.")
                assertEquals(Color(247, 247, 247).rgb, sprite.getRGB(85, 1), "Expected left foot to be brown.")
            }
        }

        on("trying to copy a sprite larger than the source image") {
            val croppedUrl = this.javaClass.classLoader.getResource("unpacker/AlreadyCropped.png")
            val croppedImage = readImage(Paths.get(croppedUrl.toURI()))

            it("constrains area to source image dimensions and returns identical output") {
                val sprite = spriteDrawer.draw(croppedImage, Rectangle(0, 0, 108, 129), Color.WHITE)

                assertEquals(Color(0, 0, 0, 0).rgb, sprite.getRGB(0, 0), "Expected top-left corner to be transparent.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(0, 119), "Expected right foot to be brown.")
                assertEquals(Color(115, 66, 16).rgb, sprite.getRGB(106, 116), "Expected left foot to be brown.")
                assertEquals(Color(247, 247, 247).rgb, sprite.getRGB(85, 1), "Expected left foot to be brown.")
            }
        }


        on("copying multiple sprites from source") {
            val multipleSpritesUrl = this.javaClass.classLoader.getResource("unpacker/ManySprites.png")
            val multipleSpritesImage = readImage(Paths.get(multipleSpritesUrl.toURI()))

            it("creates a list of three BufferedImages") {
                val sprites = spriteDrawer.drawMultiple(
                        multipleSpritesImage,
                        listOf(
                                Rectangle(42, 20, 123, 189),
                                Rectangle(215, 24, 177, 183),
                                Rectangle(428, 30, 111, 180)
                        ),
                        Color(109, 73, 138)
                )

                assertEquals(3, sprites.size, "Expected three sprites from SpriteDrawer.drawMultiple")
            }
        }

        on("copying a sprite with a transparent image") {
            val multipleSpritesTransparentBackgroundUrl = this.javaClass.classLoader.getResource("unpacker/MultipleSprites_TransparentBackground.png")
            val multipleSpritesTransparentBackgroundImage = readImage(Paths.get(multipleSpritesTransparentBackgroundUrl.toURI()))

            it("preserves transparency for each sprite") {
                val sprites = spriteDrawer.drawMultiple(
                        multipleSpritesTransparentBackgroundImage,
                        listOf(
                                Rectangle(7, 0, 30, 40),
                                Rectangle(48, 0, 31, 39),
                                Rectangle(85, 0, 40, 42),
                                Rectangle(133, 0, 36, 38)
                        ),
                        Color(0, 0, 0, 0)
                )

                assertEquals(4, sprites.size, "Expected three sprites from SpriteDrawer.drawMultiple")
            }
        }
    }
}}