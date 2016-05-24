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

import com.sbg.rpg.image.toJavaFXImage
import com.sbg.rpg.ui.canvas.ResizableCanvas
import com.sbg.rpg.ui.model.AnnotatedSpriteSheet
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import org.apache.logging.log4j.LogManager
import tornadofx.View

class SpriteSheetProcessorView: View() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorView::class.simpleName)

    override val root: BorderPane by fxml("/views/SpriteSheetView.fxml")

    val controller: SpriteSheetProcessorController by inject()

    val combineButton: Button by fxid()
    val separateButton: Button by fxid()
    val excludeButton: Button by fxid()
    val packButton: Button by fxid()
    val tutorialButton: Button by fxid()

    val canvasScrollPane: ScrollPane by fxid()
    val canvas: ResizableCanvas

    init {
        canvas = ResizableCanvas()
        canvas.width = 10000.0
        canvas.height = 10000.0

        canvasScrollPane.content = canvas

        canvas.setOnDragOver { onCanvasDragging(it) }
        canvas.setOnDragDropped { onCanvasDropped(it) }
    }

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
                drawAnnotatedSpriteSheets(it)
            }
        }
    }

    fun drawAnnotatedSpriteSheets(annotatedSpriteSheets: List<AnnotatedSpriteSheet>) {
        val graphics = canvas.graphicsContext2D

        var nextDrawHeight = 0.0

        annotatedSpriteSheets.forEach { annotatedSpriteSheet ->
            val (spriteSheet, spriteBoundsList) = annotatedSpriteSheet

            with (graphics) {
                fill = Color.ANTIQUEWHITE
                fillRect(0.0, 0.0, canvas.width, canvas.height)
                fill = Color.TRANSPARENT

                drawImage(
                        toJavaFXImage(spriteSheet),
                        0.0,
                        nextDrawHeight
                )

                spriteBoundsList.forEach { spriteBounds ->
                    strokeRect(
                            spriteBounds.getX(),
                            spriteBounds.getY() + nextDrawHeight,
                            spriteBounds.getWidth(),
                            spriteBounds.getHeight()
                    )
                }
            }

            nextDrawHeight += spriteSheet.height
        }
    }
}