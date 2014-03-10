package com.sbg.rpg.unpacker

import com.sbg.rpg.util.readImage
import java.nio.file.Path
import com.google.common.base.Preconditions
import java.nio.file.Files
import java.awt.image.BufferedImage
import java.awt.Image
import java.util.ArrayList
import java.awt.Color
import java.awt.Rectangle
import java.awt.Point
import java.util.LinkedList
import java.util.HashSet
import com.sbg.rpg.util.toBufferedImage
import com.sbg.rpg.util.copy
import com.sbg.rpg.util.eraseSprite
import com.sbg.rpg.util.Rectangle
import com.sbg.rpg.util.determineProbableBackgroundColor
import com.sbg.rpg.util.copySubImage
import java.awt.Dimension
import com.sbg.rpg.util.copyWithBorder
import com.sbg.rpg.util.iterator
import org.apache.logging.log4j.LogManager
import org.funktionale.partials.partially2
import org.funktionale.partials.partially1
import org.funktionale.composition.compose

private val logger = LogManager.getLogger("Unpacker")!!

/**
 * Given a valid path to a sprite sheet, detects and returns every individual sprite. The method may not be perfect and
 * return individual sprites if they're not contiguous. Adjust the distance value and see if that helps.
 *
 * @param spriteSheet path to sprite sheet
 * @param distance distance between non-contiguous sprites which are considered part of the same sprite
 * @return list of extracted sprite images
 * @throws IllegalArgumentException if the file could not be found
 */
fun unpack(spriteSheet: Path): List<Image> {
    Preconditions.checkArgument(Files.exists(spriteSheet),
            "The file ${spriteSheet.getFileName()} does not exist")

    logger.debug("Loading sprite sheet.")
    val spriteSheetImage = readImage(spriteSheet).toBufferedImage()

    logger.debug("Determining most probable background color.")
    val backgroundColor = determineProbableBackgroundColor(spriteSheetImage)
    logger.debug("The most probable background color is $backgroundColor")

    val copyAndClean = ::cleanSprite.partially2(backgroundColor).compose(::copySubImage.partially1(spriteSheetImage))


    return findSprites(spriteSheetImage, backgroundColor) map(copyAndClean)
}

private fun findSprites(image: BufferedImage,
                        backgroundColor: Color): List<Rectangle> {
    val workingImage = copy(image)

    val spriteRectangles = ArrayList<Rectangle>()
    for (pixel in workingImage) {
        val (point, color) = pixel

        if (color != backgroundColor) {
            logger.debug("Found a sprite starting at (${point.x}, ${point.y})")
            val spritePlot = plotSprite(workingImage, point, backgroundColor)
            val spriteRectangle = Rectangle(spritePlot, image)

            logger.debug("The identified sprite has an area of ${spriteRectangle.width}x${spriteRectangle.height}")

            spriteRectangles.add(spriteRectangle)
            eraseSprite(workingImage, backgroundColor, spritePlot)
        }
    }

    logger.info("Found ${spriteRectangles.size()} sprites.")
    return spriteRectangles
}

private fun plotSprite(image: BufferedImage, point: Point, backgroundColor: Color): List<Point> {
    val unvisited = LinkedList<Point>()
    val visited = HashSet<Point>()

    unvisited.addAll(neighbors(point, image) filter { image.getRGB(it.x, it.y) != backgroundColor.getRGB() })

    while (unvisited.isNotEmpty()) {
        val currentPoint = unvisited.pop()
        val currentColor = Color(image.getRGB(currentPoint.x, currentPoint.y))

        if (currentColor != backgroundColor) {
            unvisited.addAll(neighbors(currentPoint, image).filter {
                !visited.contains(it) && !unvisited.contains(it) &&
                image.getRGB(it.x, it.y) != backgroundColor.getRGB()
            })

            visited.add(currentPoint)
        }
    }

    return visited.toList()
}

private fun neighbors(point: Point, image: Image): List<Point> {
    val points = ArrayList<Point>()

    if (point.x > 0)
        points.add(Point(point.x - 1, point.y))
    if (point.x < image.getWidth(null) - 1)
        points.add(Point(point.x + 1, point.y))

    if (point.y > 0)
        points.add(Point(point.x, point.y - 1))
    if (point.y < image.getHeight(null) - 1)
        points.add(Point(point.x, point.y + 1))

    return points
}

private fun cleanSprite(sprite: BufferedImage, backgroundColor: Color): BufferedImage {
    val spriteWithBorders = copyWithBorder(sprite,
            Dimension(sprite.getWidth() + 2,
                    sprite.getHeight() + 2),
            backgroundColor)

    return sprite
}