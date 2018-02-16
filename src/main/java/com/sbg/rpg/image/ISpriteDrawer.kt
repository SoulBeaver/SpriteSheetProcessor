package com.sbg.rpg.image

import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage

interface ISpriteDrawer {

    fun draw(sprite: Sprite, colorToClear: Color?): BufferedImage

    fun drawInto(sprite: Sprite, canvas: BufferedImage, startingPoint: Point, colorToClear: Color? = null)
}