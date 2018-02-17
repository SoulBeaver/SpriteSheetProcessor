package com.sbg.rpg.packing.packer

import com.sbg.rpg.packing.util.iterator
import com.sbg.rpg.packing.image.SpriteDrawer
import com.sbg.rpg.packing.metadata.TextMetadataCreator
import com.sbg.rpg.packing.util.readImage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.awt.image.BufferedImage
import java.nio.file.Paths
import kotlin.test.assertEquals

object SpriteSheetPackerSpec: Spek({
    given("A SpriteSheetPacker") {
        val packer = SpriteSheetPacker(SpriteDrawer(), TextMetadataCreator())

        on("packing a single sprite") {
            val sprites = listOf(BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB))

            it("creates a 100x100 large image") {
                val (canvas, _) = packer.pack(sprites)

                assertEquals(canvas.width, 100)
                assertEquals(canvas.height, 100)
            }
        }

        on("packing homogeneous sprites") {
            val sprites = listOf(
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
            )

            it ("creates a 100x300 image") {
                val (canvas, _) = packer.pack(sprites)

                assertEquals(canvas.width, 200)
                assertEquals(canvas.height, 200)
            }
        }

        on("packing non-homogeneous sprites") {
            val sprites = listOf(
                    BufferedImage(40, 60, BufferedImage.TYPE_INT_ARGB),
                    BufferedImage(20, 60, BufferedImage.TYPE_INT_ARGB)
            )

            it("creates a 60x60 large image") {
                val (canvas, _) = packer.pack(sprites)

                assertEquals(canvas.width, 60)
                assertEquals(canvas.height, 60)
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

            it("creates a 70x70 large image") {
                val (canvas, _) = packer.pack(sprites)

                assertEquals(canvas.width, 70)
                assertEquals(canvas.height, 70)
            }
        }

        given("real sprites") {
            on("trying to draw one sprite") {
                val sprites = listOf(
                        this.javaClass.classLoader.getResource("packer/1.png")
                ).map { url -> Paths.get(url.toURI())!!.readImage() }

                val expected: BufferedImage =
                        Paths.get(this.javaClass.classLoader.getResource("packer/result_1.png").toURI()).readImage()

                it("draws an identical sprite") {
                    val (canvas, _) = packer.pack(sprites)

                    assertEquals(canvas.width, 30)
                    assertEquals(canvas.height, 39)

                    for ((point, color) in expected) {
                        assertEquals(color.rgb, canvas.getRGB(point.x, point.y))
                    }
                }
            }

            on("trying to draw multiple sprites") {
                val sprites = listOf(
                        this.javaClass.classLoader.getResource("packer/1.png"),
                        this.javaClass.classLoader.getResource("packer/2.png"),
                        this.javaClass.classLoader.getResource("packer/3.png")
                ).map { url -> Paths.get(url.toURI()).readImage() }

                val expected: BufferedImage =
                        Paths.get(this.javaClass.classLoader.getResource("packer/result_2.png").toURI()).readImage()

                it("packs them into a single image") {
                    val (canvas, _) = packer.pack(sprites)

                    assertEquals(canvas.width, 62)
                    assertEquals(canvas.height, 77)

                    for ((point, color) in expected) {
                        assertEquals(color.rgb, canvas.getRGB(point.x, point.y))
                    }
                }
            }

            on("trying to draw multiple differently-sized sprites") {
                val sprites = listOf(
                        this.javaClass.classLoader.getResource("packer/5.png"),
                        this.javaClass.classLoader.getResource("packer/6.png"),
                        this.javaClass.classLoader.getResource("packer/7.png"),
                        this.javaClass.classLoader.getResource("packer/8.png"),
                        this.javaClass.classLoader.getResource("packer/9.png"),
                        this.javaClass.classLoader.getResource("packer/10.png"),
                        this.javaClass.classLoader.getResource("packer/11.png"),
                        this.javaClass.classLoader.getResource("packer/12.png"),
                        this.javaClass.classLoader.getResource("packer/13.png"),
                        this.javaClass.classLoader.getResource("packer/14.png"),
                        this.javaClass.classLoader.getResource("packer/15.png"),
                        this.javaClass.classLoader.getResource("packer/16.png"),
                        this.javaClass.classLoader.getResource("packer/17.png"),
                        this.javaClass.classLoader.getResource("packer/18.png"),
                        this.javaClass.classLoader.getResource("packer/19.png"),
                        this.javaClass.classLoader.getResource("packer/20.png")
                ).map { url -> Paths.get(url.toURI()).readImage() }

                val expected: BufferedImage =
                        Paths.get(this.javaClass.classLoader.getResource("packer/result_3.png").toURI()).readImage()

                it("packs them into a single image") {
                    val (canvas, _) = packer.pack(sprites)

                    assertEquals(canvas.width, 143)
                    assertEquals(canvas.height, 209)

                    for ((point, color) in expected) {
                        assertEquals(color.rgb, canvas.getRGB(point.x, point.y))
                    }
                }
            }
        }
    }
})