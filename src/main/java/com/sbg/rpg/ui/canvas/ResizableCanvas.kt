package com.sbg.rpg.ui.canvas

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color

class ResizableCanvas: Canvas() {

    init {
        // Redraw canvas when size changes.
        widthProperty().addListener { obs -> draw() }
        heightProperty().addListener { obs -> draw() }
    }

    private fun draw() {
        val gc = graphicsContext2D
        gc.clearRect(0.0, 0.0, width, height)

        gc.stroke = Color.RED
        gc.strokeLine(0.0, 0.0, width, height)
        gc.strokeLine(0.0, height, width, 0.0)
    }

    override fun isResizable() = true

    override fun prefWidth(height: Double) = width
    override fun prefHeight(width: Double) = height
}