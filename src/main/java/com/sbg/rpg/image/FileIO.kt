package com.sbg.rpg.image

import java.awt.Image
import java.nio.file.Path
import javax.imageio.ImageIO

fun readImage(path: Path): Image {
    try {
        return ImageIO.read(path.toFile())
    } catch (e: Exception) {
        throw ImageReadException("Could not convert file to an image! Is this really an image?", e)
    }
}