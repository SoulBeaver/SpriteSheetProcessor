package com.sbg.rpg.util

import java.awt.image.BufferedImage
import java.nio.file.Path
import java.awt.Image
import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Point
import java.util.HashMap
import java.awt.Rectangle
import java.awt.Dimension

data class Pixel(val point: Point, val color: Color)

operator fun BufferedImage.iterator(): Iterator<Pixel> {
    return object : Iterator<Pixel> {
        var currentX = 0
        var currentY = 0

        override fun hasNext(): Boolean {
            return currentY != height
        }

        override fun next(): Pixel {
            val point = Point(currentX, currentY)
            val color = Color(getRGB(currentX, currentY))

            if (currentX == width - 1) {
                currentX = 0
                currentY += 1
            } else {
                currentX += 1
            }

            return Pixel(point, color)
        }
    }
}

fun copy(image: BufferedImage): BufferedImage {
    val colorMode = image.colorModel

    return BufferedImage(colorMode,
                         image.copyData(null),
                         colorMode.isAlphaPremultiplied,
                         null)
}

fun copySubImage(original: BufferedImage, area: Rectangle): BufferedImage {
    require(area.x >= 0 && area.y >= 0) { "Rectangle outside of image bounds; x=${area.x}, y=${area.y}" }
    require(area.width > 0 && area.height > 0) { "Rectangle must have positive, non-zero width and height; width=${area.width}, height=${area.height}" }

    val subImage = original.getSubimage(area.x, area.y, area.width, area.height)
    val target = BufferedImage(subImage.width, subImage.height, subImage.type)

    for (pixel in subImage)
        target.setRGB(pixel.point.x, pixel.point.y, pixel.color.rgb)

    return target
}

fun copyWithBorder(sprite: BufferedImage, dimensions: Dimension, borderColor: Color): BufferedImage {
    require(dimensions.width > sprite.width) { "Expected a width larger than current image to be copied; width=${dimensions.width}" }
    require(dimensions.height > sprite.height) { "Expected a height larger than current image to be copied; height=${dimensions.height}" }

    val target = BufferedImage(dimensions.width,
                               dimensions.height,
                               sprite.type)

    val widthDifference  = (dimensions.width - sprite.width) / 2
    val heightDifference = (dimensions.height - sprite.height) / 2

    for (pixel in sprite) {
        val (point, color) = pixel

        target.setRGB(point.x + widthDifference,
                      point.y + heightDifference,
                      color.rgb)
    }

    for (x in 0..target.width - 1) {
        target.setRGB(x, 0, borderColor.getRGB())
        target.setRGB(x, target.height - 1, borderColor.rgb)
    }

    for (y in 0..target.height - 1) {
        target.setRGB(0, y, borderColor.getRGB())
        target.setRGB(target.width - 1, y, borderColor.rgb)
    }

    return target
}

fun Image.toBufferedImage(imageType: Int = BufferedImage.TYPE_INT_RGB): BufferedImage {
    if (this is BufferedImage)
        return this

    val bufferedImage = BufferedImage(getWidth(null),
                                      getHeight(null),
                                      imageType)
    val graphics = bufferedImage.createGraphics()
    graphics.drawImage(this, 0, 0, null)
    graphics.dispose()

    return bufferedImage
}

fun readImage(path: Path): Image {
    try {
        return ImageIO.read(path.toFile())
    } catch (e: Exception) {
        throw ImageReadException("Could not convert file to an image! Is this really an image?", e)
    }
}

class ImageReadException(val errorMessage: String = "", val c: Throwable? = null): RuntimeException(errorMessage, c)

fun eraseSprite(from: BufferedImage, withColor: Color, points: List<Point>) {
    points.forEach { from.setRGB(it.x, it.y, withColor.rgb) }
}

fun determineProbableBackgroundColor(image: BufferedImage): Color {
    require(image.width > 0 && image.height > 0) { "Image must have positive, non-zero width and height; width=${image.width}, height=${image.height}" }

    val width  = image.width
    val height = image.height

    val colorMap = HashMap<Color, Int>()

    for (x in 0..(width - 1)) {
        for (y in 0..(height - 1)) {
            val colorAtXY = Color(image.getRGB(x, y))

            colorMap[colorAtXY] = colorMap.getOrDefault(colorAtXY, 0) + 1
        }
    }

    return colorMap.max()!!.first
}