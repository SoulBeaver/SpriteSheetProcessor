package com.sbg.rpg.packer

import com.sbg.rpg.image.ISpriteDrawer
import org.apache.logging.log4j.LogManager
import java.awt.image.BufferedImage

class SpriteSheetPacker(private val spriteDrawer: ISpriteDrawer) {
    private val logger = LogManager.getLogger(SpriteSheetPacker::class.simpleName)

    public fun pack(sprites: List<BufferedImage>): BufferedImage {
        return BufferedImage(0, 0, 0)
    }
}