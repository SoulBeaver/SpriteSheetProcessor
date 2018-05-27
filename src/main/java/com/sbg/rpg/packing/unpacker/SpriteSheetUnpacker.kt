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
        private val colorSimilarityThreshold: Int = 10,
        private val maxPixelDistance: Int = 3) {

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
        logger.debug("The background color (probably) is ${backgroundColor}")

        return detectSpriteDimensions(spriteSheet).pmap {
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
    fun detectSpriteDimensions(spriteSheet: BufferedImage): List<Rectangle> {

        // Find all sprite boundaries
        // Merge sprites completely inside other sprites
        // Merge sprites that have a face inside another sprite

        val allSpriteDimensions = findSpriteDimensions(spriteSheet, spriteSheet.probableBackgroundColor())
        logger.info("Found ${allSpriteDimensions.size} sprites.")

        // any sprites completely inside other sprites? merge 'em.
        val unconfinedSpriteDimensions = filterConfinedSpriteDimensions(allSpriteDimensions)

        // any sprites have one or more of their edges inside anothers? merge 'em.
        val mergedSpriteDimensions = mergeSpritesWithSharedEdges(unconfinedSpriteDimensions)

        return mergedSpriteDimensions
    }

    private fun filterConfinedSpriteDimensions(spriteDimensions: List<Rectangle>): List<Rectangle> {
        val unconfinedSpriteDimensions = mutableListOf<Rectangle>()

        val spriteDimensionsByArea = spriteDimensions.sortedBy(Rectangle::area).reversed()
        for (smallSpriteDimensions in spriteDimensions) {
            for (largeSpriteDimensions in spriteDimensionsByArea) {

                if (smallSpriteDimensions.area() >= largeSpriteDimensions.area()) {
                    unconfinedSpriteDimensions.add(smallSpriteDimensions)
                    break
                }

                if (largeSpriteDimensions.contains(smallSpriteDimensions)) {
                    break
                }
            }
        }

        return unconfinedSpriteDimensions
    }

    private fun mergeSpritesWithSharedEdges(spriteDimensions: List<Rectangle>): List<Rectangle> {
        val mergedSpriteDimensions = mutableListOf(*spriteDimensions.toTypedArray())

        val spriteDimensionsReversed = spriteDimensions.reversed()
        for (a: Rectangle in spriteDimensions) {
            for (b in spriteDimensionsReversed) {
                if (a == b) {
                    break
                }

                if (a.containsLine(b)) {
                    mergedSpriteDimensions.remove(a)
                    mergedSpriteDimensions.remove(b)

                    val c = a.union(b)
                    mergedSpriteDimensions.add(c)
                }
            }
        }

        return mergedSpriteDimensions
    }

    private fun findSpriteDimensions(image: BufferedImage,
                                     backgroundColor: Color): List<Rectangle> {
        val workingImage = image.copy()

        val spriteDimensions = ArrayList<Rectangle>()
        for (pixel in workingImage) {
            val (point, color) = pixel

            if (backgroundColor.distance(color) > colorSimilarityThreshold) {
                logger.debug("Found a sprite starting at (${point.x}, ${point.y}) with color $color")
                val spritePlot = findContiguousSprite(workingImage, point, backgroundColor)
                val spriteBoundaries = constructSpriteBoundaryFromPlot(spritePlot)

                logger.debug("The identified sprite has an area of ${spriteBoundaries.width}x${spriteBoundaries.height}")

                spriteDimensions.add(spriteBoundaries)
                workingImage.erasePoints(spritePlot, backgroundColor)
            }
        }

        return spriteDimensions
    }


    private fun findContiguousSprite(image: BufferedImage, point: Point, backgroundColor: Color): List<Point> {
        val unvisited = LinkedList<Point>()
        val visited = hashSetOf(point)

        unvisited.addAll(neighbors(point, image).filter { image.getRGB(it.x, it.y) != backgroundColor.rgb })

        while (unvisited.isNotEmpty()) {
            val currentPoint = unvisited.pop()
            val currentColor = Color(image.getRGB(currentPoint.x, currentPoint.y), true)

            if (backgroundColor.distance(currentColor) > colorSimilarityThreshold) {
                unvisited.addAll(neighbors(currentPoint, image).filter {
                    val neighborColor = Color(image.getRGB(it.x, it.y))

                    !visited.contains(it) &&
                            !unvisited.contains(it) &&
                            backgroundColor.distance(neighborColor) > colorSimilarityThreshold
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

    private fun constructSpriteBoundaryFromPlot(points: List<Point>): Rectangle {
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