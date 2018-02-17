package com.sbg.rpg.packing.packer

import com.sbg.rpg.packing.common.Sprite
import com.sbg.rpg.packing.common.SpriteDrawer
import com.sbg.rpg.packing.packer.metadata.MetadataCreator
import com.sbg.rpg.packing.packer.model.SpriteBounds
import com.sbg.rpg.packing.packer.model.Strip
import com.sbg.rpg.packing.packer.model.growHorizontally
import com.sbg.rpg.packing.common.extensions.area
import org.apache.logging.log4j.LogManager
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage

data class PackedSpriteSheet(val canvas: BufferedImage, val metadata: String)

class SpriteSheetPacker(private val spriteDrawer: SpriteDrawer, private val metadataCreator: MetadataCreator) {
    private val logger = LogManager.getLogger(SpriteSheetPacker::class.simpleName)

    public fun pack(sprites: List<Sprite>): PackedSpriteSheet {
        logger.debug("Packing ${sprites.size} sprites.")

        val spritesByAreaDesc = sprites.sortedBy(Sprite::area).reversed()

        logger.debug("Sprite areas:  ${sprites.sortedBy(Sprite::area).map(Sprite::area).reversed()}")

        val positionedSprites = packSprites(sprites, squareStrip(spritesByAreaDesc.first()))

        logger.debug("Positioned sprites:  ${positionedSprites.map { it.startingPoint }}")

        val canvas = drawCanvas(positionedSprites)
        val metadata = createMetadata(positionedSprites)

        logger.debug("Created a canvas of size (${canvas.width}, ${canvas.height})")
        logger.debug("Metadata file:  $metadata")

        return PackedSpriteSheet(canvas, metadata)
    }

    private fun packSprites(sprites: List<Sprite>, startingStrip: Strip): List<PositionedSprite> {
        val positionedSprites = mutableListOf<PositionedSprite>()
        var strips = mutableListOf(startingStrip)

        logger.debug("Starting strip has dimensions [0, 0, ${startingStrip.width}, ${startingStrip.height}]")

        for (sprite in sprites) {
            var fittingStrip = strips.find { fitsInStrip(sprite, it) }

            if (fittingStrip == null) {
                val latestStrip = strips.last()

                val newStrip = Strip(
                        0,
                        latestStrip.y + latestStrip.height,
                        startingStrip.width,
                        sprite.height)

                logger.debug("Unable to find a suitable strip, creating new strip [${newStrip.x}, ${newStrip.y}, ${newStrip.width}, ${newStrip.height}]")

                strips = strips.growHorizontally(sprite.width).toMutableList()
                strips.add(newStrip)

                fittingStrip = newStrip
            }

            logger.debug("Attempting to place sprite in strip is [${fittingStrip.x}, ${fittingStrip.y}, ${fittingStrip.width}, ${fittingStrip.height}]")

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

    private fun drawCanvas(positionedSprites: List<PositionedSprite>): BufferedImage {
        val canvasDimensions = spanCanvas(positionedSprites)
        val canvas = BufferedImage(
                canvasDimensions.width,
                canvasDimensions.height,
                BufferedImage.TYPE_INT_ARGB)

        logger.debug("Final common has dimensions [0, 0, ${canvasDimensions.width}, ${canvasDimensions.height}]")

        for ((sprite, startingPoint) in positionedSprites) {
            spriteDrawer.drawInto(sprite, canvas, startingPoint)
        }

        return canvas
    }

    private fun spanCanvas(positionedSprites: List<PositionedSprite>): Rectangle {
        val maxWidth = positionedSprites.map { (sprite, point) -> point.x + sprite.width }.max()!!
        val maxHeight = positionedSprites.map { (sprite, point) -> point.y + sprite.height }.max()!!

        return Rectangle(0, 0, maxWidth, maxHeight)
    }

    private fun createMetadata(positionedSprites: List<PositionedSprite>): String {
        val spriteBounds = positionedSprites.mapIndexed { idx, (sprite, coords) ->
            SpriteBounds(idx, Rectangle(coords.x, coords.y, coords.x + sprite.width, coords.y + sprite.height))
        }

        return metadataCreator.create(spriteBounds)
    }

    private data class PositionedSprite(val sprite: Sprite, val startingPoint: Point)
}