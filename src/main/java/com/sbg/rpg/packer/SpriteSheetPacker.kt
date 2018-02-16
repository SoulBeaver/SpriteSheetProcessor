package com.sbg.rpg.packer

import com.sbg.rpg.image.ISpriteDrawer
import com.sbg.rpg.image.Sprite
import com.sbg.rpg.util.area
import org.apache.logging.log4j.LogManager
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage

class SpriteSheetPacker(private val spriteDrawer: ISpriteDrawer) {
    private val logger = LogManager.getLogger(SpriteSheetPacker::class.simpleName)

    public fun pack(sprites: List<Sprite>): BufferedImage {
        logger.info("Packing ${sprites.size} sprites.")

        val spritesByAreaDesc = sprites.sortedBy(Sprite::area).reversed()

        logger.info("Sprite areas:  ${sprites.sortedBy(Sprite::area).map(Sprite::area).reversed()}")

        val positionedSprites = packSprites(sprites, squareStrip(spritesByAreaDesc.first()))

        logger.info("Positioned sprites:  ${positionedSprites.map { it.startingPoint }}")

        val canvasDimensions = spanCanvas(positionedSprites)
        val canvas = BufferedImage(
                canvasDimensions.width,
                canvasDimensions.height,
                BufferedImage.TYPE_INT_ARGB)

        logger.info("Final image has dimensions [0, 0, ${canvasDimensions.width}, ${canvasDimensions.height}]")

        for ((sprite, startingPoint) in positionedSprites) {
            spriteDrawer.drawInto(sprite, canvas, startingPoint)
        }

        return canvas
    }

    private fun packSprites(sprites: List<Sprite>, startingStrip: Strip): List<PositionedSprite> {
        val positionedSprites = mutableListOf<PositionedSprite>()
        var strips = mutableListOf(startingStrip)

        logger.info("Starting strip has dimensions [0, 0, ${startingStrip.width}, ${startingStrip.height}]")

        for (sprite in sprites) {
            var fittingStrip = strips.find { fitsInStrip(sprite, it) }

            if (fittingStrip == null) {
                val latestStrip = strips.last()

                val newStrip = Strip(
                        0,
                        latestStrip.y + latestStrip.height,
                        startingStrip.width,
                        sprite.height)

                logger.info("Unable to find a suitable strip, creating new strip [${newStrip.x}, ${newStrip.y}, ${newStrip.width}, ${newStrip.height}]")

                strips = strips.growHorizontally(sprite.width).toMutableList()
                strips.add(newStrip)

                fittingStrip = newStrip
            }

            logger.info("Attempting to place sprite in strip is [${fittingStrip.x}, ${fittingStrip.y}, ${fittingStrip.width}, ${fittingStrip.height}]")

            val (spriteRect, remainingStrip) = placeSprite(sprite, fittingStrip)

            positionedSprites.add(spriteRect)
            strips[strips.indexOf(fittingStrip)] = remainingStrip
        }

        return positionedSprites
    }

    private fun fitsInStrip(sprite: Sprite, strip: Strip): Boolean {
        return sprite.height <= strip.height &&
                sprite.width <= strip.width
    }

    private fun placeSprite(sprite: Sprite, strip: Strip): Pair<PositionedSprite, Strip> {
        val startingPoint = PositionedSprite(
                sprite,
                Point(strip.x, strip.y)
        )

        return Pair(startingPoint, strip.cutLeft(sprite.width))
    }

    private fun squareStrip(sprite: BufferedImage): Strip {
        val len = Math.max(sprite.width, sprite.height)

        return Strip(0, 0, len, len)
    }

    private fun spanCanvas(positionedSprites: List<PositionedSprite>): Rectangle {
        val maxWidth = positionedSprites.map { (sprite, point) -> point.x + sprite.width }.max()!!
        val maxHeight = positionedSprites.map { (sprite, point) -> point.y + sprite.height }.max()!!

        return Rectangle(0, 0, maxWidth, maxHeight)
    }

    data class PositionedSprite(val sprite: Sprite, val startingPoint: Point)
}