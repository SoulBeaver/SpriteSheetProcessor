package com.sbg.rpg.packer

import com.sbg.rpg.image.SpriteCutter
import com.sbg.rpg.image.SpriteDrawer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.awt.image.BufferedImage
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
    }
})