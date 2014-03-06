package com.sbg.rpg.packer

import java.awt.Image
import java.awt.Rectangle
import java.util.ArrayList

data class SpriteBounds(val frame: Int, val bounds: Rectangle)

data class PackedSpriteSheet(val spriteSheet: Image, val spritesBounds: List<SpriteBounds>)

fun packSprites(sprites: List<Image>): PackedSpriteSheet {
    return PackedSpriteSheet(sprites.first!!, listOf())
}