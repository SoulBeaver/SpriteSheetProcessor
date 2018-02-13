package com.sbg.rpg.image

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

interface ISpriteDrawer {
    fun draw(from: BufferedImage, area: Rectangle, colorToClear: Color): BufferedImage

    fun drawMultiple(from: BufferedImage, areas: List<Rectangle>, colorToClear: Color): List<BufferedImage>
}