package com.sbg.rpg.packer

import java.awt.Image
import java.awt.Rectangle
import java.util.ArrayList

data class SpriteBounds(val frame: Int, val bounds: Rectangle)

data class PackedSpriteSheet(val spriteSheet: Image, val spriteBoundsList: List<SpriteBounds>)

fun packSprites(sprites: List<Image>, margin: Int = 1): PackedSpriteSheet {
    // Calculate the minimum width and height of the sprite sheet
    val minimumWidth  = sprites map { it.getWidth(null) } reduce { current, next -> current + next }
    val minimumHeight = sprites.map { it.getHeight(null) } reduce { current, next -> current + next }

    // val spriteSheet = BufferedImage

    return PackedSpriteSheet(sprites.first!!, listOf())
}