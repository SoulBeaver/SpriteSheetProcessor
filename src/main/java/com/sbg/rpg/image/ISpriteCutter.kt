package com.sbg.rpg.image

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage

/**
 * Draws Images from other Images.
 *
 * When unpacking a SpriteSheet, it becomes necessary to re-cut each sprite onto a separate image for later
 * merging and packing.
 */
interface ISpriteCutter {

    /**
     * Each sprite is drawn directly out of a parent image and into a new BufferedImage.
     *
     * If the suggested area is larger than the source image, the entire image is drawn instead. This is a workaround
     * for the case that a spritesheet contains no or only a single sprite that has the width and height of the source
     * image.
     *
     * @param from the source image to extract an image from
     * @param area the (x,y) and widthxheight area which to copy onto a new image
     * @param colorToClear the backgroundColor that will be ignored (drawn transparent) while copying
     * @return A copy of the area in the source image with all backgroundColor pixels removed.
     */
    fun cut(from: BufferedImage, area: Rectangle, colorToClear: Color): Sprite


    /**
     * Draws multiple sprites into a list of new images.
     *
     * @param from the source image to extract an image from
     * @param areas a list of (x,y) and widthxheight areas which will be copied to a new image
     * @param colorToClear the backgroundColor that will be ignored (drawn transparent) while copying
     * @return A list of images that were drawn from the areas specified.
     */
    fun cutMultiple(from: BufferedImage, areas: List<Rectangle>, colorToClear: Color): List<Sprite>
}