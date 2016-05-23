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
import java.nio.file.Path
import java.nio.file.Files
import java.awt.image.BufferedImage
import java.awt.Image
import java.util.ArrayList
import java.awt.Color
import java.awt.Rectangle
import java.awt.Point
import java.util.LinkedList
import java.util.HashSet
import com.sbg.rpg.util.spanRectangleFrom
import org.apache.logging.log4j.LogManager

/**
* Motivation:  oftentimes a SpriteSheet is given without any further information. For example,
 * a spritesheet with twenty sprites from the famous Megaman series. How do you integrate this into your game?
 * Do you calculate the position and size of each sprite by hand? Of course not, apps like TexturePacker do a wonderful
 * job of taking individual sprites, packing them, and handing you an atlas with all necessary information.
 *
 * However, to do that, you need the individual sprites first! This class, SpriteSheetUnpacker, cuts up a SpriteSheet
 * and delivers the individual sprites.
*/
class SpriteSheetUnpacker {
    private val logger = LogManager.getLogger(SpriteSheetUnpacker::class.simpleName)

    private val spriteDrawer: SpriteDrawer

    init {
        spriteDrawer = SpriteDrawer()
    }

    /**
     * Given a valid path to a sprite sheet, detects and returns every individual sprite. The method may not be perfect and
     * return individual sprites if they're not contiguous. Adjust the distance value and see if that helps.
     *
     * @param spriteSheet path to sprite sheet
     * @return list of extracted sprite images
     * @throws IllegalArgumentException if the file could not be found
     */
    fun unpack(spriteSheet: Path): List<BufferedImage> {
        require(Files.exists(spriteSheet)) { "The file ${spriteSheet.fileName} does not exist" }

        logger.debug("Loading sprite sheet.")
        val spriteSheetImage = readImage(spriteSheet)

        logger.debug("Determining most probable background color.")
        val backgroundColor  = spriteSheetImage.determineProbableBackgroundColor()
        logger.debug("The most probable background color is $backgroundColor")

        val spriteDimensions = findSpriteDimensions(spriteSheetImage, backgroundColor)
        logger.info("Found ${spriteDimensions.size} sprites.")

        logger.debug("Drawing individual sprites.")
        return spriteDimensions.filter { it.width > 0 && it.height > 0 }.map { spriteDrawer.draw(spriteSheetImage, it, backgroundColor) }
    }

    private fun findSpriteDimensions(image: BufferedImage,
                                     backgroundColor: Color): List<Rectangle> {
        val workingImage = image.copy()

        val spriteDimensions = ArrayList<Rectangle>()
        for (pixel in workingImage) {
            val (point, color) = pixel

            if (color != backgroundColor) {
                logger.debug("Found a sprite starting at (${point.x}, ${point.y})")
                val spritePlot = findContiguous(workingImage, point) { it != backgroundColor }
                val spriteRectangle = spanRectangleFrom(spritePlot)

                logger.debug("The identified sprite has an area of ${spriteRectangle.width}x${spriteRectangle.height}")

                if (spriteRectangle.width > 500) {
                    logger.debug("Whoops!")
                    spanRectangleFrom(spritePlot)
                }

                spriteDimensions.add(spriteRectangle)
                workingImage.eraseSprite(backgroundColor, spritePlot)
            }
        }

        return spriteDimensions
    }

    private fun findContiguous(image: BufferedImage, point: Point, predicate: (Color) -> Boolean): List<Point> {
        val unvisited = LinkedList<Point>()
        val visited   = ArrayList<Point>()

        unvisited.addAll(neighbors(point, image).filter { predicate(Color(image.getRGB(it.x, it.y))) })

        while (unvisited.isNotEmpty()) {
            val currentPoint = unvisited.pop()
            val currentColor = Color(image.getRGB(currentPoint.x, currentPoint.y))

            if (predicate(currentColor)) {
                unvisited.addAll(neighbors(currentPoint, image).filter {
                    !visited.contains(it) && !unvisited.contains(it) &&
                            predicate(Color(image.getRGB(it.x, it.y)))
                })

                visited.add(currentPoint)
            }
        }

        return visited.distinct()
    }

    private fun neighbors(point: Point, image: Image): List<Point> {
        val points = ArrayList<Point>()
        val imageWidth = image.getWidth(null) - 1
        val imageHeight = image.getHeight(null) - 1

        // Left neighbor
        if (point.x > 0)
            points.add(Point(point.x - 1, point.y))

        // Right neighbor
        if (point.x < imageWidth)
            points.add(Point(point.x + 1, point.y))

        // Top neighbor
        if (point.y > 0)
            points.add(Point(point.x, point.y - 1))

        // Bottom neighbor
        if (point.y < imageHeight)
            points.add(Point(point.x, point.y + 1))

        // Top-left neighbor
        if (point.x > 0 && point.y > 0)
            points.add(Point(point.x, point.y - 1))

        // Top-right neighbor
        if (point.x < imageWidth && point.y > 0)
            points.add(Point(point.x, point.y - 1))

        // Bottom-left neighbor
        if (point.x > 0 && point.y < imageHeight - 1)
            points.add(Point(point.x, point.y + 1))

        // Bottom-right neighbor
        if (point.x < imageWidth && point.y < imageHeight)
            points.add(Point(point.x + 1, point.y + 1))

        return points
    }
}