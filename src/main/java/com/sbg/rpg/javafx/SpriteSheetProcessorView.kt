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
package com.sbg.rpg.javafx

import com.sbg.rpg.javafx.canvas.ResizableCanvas
import com.sbg.rpg.javafx.model.AnnotatedSpriteSheet
import com.sbg.rpg.packing.common.extensions.toJavaFXImage
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.effect.BlendMode
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.DirectoryChooser
import org.apache.logging.log4j.LogManager
import tornadofx.View
import tornadofx.hide
import java.awt.Point
import java.awt.Rectangle

class SpriteSheetProcessorView: View() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorView::class.simpleName)

    override val root: BorderPane by fxml("/views/spriteSheetView.fxml")

    val controller: SpriteSheetProcessorController by inject()

    val combineButton: Button by fxid()
    val separateButton: Button by fxid()
    val excludeButton: Button by fxid()
    val exportButton: Button by fxid()

    val helpPanel: VBox by fxid()
    val tutorialButton: Button by fxid()

    val statusLabel: Label by fxid()

    val canvasScrollPane: ScrollPane by fxid()
    val canvas: ResizableCanvas

    init {
        canvas = ResizableCanvas()
        canvas.width = 10000.0
        canvas.height = 10000.0

        canvasScrollPane.content = canvas

        canvas.setOnDragOver { onCanvasDragging(it) }
        canvas.setOnDragDropped { onCanvasDropped(it) }
        canvas.setOnMouseClicked { onMouseClicked(it) }
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

    /**
     *
     */
    @FXML
    fun onExcludeSelected(e: ActionEvent) {
        logger.debug("onExcludeSelected")
    }

    /**
     * OnClick event that's triggered when the user wishes to hide the tutorial panel.
     */
    @FXML
    fun onTutorialCompleted(e: ActionEvent) {
        logger.debug("onTutorialCompleted")

        helpPanel.hide()
    }

    /**
     * OnClick event that's triggered when the user wants to pack the sprites into a sprite sheet.
     */
    @FXML
    fun onExportSprites(e: ActionEvent) {
        val directoryChooser = DirectoryChooser()

        val selectedDirectory = directoryChooser.showDialog(primaryStage)
        if (selectedDirectory != null) {
            disableUI()

            displayStatus("Writing sprites to file, this might take a few seconds...")

            runAsync {
                controller.saveSprites(selectedDirectory)
            } ui {
                displayStatus("Finished writing sprites to file!")
                enableUI()
            }
        }
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

            disableUI()
            displayStatus("Loading sprite sheets,this should only take a moment...")

            runAsync {
                controller.unpackSpriteSheets(files)
            } ui {
                drawAnnotatedSpriteSheets(it)
                displayStatus("Finished loading sprite sheets!")
            }

            enableUI()
        }
    }

    fun drawAnnotatedSpriteSheets(annotatedSpriteSheets: List<AnnotatedSpriteSheet>) {
        val graphics = canvas.graphicsContext2D
        clearCanvas(graphics)

        var nextDrawHeight = 0.0
        annotatedSpriteSheets.forEach { annotatedSpriteSheet ->
            val (spriteSheet, spriteBoundsList) = annotatedSpriteSheet

            with (graphics) {

                drawImage(
                        spriteSheet.toJavaFXImage(),
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

    @FXML
    fun onMouseClicked(e: MouseEvent) {
        val clickCoords = Point(e.sceneX.toInt(), e.sceneY.toInt())
        logger.debug("Clicked on (${e.sceneX}, e.sceneY})")

        runAsync {
            controller.selectSprite(clickCoords)
        } ui { sprite ->
            if (sprite != null) {
                val graphics = canvas.graphicsContext2D

                with (graphics) {
                    setFill(Color.BLACK)
                    //setStroke(Color.BLACK)
                    //setGlobalBlendMode(BlendMode.SCREEN)
                    //globalAlpha = 0.5
                    fillRect(sprite.x.toDouble(), sprite.y.toDouble(), sprite.width.toDouble(), sprite.height.toDouble())
                    //fillRect(0.0, 0.0, 1000.0, 1000.0)
                }
            }
        }
    }

    private fun clearCanvas(graphics: GraphicsContext) {
        graphics.fill = Color.ANTIQUEWHITE
        graphics.fillRect(0.0, 0.0, canvas.width, canvas.height)
        graphics.fill = Color.TRANSPARENT
    }

    fun disableUI() {
        combineButton.isDisable = true
        separateButton.isDisable = true
        excludeButton.isDisable = true
        exportButton.isDisable = true
        tutorialButton.isDisable = true
    }

    fun enableUI() {
        combineButton.isDisable = true // TODO No functionality yet
        separateButton.isDisable = true // TODO No functionality yet
        excludeButton.isDisable = true // TODO No functionality yet
        exportButton.isDisable = false
        tutorialButton.isDisable = false
    }

    fun displayStatus(message: String) {
        statusLabel.text = message
    }
}