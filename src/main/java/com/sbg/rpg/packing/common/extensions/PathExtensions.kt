package com.sbg.rpg.packing.common.extensions

import com.sbg.rpg.packing.common.ImageReadException
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

fun Path.readImage(): BufferedImage {
    try {
        /*
         * An image is not guaranteed to contain an alpha channel.
         * Therefore we explicitly convert any loaded common to type ARGB for further processing.
         */
        return ImageIO.read(this.toFile()).toBufferedImage(BufferedImage.TYPE_INT_ARGB)
    } catch (e: Exception) {
        throw ImageReadException("Could not convert file to an image! Is this really an image?", e)
    }
}


fun Path.filenameWithoutExtension(): String {
    val filename = this.fileName.toString()

    val extensionStart = filename.lastIndexOf(".")
    return if (extensionStart == -1) {
        filename
    } else {
        filename.substring(0, extensionStart)
    }
}