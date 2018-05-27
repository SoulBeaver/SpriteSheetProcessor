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

import com.sbg.rpg.packing.common.SpriteBounds
import com.sbg.rpg.packing.common.SpriteCutter
import com.sbg.rpg.packing.common.SpriteSheet
import com.sbg.rpg.packing.common.extensions.*
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage
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
        // TODO: make value user-configurable
        private val colorSimilarityThreshold: Int = 10,
        // TODO: user-configurable variable to adjust the unpacking algorithm which may or may not help the general effort
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
        logger.debug("The background color (probably) is $backgroundColor")

        return discoverSprites(spriteSheet).pmap {
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
    fun discoverSprites(spriteSheet: SpriteSheet): List<SpriteBounds> {

        val spriteBoundsList = findSprites(spriteSheet, spriteSheet.probableBackgroundColor())
        logger.info("Found ${spriteBoundsList.size} sprites.")

        // any sprites completely inside other sprites? exclude 'em.
        val filteredSpriteBoundsList = filterSpritesContainedCompletelyInOtherSprites(spriteBoundsList)
        logger.debug("${spriteBoundsList.size} sprites remaining after filtering.")

        // any sprites have one or more of their edges inside anothers? merge 'em.
        val filteredAndMergedSpriteBoundsList = mergeSpritesWithOverlappingEdges(filteredSpriteBoundsList)
        logger.debug("${spriteBoundsList.size} sprites remaining after merging sprites with overlapping edges.")

        return filteredAndMergedSpriteBoundsList
    }

    /*
     * Sometimes we have sprites that have components that aren't connected, but still part of the same sprite.
     *
     * See the `unpacker/Intersecting.png` image in the test/resources directly for an example of this. Goku's aura
     * near the top forms a crown that isn't directly connected to the rest of his sprite, but should clearly not be treated
     * as a separate sprite. Instead, we check if the edges of their corresponding boundaries are contained inside each other.
     *
     * It's very important that there is a distinction made between entire edges inside of other sprite bounds and not
     * simply searching for an intersection. Densely packed sprite sheets may have many intersections among sprites, but
     * should not be merged.
     *
     * As a consideration for the future, this option might become toggleable.
     */
    private fun mergeSpritesWithOverlappingEdges(spriteBoundsList: List<SpriteBounds>): List<SpriteBounds> {
        if (spriteBoundsList.size <= 1)
            return spriteBoundsList

        val mergedSpriteBoundsList = mutableListOf(*spriteBoundsList.toTypedArray())

        for (a: Rectangle in spriteBoundsList) {
            for (b in spriteBoundsList.reversed()) {
                if (a == b) {
                    break
                }

                if (a.containsLine(b)) {
                    mergedSpriteBoundsList.remove(a)
                    mergedSpriteBoundsList.remove(b)

                    val c = a.union(b)
                    mergedSpriteBoundsList.add(c)
                }
            }
        }

        return mergedSpriteBoundsList
    }

    /*
     * Some sprites are located entirely inside of other sprites. A sprite that is within another sprite completely
     * is considered part of that sprite and its boundaries are discarded.
     */
    private fun filterSpritesContainedCompletelyInOtherSprites(spriteBoundsList: List<SpriteBounds>): List<SpriteBounds> {
        if (spriteBoundsList.size <= 1)
            return spriteBoundsList

        val filteredSpriteBoundsList = mutableListOf<SpriteBounds>()

        val spriteBoundsListByAreaAsc = spriteBoundsList.sortedBy(SpriteBounds::area)
        for (spriteBoundsAsc in spriteBoundsListByAreaAsc) {
            for (spriteBoundsDesc in spriteBoundsListByAreaAsc.reversed()) {

                if (spriteBoundsAsc.area() >= spriteBoundsDesc.area()) {
                    filteredSpriteBoundsList.add(spriteBoundsAsc)
                    break
                }

                if (spriteBoundsDesc.contains(spriteBoundsAsc)) {
                    break
                }
            }
        }

        return filteredSpriteBoundsList
    }

    private fun findSprites(spriteSheet: SpriteSheet,
                            backgroundColor: Color): List<SpriteBounds> {
        val workingCopy = spriteSheet.copy()

        val spriteBoundsList = ArrayList<Rectangle>()
        for (pixel in workingCopy) {
            val (point, color) = pixel

            if (backgroundColor.distance(color) > colorSimilarityThreshold) {
                logger.debug("Found a sprite starting at (${point.x}, ${point.y}) with color $color")
                val spritePlot = plotSprite(workingCopy, point, backgroundColor)
                val spriteBounds = constructSpriteBoundsFromPlot(spritePlot)

                logger.debug("The identified sprite has an area of ${spriteBounds.width}x${spriteBounds.height}")

                spriteBoundsList.add(spriteBounds)
                workingCopy.erasePoints(spritePlot, backgroundColor)
            }
        }

        return spriteBoundsList
    }


    private fun plotSprite(spriteSheet: SpriteSheet, point: Point, backgroundColor: Color): List<Point> {
        val unvisited = LinkedList<Point>()
        val visited = hashSetOf(point)

        unvisited.addAll(neighbors(point, spriteSheet).filter { spriteSheet.getRGB(it.x, it.y) != backgroundColor.rgb })

        while (unvisited.isNotEmpty()) {
            val currentPoint = unvisited.pop()
            val currentColor = Color(spriteSheet.getRGB(currentPoint.x, currentPoint.y), true)

            if (backgroundColor.distance(currentColor) > colorSimilarityThreshold) {
                unvisited.addAll(neighbors(currentPoint, spriteSheet).filter {
                    val neighborColor = Color(spriteSheet.getRGB(it.x, it.y))

                    !visited.contains(it) &&
                            !unvisited.contains(it) &&
                            backgroundColor.distance(neighborColor) > colorSimilarityThreshold
                })

                visited.add(currentPoint)
            }
        }

        return visited.toList()
    }

    private fun neighbors(point: Point, spriteSheet: BufferedImage): List<Point> {
        val neighbors = ArrayList<Point>(8)
        val imageWidth = spriteSheet.getWidth(null) - 1
        val imageHeight = spriteSheet.getHeight(null) - 1

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

    private fun constructSpriteBoundsFromPlot(points: List<Point>): Rectangle {
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