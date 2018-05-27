package com.sbg.rpg.packing.common.extensions

import java.awt.Color

/*
 * Props to StackOverflow for this efficient algorithm:
 *
 *      https://stackoverflow.com/questions/9018016/how-to-compare-two-colors-for-similarity-difference
 *
 * and an explanation for this algorithm can be found here:
 *
 *      https://www.compuphase.com/cmetric.htm
 */
public fun Color.distance(color: Color): Double {
    val rMean = (this.red + color.red) / 2

    val r = this.red - color.red
    val g = this.green - color.green
    val b = this.blue - color.blue

    val weightR = 2.0 + rMean / 256.0
    val weightG = 4.0
    val weightB = 2.0 + (255.0 - rMean) / 256.0

    return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b)
}