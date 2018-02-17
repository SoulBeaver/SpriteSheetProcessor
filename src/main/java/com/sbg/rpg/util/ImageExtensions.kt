/*
 *  Copyright 2016 Christian Broomfield
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sbg.rpg.util

import com.sbg.rpg.image.Pixel
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import java.awt.*
import java.awt.image.BufferedImage
import java.util.*

operator fun BufferedImage.iterator(): Iterator<Pixel> {
    return object : Iterator<Pixel> {
        var currentX = 0
        var currentY = 0

        override fun hasNext(): Boolean {
            return currentY != height
        }

        override fun next(): Pixel {
            val point = Point(currentX, currentY)
            val color = Color(getRGB(currentX, currentY), true)

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

fun BufferedImage.toJavaFXImage(): WritableImage {
    val writableImage = WritableImage(this.width, this.height)
    SwingFXUtils.toFXImage(this, writableImage)

    return writableImage
}

fun BufferedImage.copy() = BufferedImage(
        colorModel,
        copyData(null),
        colorModel.isAlphaPremultiplied,
        null
)

fun BufferedImage.copyWithBorder(dimensions: Dimension, borderColor: Color): BufferedImage {
    require(dimensions.width > width) { "Expected a width larger than current image to be copied; width=${dimensions.width}" }
    require(dimensions.height > height) { "Expected a height larger than current image to be copied; height=${dimensions.height}" }

    val target = BufferedImage(dimensions.width,
            dimensions.height,
            type)

    val widthDifference  = (dimensions.width - width) / 2
    val heightDifference = (dimensions.height - height) / 2

    for (pixel in this) {
        val (point, color) = pixel

        target.setRGB(point.x + widthDifference,
                point.y + heightDifference,
                color.rgb)
    }

    for (x in 0..target.width - 1) {
        target.setRGB(x, 0, borderColor.rgb)
        target.setRGB(x, target.height - 1, borderColor.rgb)
    }

    for (y in 0..target.height - 1) {
        target.setRGB(0, y, borderColor.rgb)
        target.setRGB(target.width - 1, y, borderColor.rgb)
    }

    return target
}

fun BufferedImage.erasePoints(points: List<Point>, withColor: Color) {
    points.forEach { setRGB(it.x, it.y, withColor.rgb) }
}

fun BufferedImage.probableBackgroundColor(): Color {
    require(width > 0 && height > 0) { "Image must have positive, non-zero width and height; width=${width}, height=${height}" }

    val colorMap = HashMap<Color, Int>()

    for (x in 0..(width - 1)) {
        for (y in 0..(height - 1)) {
            val colorAtXY = Color(getRGB(x, y), true)

            colorMap[colorAtXY] = colorMap.getOrDefault(colorAtXY, 0) + 1
        }
    }

    return colorMap.max()!!.first
}

fun BufferedImage.area(): Int = this.height * this.width

fun Image.toBufferedImage(imageType: Int = BufferedImage.TYPE_INT_ARGB): BufferedImage {
    if (this is BufferedImage && this.type == imageType)
        return this

    val bufferedImage = BufferedImage(
            getWidth(null),
            getHeight(null),
            imageType
    )

    val graphics = bufferedImage.createGraphics()
    graphics.drawImage(this, 0, 0, null)
    graphics.dispose()

    return bufferedImage
}