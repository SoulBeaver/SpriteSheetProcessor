package com.sbg.rpg.util

import java.awt.Rectangle
import java.awt.Image
import java.awt.Point

fun Rectangle(points: List<Point>, image: Image): Rectangle {
    if (points.isEmpty())
        return Rectangle(0, 0, 0, 0)

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

    val imageWidth = image.getWidth(null)
    val imageHeight = image.getHeight(null)

    return when {
        atBottomEdge(bottom, imageHeight) && atRightEdge(right, imageWidth) ->
            Rectangle(left.x, top.y, imageWidth, imageHeight)
        atBottomEdge(bottom, imageHeight) ->
            Rectangle(left.x, top.y, right.x - left.x, imageHeight)
        atRightEdge(right, imageWidth) ->
            Rectangle(left.x, top.y, imageWidth, bottom.y - top.y)
        else ->
            Rectangle(left.x, top.y, right.x - left.x, bottom.y - top.y)
    }
}

private fun atBottomEdge(candidate: Point, imageHeight: Int): Boolean {
    return (candidate.y == imageHeight - 1)
}

private fun atRightEdge(candidate: Point, imageWidth: Int): Boolean {
    return (candidate.x == imageWidth - 1)
}
