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
package com.sbg.rpg.packing.unpacker

import com.sbg.rpg.packing.common.*
import com.sbg.rpg.packing.common.extensions.*
import java.awt.image.BufferedImage
import java.awt.Image
import java.awt.Color
import java.awt.Rectangle
import java.awt.Point
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Motivation:  oftentimes a SpriteSheet is given without any further information. For example,
 * a spritesheet with twenty sprites from the famous Megaman series. How do you integrate this into your game?
 * Do you calculate the position and size of each sprite by hand? Of course not, apps like TexturePacker do a wonderful
 * job of taking individual sprites, packing them, and handing you an atlas with all necessary information.
 *
 * However, to do that, you need the individual sprites first! This class, SpriteSheetUnpacker, cuts up a SpriteSheet
 * and delivers the individual sprites.
 */
class SpriteSheetUnpacker(
        private val spriteCutter: SpriteCutter,
        private val colorSimilarityThreshold: Int = 10) {

    private val logger = LogManager.getLogger(SpriteSheetUnpacker::class.simpleName)

    /**
     * Given a valid sprite sheet, detects and returns every individual sprite. The method may not be perfect and
     * return grouped sprites if they're not contiguous. Does not alter the source common in any way.
     *
     * @param spriteSheet the sprite sheet to unpack
     * @return list of extracted sprite images
     */
    fun unpack(spriteSheet: BufferedImage): List<BufferedImage> {
        val backgroundColor = spriteSheet.probableBackgroundColor()
        logger.info("The background color (probably) is ${backgroundColor}")

        return calculateSpriteBounds(spriteSheet).pmap {
            spriteCutter.cut(spriteSheet, it, backgroundColor)
        }
    }

    /**
     * Given a valid sprite sheet, detects and returns the bounding rectangle of every individual sprite. The method may not be perfect and
     * return grouped sprites if they're not contiguous. Does not alter the source common in any way.
     *
     * @param spriteSheet the sprite sheet to unpack
     * @return list of extracted sprite bounds
     */
    fun calculateSpriteBounds(spriteSheet: BufferedImage): List<Rectangle> {
        val spriteDimensions = findSpriteDimensions(spriteSheet, spriteSheet.probableBackgroundColor())
        logger.info("Found ${spriteDimensions.size} sprites.")

        return spriteDimensions
    }

    private fun findSpriteDimensions(image: BufferedImage,
                                     backgroundColor: Color): List<Rectangle> {
        logger.info(image.type)
        logger.info(image.getRGB(0, 0))
        logger.info(image.colorModel)

        val workingImage = image.copy()

        val spriteDimensions = ArrayList<Rectangle>()
        for (pixel in workingImage) {
            val (point, color) = pixel

            if (distance(color, backgroundColor) > colorSimilarityThreshold) {
                logger.info("Found a sprite starting at (${point.x}, ${point.y}) with color $color")
                val spritePlot = findContiguousSprite(workingImage, point, backgroundColor)
                val spriteRectangle = spanRectangleFrom(spritePlot)

                logger.info("The identified sprite has an area of ${spriteRectangle.width}x${spriteRectangle.height}")

                spriteDimensions.add(spriteRectangle)
                workingImage.erasePoints(spritePlot, backgroundColor)
            }
        }

        return spriteDimensions
    }

    /*
     * Props to StackOverflow for this efficient algorithm:
     *
     *      https://stackoverflow.com/questions/9018016/how-to-compare-two-colors-for-similarity-difference
     *
     * and an explanation for this algorithm can be found here:
     *
     *      https://www.compuphase.com/cmetric.htm
     */
    private fun distance(backgroundColor: Color, color: Color): Double {
        val rMean = (backgroundColor.red + color.red) / 2

        val r = backgroundColor.red - color.red
        val g = backgroundColor.green - color.green
        val b = backgroundColor.blue - color.blue

        val weightR = 2.0 + rMean / 256.0
        val weightG = 4.0
        val weightB = 2.0 + (255.0 - rMean) / 256.0

        val distance = Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b)
        if (distance > 0.0) {
            logger.debug("The distance is $distance")
        }

        return distance
    }


    private fun findContiguousSprite(image: BufferedImage, point: Point, backgroundColor: Color): List<Point> {
        val unvisited = LinkedList<Point>()
        val visited = hashSetOf(point)

        unvisited.addAll(neighbors(point, image).filter { image.getRGB(it.x, it.y) != backgroundColor.rgb })

        while (unvisited.isNotEmpty()) {
            val currentPoint = unvisited.pop()
            val currentColor = Color(image.getRGB(currentPoint.x, currentPoint.y), true)

            if (distance(currentColor, backgroundColor) > colorSimilarityThreshold) {
                unvisited.addAll(neighbors(currentPoint, image).filter {
                    !visited.contains(it) &&
                            !unvisited.contains(it) &&
                            image.getRGB(it.x, it.y) != backgroundColor.rgb
                })

                visited.add(currentPoint)
            }
        }

        return visited.toList()
    }

    private fun neighbors(point: Point, image: Image): List<Point> {
        val neighbors = ArrayList<Point>(8)
        val imageWidth = image.getWidth(null) - 1
        val imageHeight = image.getHeight(null) - 1

        // Left neighbor
        if (point.x > 0)
            neighbors.add(Point(point.x - 1, point.y))

        // Right neighbor
        if (point.x < imageWidth)
            neighbors.add(Point(point.x + 1, point.y))

        // Top neighbor
        if (point.y > 0)
            neighbors.add(Point(point.x, point.y - 1))

        // Bottom neighbor
        if (point.y < imageHeight)
            neighbors.add(Point(point.x, point.y + 1))

        // Top-left neighbor
        if (point.x > 0 && point.y > 0)
            neighbors.add(Point(point.x - 1, point.y - 1))

        // Top-right neighbor
        if (point.x < imageWidth && point.y > 0)
            neighbors.add(Point(point.x + 1, point.y - 1))

        // Bottom-left neighbor
        if (point.x > 0 && point.y < imageHeight - 1)
            neighbors.add(Point(point.x - 1, point.y + 1))

        // Bottom-right neighbor
        if (point.x < imageWidth && point.y < imageHeight)
            neighbors.add(Point(point.x + 1, point.y + 1))

        return neighbors
    }

    private fun spanRectangleFrom(points: List<Point>): Rectangle {
        if (points.isEmpty())
            throw IllegalArgumentException("No points to span Rectangle from.")

        val left = points.reduce { current, next ->
            if (current.x > next.x) next else current
        }
        val top = points.reduce { current, next ->
            if (current.y > next.y) next else current
        }
        val right = points.reduce { current, next ->
            if (current.x < next.x) next else current
        }
        val bottom = points.reduce { current, next ->
            if (current.y < next.y) next else current
        }

        return Rectangle(left.x, top.y,
                right.x - left.x + 1,
                bottom.y - top.y + 1)
    }
}