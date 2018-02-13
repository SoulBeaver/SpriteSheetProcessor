package com.sbg.rpg.image

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

interface ISpriteCutter {
    fun cut(from: BufferedImage, area: Rectangle, colorToClear: Color): Sprite

    fun cutMultiple(from: BufferedImage, areas: List<Rectangle>, colorToClear: Color): List<Sprite>
}