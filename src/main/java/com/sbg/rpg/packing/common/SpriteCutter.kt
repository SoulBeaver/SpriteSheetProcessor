package com.sbg.rpg.packing.common

import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

class SpriteCutter(private val spriteDrawer: SpriteDrawer) {
    private val logger = LogManager.getLogger(SpriteCutter::class.simpleName)


    fun cut(from: BufferedImage, area: Rectangle, colorToClear: Color): BufferedImage {
        if (area.width > from.width || area.height > from.height) {
            logger.debug("Requested sub-common is larger than source common. Returning copy of source common instead.")

            area.width = Math.min(from.width, area.width)
            area.height = Math.min(from.height, area.height)
        }

        logger.debug("Drawing sprite from source area [${area.x}, ${area.y}, ${area.width}, ${area.height}]")
        val subImage = from.getSubimage(area.x, area.y,
                area.width, area.height)

        return spriteDrawer.draw(subImage, colorToClear)
    }


    fun cutMultiple(from: BufferedImage, areas: List<Rectangle>, colorToClear: Color) = areas.map { cut(from, it, colorToClear) }
}