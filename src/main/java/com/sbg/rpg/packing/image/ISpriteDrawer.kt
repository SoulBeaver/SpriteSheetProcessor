package com.sbg.rpg.packing.image

import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

interface ISpriteDrawer {

    fun draw(sprite: Sprite, colorToClear: Color?): BufferedImage

    fun drawInto(sprite: Sprite, canvas: BufferedImage, startingPoint: Point, colorToClear: Color? = null)
}