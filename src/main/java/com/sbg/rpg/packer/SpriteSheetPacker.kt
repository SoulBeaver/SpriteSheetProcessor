package com.sbg.rpg.packer

import com.sbg.rpg.image.ISpriteDrawer
import com.sbg.rpg.image.Sprite
import com.sbg.rpg.util.area
import org.apache.logging.log4j.LogManager
import java.awt.Rectangle
import java.awt.image.BufferedImage

class SpriteSheetPacker(private val spriteDrawer: ISpriteDrawer) {
    private val logger = LogManager.getLogger(SpriteSheetPacker::class.simpleName)

    public fun pack(sprites: List<Sprite>): BufferedImage {
        logger.info("Packing ${sprites.size} sprites.")

        val spritesByAreaDesc = sprites.sortedBy(Sprite::area).reversed()

        logger.info("Sprite areas:  ${sprites.sortedBy(Sprite::area).map(Sprite::area).reversed()}")

        val startingRect = squareRect(spritesByAreaDesc.first())
        val strips = mutableListOf(startingRect)
        val spriteRects = mutableListOf<SpriteRect>()

        logger.info("Starting rect is [0, 0, ${startingRect.width}, ${startingRect.height}]")

        for (sprite in sprites) {
            var fittingStrip = strips.find { fitsInStrip(sprite, it) }

            if (fittingStrip == null) {
                val latestStrip = strips.last()

                val newStrip = Rectangle(
                        0,
                        latestStrip.y + latestStrip.height,
                        startingRect.width,
                        sprite.height)

                logger.info("Unable to find a suitable strip, creating new strip [${newStrip.x}, ${newStrip.y}, ${newStrip.width}, ${newStrip.height}]")

                strips.add(newStrip)
                fittingStrip = newStrip
            }

            logger.info("Attempting to place sprite in strip is [${fittingStrip.x}, ${fittingStrip.y}, ${fittingStrip.width}, ${fittingStrip.height}]")

            val (spriteRect, remainingStrip) = placeSprite(sprite, fittingStrip)

            spriteRects.add(spriteRect)
            strips[strips.indexOf(fittingStrip)] = remainingStrip
        }

        logger.info("Final image has dimensions [0, 0, ${startingRect.width}, ${strips.last().y + strips.last().height}]")

        val canvas = BufferedImage(
                startingRect.width,
                strips.last().y + strips.last().height,
                BufferedImage.TYPE_INT_ARGB)

        return canvas
    }

    private fun fitsInStrip(sprite: Sprite, strip: Rectangle): Boolean {
        return sprite.height <= strip.height &&
                sprite.width <= strip.width
    }

    private fun placeSprite(sprite: Sprite, strip: Rectangle): Pair<SpriteRect, Rectangle> {
        val spriteRect = SpriteRect(
                sprite,
                Rectangle(strip.x, strip.y, sprite.width, sprite.height)
        )

        val remainingStrip = Rectangle(
                strip.x + sprite.width,
                strip.y,
                strip.width - sprite.width,
                strip.height
        )

        return Pair(spriteRect, remainingStrip)
    }

    private fun squareRect(sprite: BufferedImage): Rectangle {
        val len = Math.max(sprite.width, sprite.height)

        return Rectangle(0, 0, len, len)
    }

    data class SpriteRect(val sprite: Sprite, val rect: Rectangle)
}