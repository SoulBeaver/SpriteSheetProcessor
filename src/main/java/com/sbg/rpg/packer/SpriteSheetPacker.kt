package com.sbg.rpg.packer

import java.awt.Image
import java.awt.Rectangle
import java.util.ArrayList

data class PackedSpriteSheet(val spriteSheet: Image, val spritesBounds: List<Rectangle>)

fun packSprites(sprites: List<Image>): PackedSpriteSheet {
    return PackedSpriteSheet(sprites.first!!, ArrayList<Rectangle>())
}