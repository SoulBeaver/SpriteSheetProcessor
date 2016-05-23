package com.sbg.rpg.unpacker

import com.sbg.rpg.image.iterator
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

/**
 * Draws Images from other Images.
 *
 * When unpacking a SpriteSheet, it becomes necessary to re-draw each sprite onto a separate image for later
 * merging and packing.
 */
class SpriteDrawer {
    private val logger = LogManager.getLogger(SpriteSheetUnpacker::class.simpleName)

    /**
     * Each sprite is drawn directly out of a parent image and into a new BufferedImage.
     *
     * If the suggested area is larger than the source image, the entire image is drawn instead. This is a workaround
     * for the case that a spritesheet contains no or only a single sprite that has the width and height of the source
     * image.
     *
     * @param from the source image to extract an image from
     * @param area the (x,y) and widthxheight area which to copy onto a new image
     * @param colorToClear the backgroundColor that will be ignored (drawn transparent) while copying
     * @return A copy of the area in the source image with all backgroundColor pixels removed.
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

    /**
     * Draws multiple sprites into a list of new images.
     *
     * @param from the source image to extract an image from
     * @param areas a list of (x,y) and widthxheight areas which will be copied to a new image
     * @param colorToClear the backgroundColor that will be ignored (drawn transparent) while copying
     * @return A list of images that were drawn from the areas specified.
     */
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