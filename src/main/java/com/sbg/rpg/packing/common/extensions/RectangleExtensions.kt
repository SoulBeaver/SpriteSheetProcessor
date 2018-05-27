package com.sbg.rpg.packing.common.extensions

import java.awt.Point
import java.awt.Rectangle

fun Rectangle.area() = this.width * this.height

fun Rectangle.containsLine(r: Rectangle): Boolean {
    val top = (this.x..this.x+this.width).map { Point(it, this.y) }
    val bottom = (this.x..this.x+this.width).map { Point(it, this.y + this.height) }
    val left = (this.y..this.y+this.height).map { Point(this.x, it) }
    val right = (this.y..this.y+this.height).map { Point(this.x+this.width, it) }

    return r.containsLine(top) ||
            r.containsLine(bottom) ||
            r.containsLine(left) ||
            r.containsLine(right)
}

fun Rectangle.containsLine(line: List<Point>): Boolean {
    for (point in line)
        if (!this.contains(point))
            return false

    return true
}