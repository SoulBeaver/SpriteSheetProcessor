package com.sbg.rpg.packer

import org.apache.logging.log4j.LogManager
import java.awt.Image
import java.awt.image.BufferedImage

class SpriteSheetPacker {
    private val logger = LogManager.getLogger(SpriteSheetPacker::class.simpleName)

    fun packSprites(sprites: List<Image>, margin: Int = 1): PackedSpriteSheet {
        // Calculate the minimum width and height of the sprite sheet
        val minimumWidth  = sprites.map { it.getWidth(null)  }.reduce { current, next -> current + next }
        val minimumHeight = sprites.map { it.getHeight(null) }.reduce { current, next -> current + next }
        val marginArea    = margin * sprites.size * 4

        val spriteSheet = BufferedImage(minimumWidth + marginArea,
                minimumHeight + marginArea,
                BufferedImage.TYPE_INT_ARGB)

        for (sprite in sprites) {
            // TODO
        }

        return PackedSpriteSheet(sprites.first()!!, listOf())
    }
}

