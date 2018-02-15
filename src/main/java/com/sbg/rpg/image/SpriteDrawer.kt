package com.sbg.rpg.image

import com.sbg.rpg.util.iterator
import java.awt.Rectangle
import java.awt.image.BufferedImage

class SpriteDrawer : ISpriteDrawer {
    override fun draw(sprite: Sprite, to: BufferedImage, bounds: Rectangle) {
        for (pixel in sprite) {
            to.setRGB(
                    bounds.x + pixel.point.x,
                    bounds.y + pixel.point.y,
                    pixel.color.rgb
            )
        }
    }
}