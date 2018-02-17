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
package com.sbg.rpg.unpacker

import com.sbg.rpg.image.*
import com.sbg.rpg.util.*
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
class SpriteSheetUnpacker(private val spriteCutter: ISpriteCutter) {
    private val logger = LogManager.getLogger(SpriteSheetUnpacker::class.simpleName)

    /**
     * Given a valid sprite sheet, detects and returns every individual sprite. The method may not be perfect and
     * return grouped sprites if they're not contiguous. Does not alter the source image in any way.
     *
     * @param spriteSheet the sprite sheet to unpack
     * @return list of extracted sprite images
     */
    fun unpack(spriteSheet: BufferedImage): List<BufferedImage> {
        val backgroundColor = spriteSheet.probableBackgroundColor()

        return calculateSpriteBounds(spriteSheet).pmap {
            spriteCutter.cut(spriteSheet, it, backgroundColor)
        }
    }

    /**
     * Given a valid sprite sheet, detects and returns the bounding rectangle of every individual sprite. The method may not be perfect and
     * return grouped sprites if they're not contiguous. Does not alter the source image in any way.
     *
     * @param spriteSheet the sprite sheet to unpack
     * @return list of extracted sprite bounds
     */
    fun calculateSpriteBounds(spriteSheet: BufferedImage): List<Rectangle> {
        val spriteDimensions = findSpriteDimensions(spriteSheet, spriteSheet.probableBackgroundColor())
        logger.debug("Found ${spriteDimensions.size} sprites.")

        return spriteDimensions
    }

    private fun findSpriteDimensions(image: BufferedImage,
                                     backgroundColor: Color): List<Rectangle> {
        val workingImage = image.copy()

        val spriteDimensions = ArrayList<Rectangle>()
        for (pixel in workingImage) {
            val (point, color) = pixel

            if (color != backgroundColor) {
                logger.debug("Found a sprite starting at (${point.x}, ${point.y})")
                val spritePlot = findContiguousSprite(workingImage, point, backgroundColor)
                val spriteRectangle = spanRectangleFrom(spritePlot)

                logger.debug("The identified sprite has an area of ${spriteRectangle.width}x${spriteRectangle.height}")

                spriteDimensions.add(spriteRectangle)
                workingImage.erasePoints(spritePlot, backgroundColor)
            }
        }

        return spriteDimensions
    }

    private fun findContiguousSprite(image: BufferedImage, point: Point, backgroundColor: Color): List<Point> {
        val unvisited = LinkedList<Point>()
        val visited   = hashSetOf(point)

        unvisited.addAll(neighbors(point, image).filter { image.getRGB(it.x, it.y) != backgroundColor.rgb })

        while (unvisited.isNotEmpty()) {
            val currentPoint = unvisited.pop()
            val currentColor = image.getRGB(currentPoint.x, currentPoint.y)

            if (currentColor != backgroundColor.rgb) {
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

        val left = points.reduce {
            current, next -> if (current.x > next.x) next else current
        }
        val top = points.reduce {
            current, next -> if (current.y > next.y) next else current
        }
        val right = points.reduce {
            current, next -> if (current.x < next.x) next else current
        }
        val bottom = points.reduce {
            current, next -> if (current.y < next.y) next else current
        }

        return Rectangle(left.x, top.y,
                right.x - left.x + 1,
                bottom.y - top.y + 1)
    }
}