package com.sbg.rpg.ui.model

import java.awt.Rectangle
import java.awt.image.BufferedImage

data class AnnotatedSpriteSheet(val spriteSheet: BufferedImage, val spriteBoundsList: List<Rectangle>)