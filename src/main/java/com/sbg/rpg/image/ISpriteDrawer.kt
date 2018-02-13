package com.sbg.rpg.image

import java.awt.Rectangle
import java.awt.image.BufferedImage

interface ISpriteDrawer {
    public fun draw(sprite: Sprite, to: BufferedImage, bounds: Rectangle)
}