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
package com.sbg.rpg.ui

import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.image.WritableImage
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.BorderPane
import org.apache.logging.log4j.LogManager
import tornadofx.View
import java.awt.image.BufferedImage

class SpriteSheetProcessorView: View() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorView::class.simpleName)

    override val root: BorderPane by fxml("/views/SpriteSheetView.fxml")

    val controller: SpriteSheetProcessorController by inject()

    val combineButton: Button by fxid()
    val separateButton: Button by fxid()
    val excludeButton: Button by fxid()
    val packButton: Button by fxid()
    val tutorialButton: Button by fxid()

    val canvas: Canvas by fxid()

    /**
     * OnClick event that's triggered when the user wants to combine one or more sprites into one.
     */
    @FXML
    fun onCombineSelected(e: ActionEvent) {
        logger.debug("onCombineSelected")
    }

    /**
     * OnClick event that's triggered when the user wants to separate one or more sprites into one.
     */
    @FXML
    fun onSeparateSelected(e: ActionEvent) {
        logger.debug("onSeparateSelected")
    }

    @FXML
    /**
     *
     */
    fun onExcludeSelected(e: ActionEvent) {
        logger.debug("onExcludeSelected")
    }

    /**
     * OnClick event that's triggered when the user wishes to hide the tutorial panel.
     */
    @FXML
    fun onTutorialCompleted(e: ActionEvent) {
        logger.debug("onTutorialCompleted")
    }

    /**
     * OnClick event that's triggered when the user wants to pack the sprites into a sprite sheet.
     */
    @FXML
    fun onPackSpriteSheet(e: ActionEvent) {
        logger.debug("onPackSpriteSheet")
    }

    @FXML
    fun onCanvasDragging(e: DragEvent) {
        val dragboard = e.dragboard
        if (dragboard.hasFiles())
            e.acceptTransferModes(*TransferMode.ANY)

        e.consume()
    }

    @FXML
    fun onCanvasDropped(e: DragEvent) {
        val dragboard = e.dragboard

        if (dragboard.hasFiles()) {
            val files = dragboard.files

            runAsync {
                controller.unpackSpriteSheets(files)
            } ui {
                drawSprites(it)
            }
        }
    }

    fun onResize() {
        // TODO: Redraw everything on resize
        throw NotImplementedError()
    }

    fun drawSprites(spriteSequence: Sequence<List<BufferedImage>>) {
        val graphics = canvas.graphicsContext2D

        spriteSequence.forEachIndexed { y, sprites ->
            sprites.forEachIndexed { x, sprite ->
                val writableImage = WritableImage(sprite.width, sprite.height)
                SwingFXUtils.toFXImage(sprite, writableImage)

                graphics.drawImage(
                        writableImage,
                        (x * 30.0 + sprite.width),
                        y * 50.0)
            }
        }
    }
}