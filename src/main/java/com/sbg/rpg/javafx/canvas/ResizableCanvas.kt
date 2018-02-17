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
package com.sbg.rpg.javafx.canvas

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
        gc.fill = Color.ANTIQUEWHITE
        gc.fillRect(0.0, 0.0, width, height)
        gc.fill = Color.TRANSPARENT
    }

    override fun isResizable() = true

    override fun prefWidth(height: Double) = width
    override fun prefHeight(width: Double) = height
}