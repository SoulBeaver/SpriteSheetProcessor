package com.sbg.rpg.packer

import com.sbg.rpg.util.iterator
import com.sbg.rpg.image.SpriteDrawer
import com.sbg.rpg.image.readImage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.awt.image.BufferedImage
import java.nio.file.Paths
import kotlin.test.assertEquals

object SpriteSheetPackerSpec: Spek({
    given("A SpriteSheetPacker") {
        val packer = SpriteSheetPacker(SpriteDrawer())

        on("packing a single sprite") {
            val sprites = listOf(BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB))

            it("creates a 100x100 large image") {
                val image = packer.pack(sprites)

                assertEquals(image.width, 100)
                assertEquals(image.height, 100)
            }
        }

        on("packing homogeneous sprites") {
            val sprites = listOf(
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
            )

            it ("creates a 100x300 image") {
                val image = packer.pack(sprites)

                assertEquals(image.width, 100)
                assertEquals(image.height, 300)
            }
        }

        on("packing non-homogeneous sprites") {
            val sprites = listOf(
                    BufferedImage(40, 60, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(20, 60, BufferedImage.TYPE_INT_ARGB)
            )

            it("creates a 60x60 large image") {
                val image = packer.pack(sprites)

                assertEquals(image.width, 60)
                assertEquals(image.height, 60)
            }
        }

        on("packing many non-homogeneous sprites") {
            val sprites = listOf(
                    BufferedImage(40, 60, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(10, 60, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(10, 60, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)
            )

            it("creates a 60x70 large image") {
                val image = packer.pack(sprites)

                assertEquals(image.width, 60)
                assertEquals(image.height, 70)
            }
        }

        given("real sprites") {
            on("trying to draw one sprite") {
                val sprites = listOf(
                        this.javaClass.classLoader.getResource("packer/1.png")
                ).map { url -> readImage(Paths.get(url.toURI())!!) }

                val expected: BufferedImage =
                        readImage(Paths.get(this.javaClass.classLoader.getResource("packer/result_1.png").toURI()))

                it("draws an identical sprite") {
                    val image = packer.pack(sprites)

                    assertEquals(image.width, 39)
                    assertEquals(image.height, 39)

                    for ((point, color) in expected) {
                        assertEquals(color.rgb, image.getRGB(point.x, point.y))
                    }
                }
            }

            on("trying to draw multiple sprites") {
                val sprites = listOf(
                        this.javaClass.classLoader.getResource("packer/1.png"),
                        this.javaClass.classLoader.getResource("packer/2.png"),
                        this.javaClass.classLoader.getResource("packer/3.png")
                ).map { url -> readImage(Paths.get(url.toURI())) }

                val expected: BufferedImage =
                        readImage(Paths.get(this.javaClass.classLoader.getResource("packer/result_2.png").toURI()))

                it("packs them into a single image") {
                    val image = packer.pack(sprites)

                    assertEquals(image.width, 38)
                    assertEquals(image.height, 115)
                }
            }
        }
    }
})