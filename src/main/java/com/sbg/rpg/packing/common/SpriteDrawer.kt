package com.sbg.rpg.packing.common

import com.sbg.rpg.packing.common.extensions.iterator
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

class SpriteDrawer {
    private val transparent = Color(0, 0, 0, 0).rgb

    fun draw(sprite: Sprite, colorToClear: Color?): BufferedImage {
        val canvas = BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB)

        drawInto(sprite, canvas, Point(0, 0), colorToClear)

        return canvas
    }

    fun drawInto(sprite: Sprite, canvas: BufferedImage, startingPoint: Point, colorToClear: Color? = null) {
        for ((point, color) in sprite) {
            if (colorToClear != null && color == colorToClear) {
                canvas.setRGB(startingPoint.x + point.x, startingPoint.y + point.y, transparent)
            } else {
                canvas.setRGB(startingPoint.x + point.x, startingPoint.y + point.y, color.rgb)
            }
        }
    }
}