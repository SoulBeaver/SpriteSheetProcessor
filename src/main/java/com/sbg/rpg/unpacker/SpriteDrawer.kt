package com.sbg.rpg.unpacker

import com.sbg.rpg.image.iterator
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

/**
 * TODO: Write me.
 */
class SpriteDrawer {
    private val logger = LogManager.getLogger(SpriteSheetUnpacker::class.simpleName)

    /**
     * TODO: Write me.
     */
    fun draw(from: BufferedImage, area: Rectangle, colorToClear: Color): BufferedImage {
        if (area.width > from.width || area.height > from.height) {
            logger.info("Requested sub-image is larger than source image. Returning copy of source image instead.")

            area.width = Math.min(from.width, area.width)
            area.height = Math.min(from.height, area.height)
        }

        logger.debug("Drawing sprite from source area [${area.x}, ${area.y}, ${area.width}, ${area.height}]")
        val subImage = from.getSubimage(area.x, area.y,
                                        area.width, area.height)

        return drawSprite(subImage, colorToClear)
    }

    fun drawMultiple(from: BufferedImage, areas: List<Rectangle>, colorToClear: Color) = areas.map { draw(from, it, colorToClear) }

    private fun drawSprite(source: BufferedImage, colorToClear: Color): BufferedImage {
        val sprite = BufferedImage(source.width, source.height, BufferedImage.TYPE_INT_ARGB)

        for (pixel in source) {
            if (pixel.color != colorToClear)
                sprite.setRGB(pixel.point.x, pixel.point.y, pixel.color.rgb)
            else
                sprite.setRGB(pixel.point.x, pixel.point.y, Color(0, 0, 0, 0).rgb)
        }

        return sprite
    }
}