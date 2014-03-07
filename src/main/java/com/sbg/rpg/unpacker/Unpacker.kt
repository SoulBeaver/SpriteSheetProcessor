 package com.sbg.rpg.unpacker

import com.sbg.rpg.util.readImage
import java.nio.file.Path
import com.google.common.base.Preconditions
import java.nio.file.Files
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image
import java.util.ArrayList
import java.util.HashMap
import com.google.common.collect.ImmutableMap
import com.sbg.rpg.util.max
import java.awt.Color
import java.awt.Rectangle
import java.nio.file.Paths
import java.awt.Point
import java.util.LinkedList
import java.util.HashSet
import java.util.UUID
import java.awt.image.renderable.RenderableImage
import java.awt.image.RenderedImage
import com.sbg.rpg.util.toBufferedImage
import com.sbg.rpg.util.copy
import com.sbg.rpg.util.eraseSprite
import com.sbg.rpg.util.Rectangle
import com.sbg.rpg.util.determineProbableBackgroundColor
import com.sbg.rpg.util.copySubImage
import com.sbg.rpg.util.compose
import java.awt.Dimension
import com.sbg.rpg.util.copyWithBorder
import com.sbg.rpg.util.bindFirst
import com.sbg.rpg.util.bindSecond
import com.sbg.rpg.util.iterator
import com.sbg.rpg.util.iterable
import org.apache.logging.log4j.LogManager

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
    // TODO: Convert to png so we have an alpha layer to work with
    val spriteSheetImage = readImage(spriteSheet).toBufferedImage()

    logger.debug("Determining most probable background color.")
    val backgroundColor  = determineProbableBackgroundColor(spriteSheetImage)
    logger.debug("The most probable background color is $backgroundColor")

    return findSprites(spriteSheetImage, backgroundColor) map(::copySubImage.bindFirst(spriteSheetImage))
}

private fun findSprites(image: BufferedImage,
                        backgroundColor: Color): List<Rectangle> {
    val workingImage = copy(image)

    val spriteRectangles = ArrayList<Rectangle>()
    for (pixel in workingImage) {
        val (point, color) = pixel

        if (color != backgroundColor) {
            logger.debug("Found a sprite starting at (${point.x}, ${point.y})")
            val spritePlot = findContiguous(workingImage, point) { it != backgroundColor }
            val spriteRectangle = Rectangle(spritePlot, image)

            logger.debug("The identified sprite has an area of ${spriteRectangle.width}x${spriteRectangle.height}")

            spriteRectangles.add(spriteRectangle)
            eraseSprite(workingImage, backgroundColor, spritePlot)
        }
    }

    logger.info("Found ${spriteRectangles.size()} sprites.")
    return spriteRectangles
}

private fun findContiguous(image: BufferedImage, point: Point, predicate: (Color) -> Boolean): List<Point> {
    val unvisited = LinkedList<Point>()
    val visited   = HashSet<Point>()

    unvisited.addAll(neighbors(point, image) filter { predicate(Color(image.getRGB(it.x, it.y))) })

    while (unvisited.isNotEmpty()) {
        val currentPoint = unvisited.pop()
        val currentColor = Color(image.getRGB(currentPoint.x, currentPoint.y))

        if (predicate(currentColor)) {
            unvisited.addAll(neighbors(currentPoint, image) filter {
                !visited.contains(it) && !unvisited.contains(it) &&
                predicate(Color(image.getRGB(it.x, it.y)))
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

 /*
private fun cleanSprite(sprite: BufferedImage, backgroundColor: Color): BufferedImage {
    val spriteWithBorders = copyWithBorder(sprite,
                                           Dimension(sprite.getWidth() + 2,
                                                     sprite.getHeight() + 2),
                                           backgroundColor)

    val backgroundPlot = findContiguous(sprite, Point(0, 0)) { it == backgroundColor }
    for (point in backgroundPlot)
        spriteWithBorders.setRGB(point.x, point.y, Color(0, 0, 0, 0).getRGB())

    return copySubImage(spriteWithBorders, Rectangle(1, 1,
                                                     sprite.getWidth(), sprite.getHeight()))
}
 */